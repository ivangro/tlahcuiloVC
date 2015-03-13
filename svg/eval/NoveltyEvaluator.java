package svg.eval;

import edu.uci.ics.jung.graph.Graph;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import svg.context.LoadPreviousStories;
import svg.context.SVGEdge;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.DensityDetector;
import svg.detect.HSymmetryDetector;
import svg.detect.ISymmetryDetector;
import svg.detect.RadialSymmetryDetector;
import svg.detect.SymmetryResult;
import svg.detect.VSymmetryDetector;
import svg.elems.SVGUnit;

/**
 * Class to evaluate the novelty of a drawing according to the following criteria:<br>
 * <ol>
 * <li>Similarity percentage at pixel level</li>
 * <li>Similarity percentage at density level (percentage of employed area)</li>
 * <li>Symmetric similarity percentage</li>
 * <li>Rhythm similarity percentage (Number of each type of rhythm on the drawing)</li>
 * <li>Group similarity percentage (Number of groups on every level of the drawing)</li>
 * <li>Novelty similarity percentage (The media of all the similarity measures)</li>
 * <li>Most similar previous drawing accorgind to the similarity percentage</li>
 * @author Ivan Guerrero
 */
public class NoveltyEvaluator extends AbstractAction implements IEvaluator {
    private SVGRepository repository;
    private NoveltyResult maxResult = null;
    private LoadPreviousStories storyLoader;
    
    public NoveltyEvaluator(SVGRepository repository) {
        this.repository = repository;
        storyLoader = LoadPreviousStories.getInstance();
    }
    
    @Override
    public IEvaluationResult evaluate(SVGRepository repository) {
        this.repository = repository;
        maxResult = null;
        double average = 0;
        int i=0;
        
        for (SVGRepository repo : storyLoader.getPreviousStories()) {
            NoveltyResult result = new NoveltyResult();
            //Compare pixel by pixel
            result.setPixelPerc(compareByPixel(repo, repository));
            //Compare areas
            result.setAreaPerc(compareByArea(repo, repository));
            //Compare symmetries
            result.setSymmetryPerc(compareBySymmetries(repo, repository));
            //Compare rhythms
            result.setRhythmPerc(compareByRhythms(repo, repository));
            //Compare number of groups
            result.setGroupPerc(compareByGroups(repo, repository));
            result.setDrawingNumber(i++);
            
            if (result.getResult() > average) {
                maxResult = result;
                average = result.getResult();
            }
        }
        return getNoveltyResult();
    }

    /**
     * Compares the current drawing against a previous drawing pixel by pixel<br>
     * To determine if a pixel exists on both drawings the getElementAt method of the repository is employed<br>
     * Obtains the percentage of pixels (x,y) filled or empty in both images in contrast with the  different pixels.
     * @param repo Repository with information of the previous drawing
     * @param repository Repository of the current drawing
     * @return The percentage of equivalent pixels in both drawings.
     */
    private double compareByPixel(SVGRepository repo, SVGRepository repository) {
        int count = 0, total;
        double perc;
        
        //Total of pixels on the drawing
        total = SVGConfig.CANVAS_WIDTH * SVGConfig.CANVAS_HEIGHT;
        
        for (int x=1; x<=SVGConfig.CANVAS_WIDTH; x++) {
            for (int y=1; y<=SVGConfig.CANVAS_HEIGHT; y++) {
                SVGElement e0 = repo.getElementAt(x, y);
                SVGElement e1 = repository.getElementAt(x, y);
                //Compares empty pixels and colored pixels
                //if ((e0 == null && e1 == null) || (e0 != null && e0 != null))
                //Compares only colored pixels
                if (e0 != null && e1 != null)
                    count++;
                if (e0 == null && e1 == null)
                    total--;
            }
        }
        
        perc = (double)count / total * 100;
        return perc;
    }

    /**
     * Determines the relation between the employed areas on each drawing
     * @param repo Repository with information of the previous drawing
     * @param repository Repository of the current drawing
     * @return The pecentage of the minArea / maxArea employed
     */
    private double compareByArea(SVGRepository repo, SVGRepository repository) {
        double area1, area2, perc;
        area1 = ((DensityDetector)repo.getDetector(DensityDetector.class)).getEmployedAred();
        area2 = ((DensityDetector)repository.getDetector(DensityDetector.class)).getEmployedAred();
        perc = Math.min(area1, area2) / Math.max(area1, area2) * 100;
        return perc;
    }

