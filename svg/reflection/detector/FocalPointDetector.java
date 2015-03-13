package svg.reflection.detector;

import ivangro.shapes.Shape;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.actions.design.DActionCreateUnit;
import svg.actions.design.UnitSpecs;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.ElementDistanceType;
import svg.elems.ElementFactory;
import svg.elems.ElementShape;
import svg.elems.SVGElementSpecs;

/**
 * Class to identify focal points inside a drawing based on the following parameters:<br>
 * Size<br>
 * Shape<br>
 * Empty space (contrast)<br>
 * @author Ivan Guerrero
 */
public class FocalPointDetector implements IReflectionDetector {
    private Map<Integer, Integer> images;
    private Map<Integer, Integer> sizes;
    private int focalPointImage, dominantImage;
    private int focalPointSize, dominantSize;
    private Random random;
    private SVGRepository repository;
    
    public FocalPointDetector() {
        random = new Random();
    }
    
    @Override
    public boolean execute(SVGRepository repository) {
        this.repository = repository;
        dominantImage = -1;
        dominantSize = -1;
        images = new HashMap<>();
        sizes = new HashMap<>();
        List<List<SVGElement>> elements = repository.getElements();

        if (!elements.isEmpty())
            detectFocalPoint(elements.get(0));

        return false;
    }

    private void detectFocalPoint(List<SVGElement> elements) {
        for (SVGElement elem : elements) {
            addImage(elem.getImageID());
            addSize(elem.getSize());
        }
        
        int noElements = elements.size();
        
        //Determines the most common image
        for (Integer image : images.keySet()) {
            if (images.get(image) == noElements)
                dominantImage = image;
            
        }
        //Determines the most common element size
        for (Integer size : sizes.keySet()) {
            if (sizes.get(size) == noElements)
                dominantSize = size;
        }
        
        //If there's a dominant attribute, create a focal point
        if (dominantImage >= 0|| dominantSize >= 0) {
            UnitSpecs specs = new UnitSpecs();
            SVGElementSpecs focalPointSpecs = ElementFactory.getInstance().getFocalPointSpecs();
            if (focalPointSpecs != null) {
                focalPointImage = focalPointSpecs.getID();
                specs.setImageID(focalPointImage);
            }
            else if (dominantImage >= 0) {
                do {
                    focalPointImage = random.nextInt(ElementFactory.getInstance().getSVGElementSpecs().size());
                } while (dominantImage == focalPointImage);
                specs.setImageID(focalPointImage);
            }
            else {
                specs.setImageID(random.nextInt(ElementFactory.getInstance().getSVGElementSpecs().size()));
            }
            
            if (dominantSize >= 0) {
                do {
                    focalPointSize = 1 + random.nextInt(3);
                } while (focalPointSize == dominantSize);
                specs.setSize(focalPointSize);
            }
            else {
                specs.setSize(1 + random.nextInt(3));
            }
            
            specs.setDistanceType(ElementDistanceType.values()[random.nextInt(ElementDistanceType.values().length)]);
            specs.setShape(ElementShape.values()[(random.nextInt(2) == 0) ? random.nextInt(ElementShape.values().length - 1) : ElementShape.MULTIPLE.ordinal()]);
            specs.setElements(1 + random.nextInt(5));
            List<Shape> shapes = repository.getShapeStore().getShapes();
            int noShapes = shapes.size();
            specs.setShapeID(shapes.get(random.nextInt(noShapes)).getId());
            specs.setRhythms(1);
            
            DActionCreateUnit action = new DActionCreateUnit();
            action.applyAction(specs, repository);
            Logger.getGlobal().log(Level.INFO, "Focal point added {0}", specs);
        }
        else {
            Logger.getGlobal().log(Level.INFO, "No focal point added");
        }
    }

    private void addImage(int imageID) {
        int count;
        count = (images.containsKey(imageID)) ? images.get(imageID) : 0;
        images.put(imageID, count+1);
    }
    
    private void addSize(int size) {
        int count;
        count = (sizes.containsKey(size)) ? sizes.get(size) : 0;
        sizes.put(size, count+1);
    }
}