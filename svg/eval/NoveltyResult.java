package svg.eval;

import java.text.DecimalFormat;

/**
 *
 * @author Ivan Guerrero
 */
public class NoveltyResult implements IEvaluationResult{
    private int drawingNumber;
    private double pixelPerc, rhythmPerc, symmetryPerc, areaPerc, groupPerc;

    /**
     * @return the pixelPerc
     */
    public double getPixelPerc() {
        return pixelPerc;
    }

    /**
     * @param pixelPerc the pixelPerc to set
     */
    public void setPixelPerc(double pixelPerc) {
        this.pixelPerc = pixelPerc;
    }

    /**
     * @return the rhythmPerc
     */
    public double getRhythmPerc() {
        return rhythmPerc;
    }

    /**
     * @param rhythmPerc the rhythmPerc to set
     */
    public void setRhythmPerc(double rhythmPerc) {
        this.rhythmPerc = rhythmPerc;
    }
    
    /**
     * @return the drawingNumber
     */
    public int getDrawingNumber() {
        return drawingNumber;
    }

    /**
     * @param drawingNumber the drawingNumber to set
     */
    public void setDrawingNumber(int drawingNumber) {
        this.drawingNumber = drawingNumber;
    }
    
    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("0.00");
        return "Drawing: " + drawingNumber + "\n" + 
               "Pixel: " + format.format(pixelPerc) + "\n" + 
               "Area: " + format.format(areaPerc) + "\n" + 
               "Symmetry: " + format.format(symmetryPerc) + "\n" + 
               "Rhythms: " + format.format(rhythmPerc) + "\n" + 
               "Groups: " + format.format(groupPerc) + "\n" +
               "Average: " + format.format(getResult());
    }
    
    @Override
    public double getResult() {
        return (pixelPerc + symmetryPerc + rhythmPerc + areaPerc + groupPerc) / 5;
    }


    /**
     * @return the symmetryPerc
     */
    public double getSymmetryPerc() {
        return symmetryPerc;
    }

    /**
     * @param symmetryPerc the symmetryPerc to set
     */
    public void setSymmetryPerc(double symmetryPerc) {
        this.symmetryPerc = symmetryPerc;
    }

    /**
     * @return the areaPerc
     */
    public double getAreaPerc() {
        return areaPerc;
    }

    /**
     * @param areaPerc the areaPerc to set
     */
    public void setAreaPerc(double areaPerc) {
        this.areaPerc = areaPerc;
    }

    public void setGroupPerc(double groupPerc) {
        this.groupPerc = groupPerc;
    }
}