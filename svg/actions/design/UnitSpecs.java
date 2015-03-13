package svg.actions.design;

import svg.elems.ElementDistanceType;
import svg.elems.ElementShape;

/**
 * Class to determine the specs of a unit to be generated
 * @author Ivan Guerrero
 */
public class UnitSpecs {
    private ElementShape shape;
    private int elements;
    private int rhythms;
    private int size;
    private ElementDistanceType distanceType;
    private int imageID;
    /** If the shape is non-linear, stores the id of the stored shape */
    private int shapeID;
    
    public UnitSpecs() {
        elements = 1;
        rhythms = 1;
        size = 2;
        shape = ElementShape.HORIZONTAL;
        distanceType = ElementDistanceType.OVERLAP;
        imageID = 0;
        shapeID = -1;
    }

    /**
     * @return the shape
     */
    public ElementShape getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(ElementShape shape) {
        this.shape = shape;
    }
    
    public int getShapeID() {
        return shapeID;
    }
    
    public void setShapeID(int shapeID) {
        this.shapeID = shapeID;
    }

    /**
     * @return the elements
     */
    public int getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(int elements) {
        this.elements = elements;
    }

    /**
     * @return the rhythms
     */
    public int getRhythms() {
        return rhythms;
    }

    /**
     * The number of rhythms cannot be greater than the number of elements/2
     * @param rhythms the rhythms to set
     */
    public void setRhythms(int rhythms) {
        if (elements > 0)
        this.rhythms = Math.min(rhythms, elements/2);
    }

    /**
     * @return the distanceType
     */
    public ElementDistanceType getDistanceType() {
        return distanceType;
    }

    /**
     * @param distanceType the distanceType to set
     */
    public void setDistanceType(ElementDistanceType distanceType) {
        this.distanceType = distanceType;
    }
    
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getImageID() {
        return imageID;
    }
    
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
    
    @Override
    public String toString() {
        return "Shape: " + shape.name() + "\tDist: " + distanceType.name() +
               "\tElems: " + elements + "\tRhythms: " + rhythms + "\tSize: " + size + 
                "\tImage: " + imageID + "\tShape: " + shapeID;
    }

}
