package svg.engagement;

import java.io.Serializable;
import java.util.*;

/**
 * Class to store a set of atoms with the same number of relations inside them
 * @author Ivan Guerrero
 */
public class SVGCell implements Serializable {
    private List<SVGAtom> atoms;
    private int elements;
    
    public SVGCell(int elements) {
        atoms = new ArrayList<>();
        this.elements = elements;
    }
    
    public void addAtom(SVGAtom atom) {
        atoms.add(atom);
    }
    
    public List<SVGAtom> getAtoms() {
        return atoms;
    }
    
    public int getElements() {
        return elements;
    }
    
    public boolean equals(SVGCell cell) {
        return cell.getElements() == elements;
    }
    
    @Override
    public String toString() {
        String text = "Cell (" + elements + ")\n";
        
        /*for (SVGAtom atom : atoms) {
            text += "\t" + atom.toString() + "\n";
        }*/
        return text;
    }
}
