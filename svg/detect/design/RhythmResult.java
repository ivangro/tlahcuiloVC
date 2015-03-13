package svg.detect.design;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import svg.core.SVGConfig;

public class RhythmResult {
    public int level;
    public List<Double> patterns;
    
    public RhythmResult() {
        patterns = new ArrayList<>();
    }
    
    public boolean containsPattern(double pattern) {
        boolean res = false;
        for (Double p : patterns) {
            if (Math.abs(p - pattern) < SVGConfig.MIN_PATTERN_DISTANCE) {
                res = true;
                break;
            }
        }
        return res;
    }
    
    public void addPattern(double pattern) {
        if (!containsPattern(pattern) && pattern > SVGConfig.MIN_PATTERN_DISTANCE)
            patterns.add(pattern);
    }
    
    public void addPatterns(List<Double> patterns) {
        for (Double p : patterns) {
            addPattern(p);
        }
    }
    
    @Override
    public String toString() {
        return "Level: " + level + "\tPatterns: " + patterns.size() + "\t" + convertToString(patterns);
    }
    
    private String convertToString(List<Double> list) {
        List<String> numbers = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("#.##");
        for (Double number : list) {
            numbers.add(formatter.format(number));
        }
        
        return Arrays.toString(numbers.toArray(new String[0]));
    }
}