package svg.elems;

import java.util.*;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Factory of basic graphic elements
 * @author Ivan Guerrero
 */
public class ElementFactory {
    private static ElementFactory instance = new ElementFactory();
    private List<SVGElementSpecs> elements;
    private SVGElementSpecs focalPoint;
    //TODO: Read background specs from ElementLoader and add the code to use it at BackgroundImageDrawingGenerator
    private SVGElementSpecs background;
    
    private ElementFactory() {
        elements = new ArrayList<>();
    }
    
    public static ElementFactory getInstance() {
        return instance;
    }
    
    public static ElementFactory getNewInstance() {
        return new ElementFactory();
    }
    
    /**
     * Creates a new simple element with the default size
     * @return 
     */
    public SVGElement createNewElement() {
        return createNewElement(SVGConfig.DEFAULT_ELEMENT_SIZE);
    }
    
    /**
     * Creates a new simple element with the given size
     * @param size
     * @return 
     */
    public SVGElement createNewElement(int size) {
        SVGElement elem = null;
        switch (SVGConfig.GRAPHIC_ELEMENT) {
            case Text: 
                elem = new ElementText("Q");
                elem.setColor(SVGConfig.FORE_COLOR);
                elem.setSize(size);
                break;
            case Image:
                SVGElementSpecs specs = getSVGElementSpecs(SVGConfig.CURRENT_IMAGE_ID);
                elem = new ElementImage(specs.getImageHeight(), specs.getImageWidth(), specs.getImagePath(), specs.getID());
                elem.setColor(SVGConfig.FORE_COLOR);
                elem.setSize(size);
                ((ElementImage)elem).setOpacity(specs.getImageAlpha() / 100.0);
                break;
        }
        
        return elem;
    }

    /**
     * Creates a new image element based on the given values
     * @param size Size of the image
     * @param imageID ID of the image
     * @return A new element representing the given imageID with the corresponding size
     */
    public SVGElement createNewElement(int size, int imageID) {
        SVGElement elem;
        SVGElementSpecs specs;
        switch (imageID) {
            case 100: specs = focalPoint; break;
            case 200: specs = background; break;
            default: 
                specs = (elements.size() > imageID) ? elements.get(imageID) : null;
        }
        //specs = (elements.size() > imageID) ? elements.get(imageID) : (imageID == focalPoint.getID()) ? focalPoint : elements.get(elements.size()-1);
        elem = new ElementImage(specs.getImageHeight(), specs.getImageWidth(), specs.getImagePath(), imageID);
        elem.setColor(SVGConfig.FORE_COLOR);
        elem.setSize(size);
        
        return elem;
    }
    
    public void addSVGElementSpecs(SVGElementSpecs specs) {
        elements.add(specs);
    }
    
    public void setFocalPointSpecs(SVGElementSpecs specs) {
        focalPoint = specs;
    }
    
    public SVGElementSpecs getFocalPointSpecs() {
        return focalPoint;
    }
    
    public void setBackgroundSpecs(SVGElementSpecs specs) {
        background = specs;
    }
    
    public SVGElementSpecs getBackgroundSpecs() {
        return background;
    }
    
    public List<SVGElementSpecs> getSVGElementSpecs() {
        return elements;
    }

    public SVGElementSpecs getSVGElementSpecs(int id) {
        SVGElementSpecs specs = new SVGElementSpecs();
        specs.setID(id);
        int index = elements.indexOf(specs);
        if (index >= 0)
            return elements.get(index);
        else if (focalPoint != null && id == focalPoint.getID())
            return focalPoint;
        else 
            return null;
    }

    /**
     * Obtains the factor to be employed to fix the maximum distance between elements in a group.
     * The default value is set in SVGConfig.MAX_UNIT_DISTANCE, but this factor depends on the size of the image.
     * The maximum factor is 2, and the lowest depends on the size of the image. 
     * Since the original value was established for images with size 30*30, 
     * these measurements are set as the basis for the factor value of 1.
     * @param element
     * @return 
     */
    public double getUnitFactor(SVGElement element) {
        double factor = 1;
        if (element != null) {
            SVGElementSpecs specs = getSVGElementSpecs(element.getImageID());
            if (specs != null) {
                int height = specs.getImageHeight();
                int width = specs.getImageWidth();
                factor = Math.max(height / 30, width / 30);
                factor = Math.min(factor, 2);
            }
        }
        
        return factor;
    }
}