package svg.reflection.detector;

import edu.uci.ics.jung.graph.Graph;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.actions.ActionDeleteElement;
import svg.actions.ActionModifyElement;
import svg.context.SVGEdge;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGUnit;


/**
 * Class to determine the diversity of elements present in a drawing.
 * @author Ivan Guerrero
 */
public class DiversityDetector implements IReflectionDetector {
    /** Determines the maximum number of diverse elements allowed in a drawing */
    private static final int MAX_DIVERSE_ITEMS = 3;
    
    private List<ElementSpecs> diversity;
    private SVGRepository repository;
    
    /**
     * Determines the diversity of elements present in a drawing.<br>
     * If the number of diverse elements is greater than a fixed threshold, 
     * some of them are removed to preserve the homogeity of the visual composition.<br>
     * The attributes considered for an element to be diverse are the following:
     * <li>
     * <ul>Image</ul>
     * <ul>Size</ul>
     * <ul>Group shape on level 1</ul>
     * </li>
     * @param repository
     * @return 
     */
    @Override
    public boolean execute(SVGRepository repository) {
        boolean elementsRemoved = false, elementsModified = false;
        this.repository = repository;
        int lastLevel = repository.getCurrentLevel();
        countDiversity(repository.getElements(0));
        int diverseElements = diversity.size();
        if (diverseElements > MAX_DIVERSE_ITEMS) {
            repository.setCurrentLevel(1);
            //Identify the number of elements to remove
            int forRemoval = diverseElements - MAX_DIVERSE_ITEMS;
            //Identify the shapes to which the diverse fewer elements belong to and try to find a matching for them
            for (int i=0; i<forRemoval; i++) {
                ElementSpecs specs = diversity.get(i);
                //find the element with the given specs
                for (SVGElement e : specs.elements) {
                    //Find the group of the element
                    SVGUnit group = (SVGUnit)repository.findContainerAtLevel(e, 1);
                    //If the group has balance relations with another group
                    if (isBalanced(group)) {
                        modifyGroupSpecs(group);
                        elementsModified = true;
                        break;
                    }
                    else {
                        //Look for another group with a similar shape
                        if (isIsolatedShape(group)) {
                            //If no other group with the same shape exists, remove the group
                            removeGroup(group);
                            elementsRemoved = true;
                            break;
                        }
                    }
                }
            }
        }
        
        repository.setCurrentLevel(lastLevel);
        repository.runDetectors();
        if (!elementsRemoved && !elementsModified)
            Logger.getGlobal().log(Level.INFO, "{0} diverse elements. Diversity is between the valid limits.", diverseElements);
        else
            Logger.getGlobal().log(Level.INFO, "{0} diverse elements. Diversity off the limits, elements were removed or modified.", diverseElements);
        return false;
    }

    /**
     * Determine the number of diverse elements according to size and image.
     * @param elements The list of basic elements
     * @return The number of diverse elements
     */
    private void countDiversity(List<SVGElement> elements) {
        diversity = new ArrayList<>();
        for (SVGElement e : elements) {
            ElementSpecs specs = new ElementSpecs(e.getSize(), e.getImageID());
            addSpecs(specs, e);
        }
        ElementSpecs[] array = diversity.toArray(new ElementSpecs[0]);
        Arrays.sort(array);
        diversity = Arrays.asList(array);
        
    }

    private void addSpecs(ElementSpecs specs, SVGElement e) {
        if (diversity.contains(specs)) {
            specs = diversity.get(diversity.indexOf(specs));
            specs.elements.add(e);
        }
        else {
            specs.elements.add(e);
            diversity.add(specs);
        }
    }

