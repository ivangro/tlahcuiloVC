package svg.story;

import java.io.*;
import java.util.*;
import svg.core.SVGConfig;
import svg.core.SVGDAction;
import svg.core.SVGRepository;

/**
 * Class to generate the level curve for a given drawing.<br>
 * The level curve represents the level into which each action performed to create the drawing, was applied.
 * @author Ivan Guerrero
 */
public class LevelCurveManager {
    private static Map<Integer, List<Integer>> levelCurves;
    
    public static void loadLevelCurves() throws FileNotFoundException {
        levelCurves = new HashMap<>();
        File file = new File(SVGConfig.LEVEL_CURVE_FILE);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] elems = line.split(":");
            int storyID = Integer.parseInt(elems[0]);
            String[] levels = elems[1].trim().split(" ");
            List<Integer> levelCurve = new ArrayList<>();
            for (String level : levels) {
                levelCurve.add(Integer.parseInt(level));
            }
            levelCurves.put(storyID, levelCurve);
        }
    }

    /**
     * Obtains the level curve for the current drawing.<br>
     * The level curve is formed by the level into which each action was performed.
     * @param repository
     * @return The list of integers representing the levels for every action performed in the current drawing.
     */
    public static List<Integer> calculateLevelCurve(SVGRepository repository) {
        List<Integer> curve = new ArrayList<>();
        for (SVGDAction action : repository.getDesignActions()) {
            curve.add(action.getActionLevel());
        }
        return curve;
    }
    
    /**
     * Obtains the level curve for the current drawing and compares it against the level curves of the inspiring drawings.<br>
     * If a similar curve is found, it is employed as layout for the drawing.
     * @param repository
     * @return -1 If there's no available information to predict the following level, or the next level found in the most similar curve.
     */
    public static int predictFollowingLevel(SVGRepository repository) {
        int level = -1;
        double maxSimilarity = 0;
        int similarStory = -1;
        try {
            if (levelCurves == null)
                loadLevelCurves();

            List<Integer> currentCurve = calculateLevelCurve(repository);
            List<Integer> curve;
            for (Integer storyID : levelCurves.keySet()) {
                curve = levelCurves.get(storyID);
                double similarity = compareCurves(curve, currentCurve);
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    similarStory = storyID;
                }
            }
            
            if (similarStory >= 0) {
                curve = levelCurves.get(similarStory);
                if (currentCurve.size() < curve.size())
                    level = curve.get(currentCurve.size());
                else
                    level = curve.get(curve.size()-1);
            }
        } catch (IOException ioe) {}
        return level;
    }

    /**
     * Compares two given level curves. <br>
     * For this purpose the curves are converted to trend curves and the elements with the same trend are counted.<br>
     * The result of the comparison is the ratio between similar and disimilar elements in the trend curves.
     * @param curve
     * @param currentCurve
     * @return The ratio between similar and disimilar elements in the trend curves for each of the given curves.
     */
    private static double compareCurves(List<Integer> curve, List<Integer> currentCurve) {
        double similarity = 0;
        List<Integer> trend, currentTrend;
        trend = convertToTrends(curve);
        currentTrend = convertToTrends(currentCurve);
        int commonElements = Math.min(trend.size(), currentTrend.size());
        for (int i=0; i<commonElements; i++) {
            int elem = trend.get(i);
            int currentElem = currentTrend.get(i);
            if (elem == currentElem)
                similarity += 1;
        }
        
        if (commonElements > 0)
            similarity /= commonElements;
        
        return similarity;
    }
    
    /**
     * Converts a level curve to a trend curve.<br>
     * A trend curve compares the transition between each element on the level curve. 
     *  If the i-th element is lower than the i+1-th element, the trend value is +1
     *  If the i-th element is greater than the i+1-th element, the trend value is -1
     *  If the i-th element is equal than the i+1-th element, the trend value is 0
     * @param levelCurve
     * @return 
     */
    private static List<Integer> convertToTrends(List<Integer> levelCurve) {
        List<Integer> trendCurve = new ArrayList<>();
        if (!levelCurve.isEmpty()) {
            int previous = levelCurve.get(0);
            for (int i=1; i<levelCurve.size(); i++) {
                int current = levelCurve.get(i);
                if (previous < current)
                    trendCurve.add(+1);
                else if (previous > current)
                    trendCurve.add(-1);
                else 
                    trendCurve.add(0);
                previous = current;
            }
        }
        
        return trendCurve;
    }
}