package svg.engagement;

/**
 * Class to store the similarity between a context and an atom
 * @author Ivan Guerrero
 */
public class ContextSimilarity {
    /** inLevel represents the similarity between an atom in the same level of the context, 
     * and overall represents the maximum similarity between an atom in any level and the context */
    public SimilarityResult inLevel, overall;
    /** The level of the context employed */
    public int contextLevel;

    /**
     * Compares the maximum similarity result obtained for a given context with the given similarity result.
     * If the new similarity result is greater than the previous ones, this is stored as the new maximum.
     * @param simResult The similarity result to be compared against all the previous similarity results for the same context.
     * @param atom The atom employed for the similarity result.
     */
    public void evaluateResult(SimilarityResult simResult, SVGAtom atom) {
        simResult.atom = atom;
        if (atom.getLevel() == contextLevel) {
            if (inLevel == null)
                inLevel = simResult;
            else if (simResult.similarity > inLevel.similarity)
                    inLevel = simResult;
        }
        else {
            if (overall == null)
                overall = simResult;
            else if (simResult.similarity > overall.similarity)
                overall = simResult;
        }
    }
}