    /**
     * Detects if the given group or one of the groups containing it, have balance relations with another group
     * @param group The group to analyze
     * @return TRUE if the group has balance relations with another group
     */
    private boolean isBalanced(SVGUnit group) {
        List<Graph<SVGElement, SVGEdge>> contexts = repository.getContext().getContexts();
        for (int i=1; i<contexts.size(); i++) {
            //Obtain the context at the i-th level
            Graph<SVGElement, SVGEdge> context = contexts.get(i);
            //Find the vertex to which the group belongs to
            SVGUnit u = (SVGUnit)repository.findContainerAtLevel(group, i);
            //Obtain the edges of the element
            Collection<SVGEdge> inEdges = context.getInEdges(u);
            for (SVGEdge edge : inEdges) {
                //If a balance relation exists, the element is balanced
                switch (edge.getType()) {
                    case HB:
                    case VB:
                        if (!edge.getSource().equals(edge.getTarget()))
                            return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Detects if the given group's shape is an isolated shape, or exist another group with the same shape.<br>
     * Two shapes are considered equivalent if they have the same shape without considering the number of elements.
     * @param group The group to analyze
     * @return TRUE is there are no additional groups with the same shape
     */
    private boolean isIsolatedShape(SVGUnit group) {
        List<List<SVGElement>> list = repository.getElements();
        for (int i=1; i<list.size(); i++) {
            List<SVGElement> elems = list.get(i);
            SVGUnit reference = (SVGUnit)repository.findContainerAtLevel(group, i);
            for (SVGElement e : elems) {
                SVGUnit u = (SVGUnit)e;
                if (!reference.equals(u) && reference.getShape() == u.getShape()) {
                    switch (reference.getShape()) {
                        case MULTIPLE:
                            //If the shape is multiple, validate that both groups have the same shapeID
                            if (reference.getShapeID() == u.getShapeID())
                                return false;
                            break;
                        case SINGLE_ELEMENT:
                            //If the shape is single, validate that both groups have the same image and size
                            if (reference.getSize() == u.getSize() && reference.getImageID() == u.getImageID())
                                return false;
                            break;
                        default:
                            //Any other shape is valid
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Remove the given group from the current drawing
     * @param group The group to be removed
     */
    private void removeGroup(SVGUnit group) {
        ActionDeleteElement del = new ActionDeleteElement();
        group.setUserAdded(false);
        repository.applyAction(del, group);
        Logger.getGlobal().log(Level.INFO, "{0} removed for diversity purposes", group);
    }

    /**
     * Adapt the group specs to incorporate it with the rest of the elements.<br>
     * This adaptation is because the group is valuable inside the curent drawing because it has balance relations with another group.
     * @param group The group to be modified
     */
    private void modifyGroupSpecs(SVGUnit group) {
        //Change the image of the group with a previously employed image
        ElementSpecs specs = diversity.get(diversity.size()-1);
        for (SVGElement e : group.getSimpleElements()) {
            ActionModifyElement action = new ActionModifyElement();
            action.setNewImageID(specs.image);
            action.setNewSize(specs.size);
            e.setUserAdded(false);
            repository.applyAction(action, e);
        }
        //TODO: Validate if the group attributes must be rewritten
        group.setImageID(specs.image);
        group.setSize(specs.size);
        group.determineCenter();
        Logger.getGlobal().log(Level.INFO, "{0} changed for diversity purposes", group);
    }
}
class ElementSpecs implements Comparable {
    int size;
    int image;
    List<SVGElement> elements;
    
    public ElementSpecs(int size, int image) {
        elements = new ArrayList<>();
        this.size = size;
        this.image = image;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ElementSpecs) {
            ElementSpecs es = (ElementSpecs)obj;
            return (es.size == size && es.image == image);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.size;
        hash = 89 * hash + this.image;
        return hash;
    }

    @Override
    public int compareTo(Object t) {
        if (t instanceof ElementSpecs) {
            ElementSpecs e1 = (ElementSpecs)t;
            if (elements.size() > e1.elements.size())
                return 1;
            else if (elements.size() < e1.elements.size())
                return -1;
            else return 0;
        }
        
        throw new ClassCastException("Incompatible clases");
    }
}