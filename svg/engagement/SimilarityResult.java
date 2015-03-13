package svg.engagement;

import java.util.Map;
import svg.core.SVGElement;

/**
 * Class to store the similarity result between a context and an atom
 * @author ivang_000
 */
public class SimilarityResult {
    /** The similarity percentage between the context and the atom */
    public double similarity;
    /** 
     * The mapping betwen the elements in the atom and the context.<br>
     * The key elements belong to the atom and each value associated belongs to the context.
     */
    public Map<SVGElement, SVGElement> vertexMap;
    /** The atom employed for the comparisson */
    public SVGAtom atom;
    
    @Override
    public String toString() {
        return "Similarity: " + similarity + "\nMapping: " + vertexMap.toString() + "\nAtom: " + atom;
    }
}