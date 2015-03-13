package svg.actions.design;

import java.util.Random;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.elems.ElementDistanceType;
import svg.elems.ElementFactory;
import svg.elems.ElementText;
import svg.elems.GraphicElement;
import svg.elems.SVGElementSpecs;
import svg.elems.SVGUnit;

/**
 * Class to generate units depending on the given specs
 * @author Ivan Guerrero
 */
public class UnitFactory {
    private static UnitFactory instance = new UnitFactory();
    private int OVERLAP_DISTANCE;
    private int CLOSE_DISTANCE;
    private int FAR_DISTANCE;
    private Random random;
    private double slopeX, slopeY;
    
    private UnitFactory() {
        //Set the default values for the distances. This values will be reset when an image is employed.
        if (SVGConfig.GRAPHIC_ELEMENT == GraphicElement.Text)
            OVERLAP_DISTANCE = (int)Math.round(SVGConfig.FONT_WIDTH_FACTOR * SVGConfig.defaultFontSize);
        else
            OVERLAP_DISTANCE = Math.max(SVGConfig.IMAGE_HEIGHT, SVGConfig.IMAGE_WIDTH);
        FAR_DISTANCE = SVGConfig.MAX_UNIT_DISTANCE;
        CLOSE_DISTANCE = SVGConfig.MAX_UNIT_DISTANCE / 2;
    }
    
    public static UnitFactory getInstance() {
        return instance;
    }
    
    public SVGElement buildElement(UnitSpecs specs) {
        //Determine the distances between elements according to the image dimensions
        determineDistances(specs);
        
        SVGUnit unit = new SVGUnit();
        int distance, x, y;
        int[] rhythms;
        random = new Random();
        slopeX = 0;
        slopeY = 0;
        
        ElementFactory factory = ElementFactory.getInstance();
        
        //Since elems >= rhythms-1, obtain the minimum value
        rhythms = new int[Math.min(specs.getElements()-1, specs.getRhythms())];
        
        for (int i=0; i<rhythms.length; i++) {
            //Obtain the distance between elements depending on the unit distance type for each rhythm
            switch (specs.getDistanceType()) {
                case OVERLAP:
                    distance = 1 + random.nextInt(OVERLAP_DISTANCE - 1);
                    break;
                case TANGENT:
                    distance = OVERLAP_DISTANCE;
                    break;
                case CLOSE:
                    distance = OVERLAP_DISTANCE + random.nextInt(Math.abs(CLOSE_DISTANCE - OVERLAP_DISTANCE));
                    break;
                case FAR:
                    distance = CLOSE_DISTANCE + random.nextInt(Math.abs(FAR_DISTANCE - CLOSE_DISTANCE));
                    break;
                default:
                    distance = 1;
            }
            rhythms[i] = distance;
        }
        
        SVGElement newElem = factory.createNewElement(specs.getSize(), specs.getImageID());
        newElem.setID("0");
        x = newElem.getCenterX() + SVGConfig.CANVAS_WIDTH / 2;
        y = newElem.getCenterY() + SVGConfig.CANVAS_HEIGHT / 2;
        newElem.setCenter(x, y);
        unit.addElement(newElem);
        
        if (specs.getDistanceType() != ElementDistanceType.SINGLE) {
            //Obtain the number of elements that will have the same rhythm
            int elemsPerRhythm = (int)Math.ceil(1.0 * specs.getElements() / Math.max(specs.getRhythms()-1, 1));
            elemsPerRhythm = Math.max(elemsPerRhythm, 1);

            for (int i=1; i<specs.getElements(); i++) {
                //Determine which rhythm will be employed
                int rhythmNumber = i / elemsPerRhythm;
                //If there's a new rhythm, put the element to a different distance based on the last rhythm generated
                int dist = (i % elemsPerRhythm == 0) ? rhythms[rhythms.length-1] : rhythms[rhythmNumber];

                int[] coords = obtainCoords(specs, dist);
                newElem = factory.createNewElement(specs.getSize(), specs.getImageID());
                newElem.setID(i + "");
                newElem.setCenter(x+coords[0], y+coords[1]);
                x = newElem.getCenterX();
                y = newElem.getCenterY();
                unit.addElement(newElem);
            }
        }
        
        return unit;
    }
    
    /**
     * Obtains the coordinates of a new point according to the unit specs and 
     * the distance between the new point and the previous in the segment.
     * @param specs
     * @param distance
     * @return The [x,y] offset for the new point
     */
    private int[] obtainCoords(UnitSpecs specs, int distance) {
        int dx, dy;
        if (slopeX == 0 && slopeY == 0) {
            slopeX = random.nextInt(distance);
            slopeY = (int)Math.round(Math.sqrt(Math.pow(distance, 2) - Math.pow(slopeX,2)));
            if (slopeX > slopeY) {
                slopeY /= slopeX * 1.0;
                slopeX = 1;
            }
            else {
                slopeX /= slopeY * 1.0;
                slopeY = 1;
            }
        }
        //Obtain the slope depending on the unit shape
        switch (specs.getShape()) {
            case HORIZONTAL:
                dy = 0;
                dx = distance;
                break;
            case VERTICAL:
                dy = distance;
                dx = 0;
                break;
            case NEGATIVE_TREND:
                dx = (int)Math.round(slopeX * distance);
                dy = -(int)Math.round(slopeY * distance);
                break;
            case POSITIVE_TREND:
                dx = (int)Math.round(slopeX * distance);
                dy = (int)Math.round(slopeY * distance);
                break;
            default:
                dx = 0;
                dy = 0;
        }
        return new int[]{dx, dy};
    }
    
    public ElementDistanceType getDistanceType(double distance, int size) {
        ElementDistanceType type;
        double factor = ElementText.getSizeFactor(size);
        
        if (distance < (OVERLAP_DISTANCE * factor))
            type = ElementDistanceType.OVERLAP;
        else if (distance == (OVERLAP_DISTANCE * factor))
            type = ElementDistanceType.TANGENT;
        else if (distance <= (CLOSE_DISTANCE * factor))
            type = ElementDistanceType.CLOSE;
        else
            type = ElementDistanceType.FAR;
        
        return type;
    }

    /**
     * Determine the available distances between elements according to the attributes of the given image.<br>
     * The distances determined are OVERLAP, CLOSE and FAR.
     * @param specs The image is obtained from the imageID property
     */
    private void determineDistances(UnitSpecs specs) {
        SVGElementSpecs imageSpecs = ElementFactory.getInstance().getSVGElementSpecs(specs.getImageID());
        int height, width;
        height = imageSpecs.getImageHeight();
        width = imageSpecs.getImageWidth();
        OVERLAP_DISTANCE = Math.min(height, width);
        FAR_DISTANCE = SVGConfig.MAX_UNIT_DISTANCE;
        CLOSE_DISTANCE = SVGConfig.MAX_UNIT_DISTANCE / 2;
    }
}