package svg.eval;

/**
 *
 * @author Ivan Guerrero
 */
public class InterestResult implements IEvaluationResult{
    private int removedSwaps, insertedSwaps;
    private int focalPoints;
    private double noveltyEvaluation;
    
    @Override
    public double getResult() {
        double res = getInsertedSwaps() + getRemovedSwaps();
        res += (focalPoints > 0) ? 10 : 0;
        
        return res;
    }

    @Override
    public String toString() {
        String res = "Inserted relations: " + getInsertedSwaps() + "\nRemoved relations: " + getRemovedSwaps();
        res += "\nFocal points: " + getFocalPoints() + "\nNovelty: " + getNoveltyEvaluation();
        return res;
    }

    /**
     * @return the removedSwaps
     */
    public int getRemovedSwaps() {
        return removedSwaps;
    }

    /**
     * @param removedSwaps the removedSwaps to set
     */
    public void setRemovedSwaps(int removedSwaps) {
        this.removedSwaps = removedSwaps;
    }

    /**
     * @return the insertedSwaps
     */
    public int getInsertedSwaps() {
        return insertedSwaps;
    }

    /**
     * @param insertedSwaps the insertedSwaps to set
     */
    public void setInsertedSwaps(int insertedSwaps) {
        this.insertedSwaps = insertedSwaps;
    }

    /**
     * @return the noveltyEvaluation
     */
    public double getNoveltyEvaluation() {
        return noveltyEvaluation;
    }

    /**
     * @param noveltyEvaluation the noveltyEvaluation to set
     */
    public void setNoveltyEvaluation(double noveltyEvaluation) {
        this.noveltyEvaluation = noveltyEvaluation;
    }

    /**
     * @return the focalPoints
     */
    public int getFocalPoints() {
        return focalPoints;
    }

    /**
     * @param focalPoints the focalPoints to set
     */
    public void setFocalPoints(int focalPoints) {
        this.focalPoints = focalPoints;
    }
}
