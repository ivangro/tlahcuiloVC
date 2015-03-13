package svg.detect.design;

import svg.core.SVGConfig;

/**
 * Enum to determine the area range of a unit
 * @author Ivan Guerrero
 */
public enum AreaRange {
    SMALL(0, 2), MEDIUM(2, 4), BIG(4, 10), EXTRA(10, Integer.MAX_VALUE);
    private int minArea, maxArea;
    private static double ELEMENT_AREA = SVGConfig.FONT_HEIGHT_FACTOR * SVGConfig.defaultFontSize * 
                                         SVGConfig.FONT_WIDTH_FACTOR * SVGConfig.defaultFontSize;
    
    static AreaRange forDecimal(double area) {
        area = area / ELEMENT_AREA;
        for (AreaRange range : AreaRange.values()) {
            if (area >= range.minArea && area <= range.maxArea)
                return range;
        }
        
        return EXTRA;
    }   
    
    AreaRange(int minArea, int maxArea) {
        this.minArea = minArea;
        this.maxArea = maxArea;
    }
    
    public int getMinElements() {
        return minArea;
    }
    
    public int getMaxElements() {
        return maxArea;
    }
}