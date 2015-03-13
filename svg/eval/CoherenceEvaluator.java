package svg.eval;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import svg.core.SVGRepository;
import svg.detect.CollisionDetector;
import svg.detect.HSymmetryDetector;
import svg.detect.SymmetryResult;
import svg.detect.VSymmetryDetector;
import svg.detect.design.RhythmDetector;
import svg.detect.design.RhythmResult;
import svg.reflection.detector.UnboundElementDetector;

/**
 * Class to evaluate the coherence of a drawing
 * @author Ivan Guerrero
 */
public class CoherenceEvaluator extends AbstractAction implements IEvaluator {
    private SVGRepository repository;
    private CoherenceResult result;
    
    public CoherenceEvaluator(SVGRepository repository) {
        this.repository = repository;    
    }
    
    @Override
    public IEvaluationResult evaluate(SVGRepository repository) {
        this.repository = repository;
        result = new CoherenceResult();
        result.setHorizontalBalance(getHorizontalBalance());
        result.setVerticalBalance(getVerticalBalance());
        result.setNumberOfCollisions(getCollisions());
        result.setNumberOfIsolatedElements(getIsolatedElements());
        result.setNumberOfRhythms(getRhythms());
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        evaluate(repository);
        JOptionPane.showMessageDialog(null, result, "Evaluation Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private double getHorizontalBalance() {
        HSymmetryDetector detector = (HSymmetryDetector)repository.getDetector(HSymmetryDetector.class);
        List<SymmetryResult> results = detector.getResults();
        SymmetryResult res = results.get(results.size() - 1);
        return res.getMassBalance();
    }

    private double getVerticalBalance() {
        VSymmetryDetector detector = (VSymmetryDetector)repository.getDetector(VSymmetryDetector.class);
        List<SymmetryResult> results = detector.getResults();
        SymmetryResult res = results.get(results.size() - 1);
        return res.getMassBalance();
    }

    private int getCollisions() {
        CollisionDetector detector = (CollisionDetector)repository.getDetector(CollisionDetector.class);
        return detector.getTotalCollisions();
    }

    private int getIsolatedElements() {
        UnboundElementDetector detector = new UnboundElementDetector();
        detector.setEnableRemoval(false);
        detector.execute(repository);
        return detector.getTotalUnboundElements();
    }

    private int getRhythms() {
        RhythmDetector detector = (RhythmDetector)repository.getDetector(RhythmDetector.class);
        List<RhythmResult> results = detector.getResults();
        if (results.size() > 0)
            return results.get(results.size()-1).patterns.size();
        else
            return 0;
        
        //RhythmDetector detector = new RhythmDetector();
        //detector.execute(repository);
        //return detector.getRhythms();
    }
    
    public CoherenceResult getCoherenceResult() {
        return result;
    }
}
