package svg.detect;

import java.text.DecimalFormat;

/**
 *
 * @author Ivan Guerrero
 */
public class SymmetryResult {
    private boolean symmetric, mirrored;
    private int elemsInTheMiddle;
    private double firstMass, secondMass;
    
    public SymmetryResult() {}

    /**
     * Determines if a drawing is symmetric respect to an axis
     * @return TRUE if the analyzed drawing is symmetric
     */
    public boolean isSymmetric() {
        return symmetric;
    }

    /**
     * @param symmetric the symmetric to set
     */
    public void setSymmetric(boolean symmetric) {
        this.symmetric = symmetric;
    }

    /**
     * Determines if a drawing is symmetric as a mirrored image
     * @return TRUE if one half of a drawing is a mirror of the other half drawing
     */
    public boolean isMirrored() {
        return mirrored;
    }

    /**
     * @param mirrored the mirrored to set
     */
    public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }

    /**
     * @return the elemsInTheMiddle
     */
    public int getElemsInTheMiddle() {
        return elemsInTheMiddle;
    }

    /**
     * @param elemsInTheMiddle the elemsInTheMiddle to set
     */
    public void setElemsInTheMiddle(int elemsInTheMiddle) {
        this.elemsInTheMiddle = elemsInTheMiddle;
    }

    /**
     * @return the firstMass
     */
    public double getFirstMass() {
        return firstMass;
    }

    /**
     * @param firstMass the firstMass to set
     */
    public void setFirstMass(double firstMass) {
        this.firstMass = firstMass;
    }

    /**
     * @return the secondMass
     */
    public double getSecondMass() {
        return secondMass;
    }

    /**
     * @param secondMass the secondMass to set
     */
    public void setSecondMass(double secondMass) {
        this.secondMass = secondMass;
    }
    
    public String getDescription() {
        return "Translation symmetry: " + symmetric + 
                "\nMirrored symmetry: " + mirrored + 
                "\nBalance: " + getMassBalanceStr();
    }
    
    /**
     * Obtains the relation between the mass located in the each half of the drawing
     * @return 1 if the masses are equivalent. <1 if the first half contains more mass
     * >1 if the second half contains more mass.
     */
    public String getMassBalanceStr() {
        return new DecimalFormat("0.00").format(getMassBalance());
    }
    
    /**
     * Obtains the relation between the mass located in the each half of the drawing
     * @return 1 if the masses are equivalent. <1 if the first half contains more mass
     * >1 if the second half contains more mass.
     */
    public double getMassBalance() {
        if (getFirstMass() == 0 && getSecondMass() == 0)
            return 1;
        else if (getSecondMass() == 0)
            return getFirstMass();
        else
            return getFirstMass() / getSecondMass();
    }
}