    /**
     * Obtains the rhythms on every level and compares them against the rhythms on the same level of the other drawing
     * @param repo Repository with information of the previous drawing
     * @param repository Repository of the current drawing
     * @return The average of percentage of equivalent rhythms between both drawings
     */
    private double compareByRhythms(SVGRepository repo, SVGRepository repository) {
        double perc;
        List<Double> percentages = new ArrayList<>();
        int minLevels = Math.min(repo.getLevels(), repository.getLevels());
        
        for (int i=1; i<minLevels; i++) {
            Set<Double> totalPatterns = new HashSet<>();
            
            List<SVGElement> elems1 = repo.getElements(i);
            Set<Double> patts1 = new HashSet<>();
            
            for (SVGElement elem : elems1) {
                List<Double> patts = (elem instanceof SVGUnit) ? ((SVGUnit)elem).getRhythmList() : new ArrayList<Double>();
                for (Double pattern : patts) {
                    totalPatterns.add(pattern);
                    patts1.add(pattern);
                }
            }
            
            List<SVGElement> elems2 = repository.getElements(i);
            Set<Double> patts2 = new HashSet<>();
            
            for (SVGElement elem : elems2) {
                List<Double> patts = (elem instanceof SVGUnit) ? ((SVGUnit)elem).getRhythmList() : new ArrayList<Double>();
                for (Double pattern : patts) {
                    totalPatterns.add(pattern);
                    patts2.add(pattern);
                }
            }
            
            perc = (patts1.size() + patts2.size() - totalPatterns.size()) / totalPatterns.size() * 100;
            percentages.add(perc);
        }
        
        perc = 0;
        for (Double p : percentages) {
            perc += p;
        }
        if (percentages.size() > 0)
            perc /= percentages.size();
        
        return perc;
    }

    /**
     * Obtains the symmetries on the last level of the drawings and compares them<br>
     * The result is the percentage of equivalent symmetries.<br>
     * 100% If Both drawings have the same results for H, V, R symmetries (translational or mirrored)
     * @param repo Repository with information of the previous drawing
     * @param repository Repository of the current drawing
     * @return The percentage of equivalent symmetries on the last level of the drawing
     */
    private double compareBySymmetries(SVGRepository repo, SVGRepository repository) {
        double perc;
        int count = 0;
        int total = 0;
        
        //Obtain the horizontal, vertical and radial symmetry results
        Class[] classes = new Class[] {HSymmetryDetector.class, VSymmetryDetector.class, RadialSymmetryDetector.class};
        
        for (Class symClass : classes) {
            List<SymmetryResult> res1 = ((ISymmetryDetector)repo.getDetector(symClass)).getResults();
            List<SymmetryResult> res2 = ((ISymmetryDetector)repository.getDetector(symClass)).getResults();
            //Compare the results for the last level
            SymmetryResult lastRes1 = res1.get(res1.size()-1);
            SymmetryResult lastRes2 = res2.get(res2.size()-1);

            if (!(lastRes1.isMirrored() ^ lastRes2.isMirrored()))
                count++;
            if (!(lastRes1.isSymmetric() ^ lastRes2.isSymmetric()))
                count++;
            total += 2;
        }
        
        perc = count * 100.0 / total;
        
        return perc;
    }
    
    /**
     * Compares the number of groups on every level of the drawing.<br>
     * Obtains the average of the comparison of generated groups on every level
     * @param repo Repository with information of the previous drawing
     * @param repository Repository of the current drawing
     * @return The average of the comparison of the number of generated groups on every level
     */
    private double compareByGroups(SVGRepository repo, SVGRepository repository) {
        double perc = 0;
        List<Graph<SVGElement, SVGEdge>> contexts1 = repo.getContext().getContexts();
        List<Graph<SVGElement, SVGEdge>> contexts2 = repository.getContext().getContexts();
        
        int noContexts = Math.min(contexts1.size(), contexts2.size());
        int maxContexts = Math.max(contexts1.size(), contexts2.size())-1;
        for (int i=1; i<noContexts; i++) {
            int groups1 = contexts1.get(i).getVertexCount();
            int groups2 = contexts2.get(i).getVertexCount();
            perc += Math.min(groups1, groups2) * 100.0 / Math.max(groups1, groups2);
        }
        perc /= maxContexts;
        
        return perc;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        evaluate(repository);
        JOptionPane.showMessageDialog(null, getNoveltyResult(), "Evaluation Result", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @return the maxResult
     */
    public NoveltyResult getNoveltyResult() {
        return maxResult;
    }
}
