package svg.detect;

import java.util.*;
import svg.context.CanvasContext;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGUnit;



/**
 * Class to group the elements found on a canvas.
 * The grouping considers the following aspects:
 * <ul>
 * <li>Type of image of each element</li>
 * <li>Size of the image</li>
 * <li>Position of the element</li>
 * </ul>
 * @author Ivan Guerrero
 */
public class MultiElementsUnitDetector implements IDetector {
    private SVGRepository repository;
    private final int MULTIPLE_SIZES = 4;
    private List<List<SVGElement>> elements;
    
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        this.repository = repository;
        this.elements = elements;
        //Every time a unit detection starts, remove the previous elements in context and in elements' list
        for (int i=elements.size()-1; i > 0; i--) {
            elements.remove(i);
        }
        repository.getContext().clearContexts();
        
        List<SVGElement> newLevel, lastLevel = null;
        int level = 1;
        int groupDifference;
        
        do {
            newLevel = detectGroups(elements.get(level-1), level);
            groupDifference = (lastLevel == null) ? newLevel.size() : lastLevel.size() - newLevel.size();
            if (groupDifference > 0) {
                elements.add(newLevel);
                createContext(newLevel, level);
                lastLevel = newLevel;
                level++;
            }
        }while (groupDifference > 1);
    }
    
    /**
     * Groups the given elements into units based on its type, size and location in the canvas.<br>
     * First groups elements with the same imageID and size by employing a proximity grouping.<br>
     * If there are isolated elements remaining<br>
     *  Group the elements with the same imageID by employing a proximity grouping.<br>
     * If there are isolated elements remaining<br>
     *  Group them by employing a proximity grouping.<br>
     * @param elems The ungrouped elements
     * @param level The level of the elements
     * @return A list of units with all the elements grouped
     */
    private List<SVGElement> detectGroups(List<SVGElement> elems, int level) {
        List<SVGElement> units = new ArrayList<>();
        Map<ImageGroupID, List<SVGElement>> images = new HashMap<>();
        List<SVGElement> ungroupedElems = new ArrayList<>();
        int groupCount = 0;
        
        //Separate the elements according to their imageID and size
        for (SVGElement elem : elems) {
            ImageGroupID id = new ImageGroupID(elem.getImageID(), elem.getSize());
            groupImage(id, elem, images);
        }
        
        //Group the elements of each segment (imageID / size) by their proximity
        RecursiveUnitDetector proximityDetector = new RecursiveUnitDetector();
        for (ImageGroupID id : images.keySet()) {
            List<SVGElement> list = images.get(id);
            List<SVGElement> groups = proximityDetector.detectGroups(list, level);
            for (SVGElement e : groups) {
                SVGUnit group = (SVGUnit)e;
                group.setImageID(id.imageID);
                group.setSize(id.size);
                
                if (group.getElements().size() > 1) {
                    e.setID("U" + level + "-" + groupCount);
                    groupCount++;
                    units.add(e);
                }
                else
                    ungroupedElems.add(e);
            }
        }
        
        //If there are isolated elements, try to group them with elements sharing the same imageID but different size
        images = new HashMap<>();
        for (SVGElement elem : ungroupedElems) {
            ImageGroupID id = new ImageGroupID(elem.getImageID(), MULTIPLE_SIZES);
            groupImage(id, elem, images);
        }
        
        ungroupedElems = new ArrayList<>();
        for (ImageGroupID id : images.keySet()) {
            List<SVGElement> list = images.get(id);
            List<SVGElement> groups = proximityDetector.detectGroups(list, level);
            for (SVGElement e : groups) {
                SVGUnit group = (SVGUnit)e;
                if (group.getElements().size() > 1) {
                    group.setImageID(id.imageID);
                    group.setSize(MULTIPLE_SIZES);
                    group.setID("U" + level + "-" + groupCount);
                    groupCount++;
                    units.add(e);
                }
                else {
                    ungroupedElems.add(e);
                }
            }
        }
        
        //If there are isolated elements, try to group them by proximity with different elements
        List<SVGElement> groups = proximityDetector.detectGroups(ungroupedElems, level);
        for (SVGElement e : groups) {
            SVGUnit group = (SVGUnit)e;
            if (group.getElements().size() > 1) {
                group.setImageID(-1);
                group.setSize(MULTIPLE_SIZES);
            }
            group.setID("U" + level + "-" + groupCount);   //TODO: Validate if MI is still needed
            groupCount++;
            units.add(e);
        }

        return units;
    }
    
    /**
     * Creates a context for the given elements at the given level. 
     * A context allows to store relations between the elements.
     * @param elements The elements at the given level
     * @param level The level of the elements
     */
    private void createContext(List<SVGElement> elements, int level) {
        CanvasContext context = repository.getContext();
        for (SVGElement elem : elements) {
            context.addElement(elem, level);
        }
    }

    @Override
    public String getDescription() {
        String unitText = "Detected units: \n";
        if (elements != null && elements.size() > 0) {
            List<SVGElement> list = elements.get(elements.size() - 1);
            for (SVGElement elem : list) {
                unitText += elem.toString() + "\n";
            }
        }
        else
            unitText = "";
        
        return unitText;
    }

    /**
     * Depending on the imageID and size of the given image, it is grouped inside the given lists
     * @param image The image to be grouped
     * @param images The list of available groups of images
     */
    private void groupImage(ImageGroupID id, SVGElement image, Map<ImageGroupID, List<SVGElement>> images) {
        if (images.containsKey(id)) {
            images.get(id).add(image);
        }
        else {
            List<SVGElement> list = new ArrayList<>();
            list.add(image);
            images.put(id, list);
        }
    }
}
class ImageGroupID {
    int imageID;
    int size;
    
    public ImageGroupID(int imageID, int size) {
        this.imageID = imageID;
        this.size = size;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageGroupID) {
            ImageGroupID id = (ImageGroupID)obj;
            return id.imageID == imageID && id.size == size;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.imageID;
        hash = 41 * hash + this.size;
        return hash;
    }
}