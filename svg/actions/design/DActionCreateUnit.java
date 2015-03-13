package svg.actions.design;

import ivangro.freespace.AnalyseFreeSpace;
import ivangro.freespace.EmptyRegion;
import java.awt.Dimension;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionCreateElement;
import svg.context.SimpleElement;
import svg.core.*;
import svg.detect.utils.CanvasAdapter;
import svg.detect.utils.ShapeFactory;
import svg.elems.*;

/**
 * Class to add a unit with the given number of elements
 * @author Ivan Guerrero
 */
public class DActionCreateUnit extends SVGDAction {
    private final int MAX_ELEMENTS = 6, MIN_ELEMENTS = 2, MAX_ELEMENTS_IN_UNIT = 20;
    private Random rand;
    private int dr, distType, shape, imageID, noElements, shapeID, rhythms, size;
    private boolean calledByUser, banDesignAction;
    
    /** UNIT elements rhythms size shape distanceType imageID shapeID? */
    private static Pattern createUnitPattern = 
            Pattern.compile("\\s*UNIT\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)(\\s+(\\d+))?.*");
    
    public DActionCreateUnit() {
        rand = new Random();
        actionName = "UNIT";
        banDesignAction = false;
    }

    /**
     * Creates a new group with random values for distance, shape, image, size , number of elements and shapeID
     * @param repository 
     */
    @Override
    public void applyAction(SVGRepository repository) {
        //If the properties of the design action have been set, employ them
        UnitSpecs specs = new UnitSpecs();
        if (properties.isEmpty()) {
            distType = rand.nextInt(ElementDistanceType.values().length);
            shape = (rand.nextInt(2) == 0) ? rand.nextInt(ElementShape.values().length - 1) : ElementShape.MULTIPLE.ordinal();
            imageID = rand.nextInt(ElementFactory.getInstance().getSVGElementSpecs().size());
            size = 1 + rand.nextInt(3);
            noElements = MIN_ELEMENTS + rand.nextInt(MAX_ELEMENTS - MIN_ELEMENTS);
            int noShapes = repository.getShapeStore().getShapes().size();
            shapeID = rand.nextInt(noShapes);
        }
        repository.setCurrentLevel(0);
        actionLevel = 1; //repository.getCurrentLevel(); The action level of a group creation process is always 1
        
        //Obtains the empty space where the element will be added
        CanvasAdapter canvas = new CanvasAdapter(repository);
        EmptyRegion region = AnalyseFreeSpace.findBiggestEmptyRegion(canvas);
        //Determines the shape size according to the shape of the empty region
        if (region.getRatio() >= 80)
            specs.setShape(ElementShape.VERTICAL);
        else if (region.getRatio() <= 0.2)
            specs.setShape(ElementShape.HORIZONTAL);
        else
            specs.setShape(ElementShape.values()[shape]);
        //TESTING
        //specs.setShape(ElementShape.MULTIPLE);
        
        if (Math.max(region.height, region.width) < SVGConfig.MAX_UNIT_DISTANCE * specs.getElements() / 2){
            //Remove the far distance from the available options
            distType = rand.nextInt(ElementDistanceType.values().length - 1);
        }
        
        //Determines the distance of the elements according to the size of the empty region
        specs.setElements(noElements);
        specs.setDistanceType(ElementDistanceType.values()[distType]);
        specs.setRhythms(SVGConfig.MIN_NUMBER_OF_RHYTHMS + rand.nextInt(SVGConfig.MAX_NUMBER_OF_RHYTHMS - SVGConfig.MIN_NUMBER_OF_RHYTHMS));
        specs.setSize(size);
        specs.setImageID(imageID);
        specs.setShapeID(shapeID);
            
        desc = actionName + " " + specs.getElements() + " " + specs.getRhythms() + " " + 
               specs.getSize() + " " + specs.getShape().ordinal() + " " + specs.getDistanceType().ordinal() + " " + 
               specs.getImageID() + ((specs.getShapeID() >= 0) ? " " + specs.getShapeID(): "");
        if (!isBanDesignAction())
            repository.addDesignAction(this);
        
        Logger.getGlobal().log(Level.INFO, "Unit generation {0}\nIn region {1}", new Object[]{specs, region});
        if (specs.getShape() == ElementShape.MULTIPLE) {
            element = ShapeFactory.create(repository, specs);
        }
        else {
            element = UnitFactory.getInstance().buildElement(specs);
        }
        determineGroupCenter(region);

        for (SVGElement elem : element.getSimpleElements()) {
            //Establishes if the element can be removed
            elem.setUserAdded(calledByUser);
            repository.applyAction(new ActionCreateElement(), elem, true);
            Logger.getGlobal().log(Level.FINE, "Elem added {0}", elem);
        }
        
        //Marks the last action as end of design action
        super.setDesignAction(repository);
        if (properties.isEmpty())
            setProperties(element);
    }
        
    /**
     * Generates a new unit with the same attributes of the given element.<br>
     * The generated unit is stored as the firstElement object.<br>
     * This method is employed during engagement to create a new unit with the same specs stored in the atom.
     * @param element
     * @param repository
     * @depends The method DActionCreateUnit.applyAction(UnitSpecs, SVGRepository) is called
     */
    @Override
    public void applyAction(SVGElement element, SVGRepository repository) {
        //Read this values from the given element
        UnitSpecs specs = new UnitSpecs();
        specs.setDistanceType(element.getDistanceType());
        if (element instanceof SimpleElement) {
            int noElems = ((SimpleElement)element).getNoElements();
            specs.setElements(Math.min(noElems, MAX_ELEMENTS_IN_UNIT));
            specs.setShapeID(((SimpleElement)element).getShapeID());
        }
        else if (element instanceof SVGUnit) {
            int noElems = ((SVGUnit)element).getElements().size();
            specs.setElements(Math.min(noElems, MAX_ELEMENTS_IN_UNIT));
        }
        
        specs.setShape(element.getShape());
        specs.setSize(element.getSize());
        specs.setRhythms(SVGConfig.MIN_NUMBER_OF_RHYTHMS + 
                         rand.nextInt(SVGConfig.MAX_NUMBER_OF_RHYTHMS - SVGConfig.MIN_NUMBER_OF_RHYTHMS));
        specs.setImageID(element.getImageID());
        
        actionLevel = 1; //repository.getCurrentLevel(); The action level of a group creation process is always 1
        applyAction(specs, repository);
    }
    
    /**
     * Generates a new unit with the attributes defined on the specs.<br>
     * The generated unit is stored as the firstElement object.<br>
     * @param specs
     * @param repository 
     */
    public void applyAction(UnitSpecs specs, SVGRepository repository) {
        int lastLevel = repository.getCurrentLevel();
        repository.setCurrentLevel(0);
        
        desc = actionName + " " + specs.getElements() + " " + specs.getRhythms() + " " + 
               specs.getSize() + " " + specs.getShape().ordinal() + " " + specs.getDistanceType().ordinal() + " " + 
               specs.getImageID() + " " + specs.getShapeID();
        
        if (!isBanDesignAction())
            repository.addDesignAction(this);
        
        if (!specs.getShape().equals(ElementShape.MULTIPLE)) {
            super.element = UnitFactory.getInstance().buildElement(specs);
        }
        else {
            super.element = ShapeFactory.create(repository, specs);
        }
        
        CanvasAdapter canvas = new CanvasAdapter(repository);
        EmptyRegion region = AnalyseFreeSpace.findBiggestEmptyRegion(canvas);
        determineGroupCenter(region);
        
        for (SVGElement elem : super.element.getSimpleElements()) {
            //Establishes if the element can be removed
            elem.setUserAdded(calledByUser);
            repository.applyAction(new ActionCreateElement(), elem, true);
            Logger.getGlobal().log(Level.FINE, "Elem added {0}", elem);
        }
        repository.runDetectors();
        repository.setCurrentLevel(1);
        SVGElement first = super.element.getSimpleElements().get(0);
        
        //TODO: Validate why shape and shapeID values are deleted
        super.element = repository.getElementAt(first.getCenterX(), first.getCenterY());
        super.element.setShapeID(specs.getShapeID());
        super.element.setShape(specs.getShape());
        repository.setCurrentLevel(lastLevel);
        
        //Marks the last action as end of design action
        super.setDesignAction(repository);
        //Set the level at which the action occurs
        actionLevel = 1;
        //Employed to store the atributes of the group in an atom
        setProperties(element);
    }

    public void applyAction(ivangro.shapes.Shape shape, SVGRepository repository) {
        imageID = rand.nextInt(ElementFactory.getInstance().getSVGElementSpecs().size());
        size = 1 + rand.nextInt(3);
        //noElements = MIN_ELEMENTS + rand.nextInt(MAX_ELEMENTS - MIN_ELEMENTS);
        distType = rand.nextInt(ElementDistanceType.values().length);
        
        UnitSpecs specs = new UnitSpecs();
        specs.setElements(shape.getPoints().size() + 1);
        specs.setDistanceType(ElementDistanceType.values()[distType]);
        specs.setRhythms(SVGConfig.MIN_NUMBER_OF_RHYTHMS + rand.nextInt(SVGConfig.MAX_NUMBER_OF_RHYTHMS - SVGConfig.MIN_NUMBER_OF_RHYTHMS));
        specs.setSize(size);
        specs.setImageID(imageID);
        specs.setShapeID(shape.getId());
        specs.setShape(ElementShape.MULTIPLE);
        
        applyAction(specs, repository);
    }
    /**
     * @return the calledByUser
     */
    public boolean isCalledByUser() {
        return calledByUser;
    }

    /**
     * @param calledByUser the calledByUser to set
     */
    public void setCalledByUser(boolean calledByUser) {
        this.calledByUser = calledByUser;
    }

    @Override
    public boolean parseAction(String actionStr, SVGRepository repository) {
        Matcher m = createUnitPattern.matcher(actionStr);
        if (m.matches()) {
            SimpleElement elem = new SimpleElement();
            elem.setNoElements(Integer.parseInt(m.group(1)));
            elem.setSize(Integer.parseInt(m.group(3)));
            elem.setShape(ElementShape.values()[Integer.parseInt(m.group(4))]);
            elem.setDistanceType(ElementDistanceType.values()[Integer.parseInt(m.group(5))]);
            if (m.group(6) != null) {
                //The imageID can be the defined on, or the maximum available
                int imageID = Integer.parseInt(m.group(6));
                elem.setImageID(imageID);
            }
            else
                elem.setImageID(0);
            if (m.group(8) != null) {
                int shapeID = Integer.parseInt(m.group(8));
                elem.setShapeID(shapeID);
            }
            applyAction(elem, repository);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean parseAction(String actionStr, SVGRepository repository, boolean execute) {
        if (execute)
            return parseAction(actionStr, repository);
        
        Matcher m = createUnitPattern.matcher(actionStr);
        if (m.matches()) {
            SimpleElement elem = new SimpleElement();
            elem.setNoElements(Integer.parseInt(m.group(1)));
            elem.setSize(Integer.parseInt(m.group(3)));
            elem.setShape(ElementShape.values()[Integer.parseInt(m.group(4))]);
            elem.setDistanceType(ElementDistanceType.values()[Integer.parseInt(m.group(5))]);
            if (m.group(6) != null) {
                //The imageID can be the defined on, or the maximum available
                int imageID = Integer.parseInt(m.group(6));
                elem.setImageID(imageID);   //Was -1
            }
            else
                elem.setImageID(0);
            if (m.group(8) != null) {
                int shapeID = Integer.parseInt(m.group(8));
                elem.setShapeID(shapeID);
            }
            desc = actionName + " " + elem.getNoElements() + " " + elem.getRhythms() + " " + 
               elem.getSize() + " " + elem.getShape().ordinal() + " " + elem.getDistanceType().ordinal() + " " +
               elem.getImageID() + ((elem.getShapeID() > 0) ? " " +  elem.getShapeID() : "");
            if (!isBanDesignAction())
                repository.addDesignAction(this);
            setProperties(elem);
            //Set the level at which the action occurs
            actionLevel = 1;
            return true;
        }
        
        return false;
    }

    /**
     * @return the banDesignAction
     */
    public boolean isBanDesignAction() {
        return banDesignAction;
    }

    /**
     * Determines if the action is going to be banned from the design action's list
     * @param banDesignAction the banDesignAction to set
     */
    public void setBanDesignAction(boolean banDesignAction) {
        this.banDesignAction = banDesignAction;
    }
    
    public void setActionText(String text) {
        Matcher matcher = createUnitPattern.matcher(text);
        if (matcher.matches()) {
            desc = text;
        }
    }

    public void setElement(SVGElement element) {
        super.element = element;
    }

    /**
     * Update the center of the element to correspond with a point inside the empty space
     * @param region The empty space available region
     */
    private void determineGroupCenter(EmptyRegion region) {
        int minCenterX, minCenterY, maxCenterX, maxCenterY, centerX, centerY;
        Dimension groupDimension = ((SVGUnit)element).getDimensions();
        minCenterX = region.getX() + (groupDimension.width / 2);
        minCenterY = region.getY() + (groupDimension.height / 2);
        maxCenterX = region.getX() + region.width - (groupDimension.width / 2);
        maxCenterY = region.getY() + region.height - (groupDimension.height / 2);
        centerX = (maxCenterX > minCenterX) ? rand.nextInt(maxCenterX - minCenterX) + minCenterX : region.getCenterX();
        centerY = (maxCenterY > minCenterY) ? rand.nextInt(maxCenterY - minCenterY) + minCenterY : region.getCenterY();
        element.setCenter(centerX, centerY);
    }

    private void setProperties(SVGElement elem) {
        if (elem instanceof SimpleElement)
            properties.setProperty("noElements", ((SimpleElement)elem).getNoElements()+"");
        else
            properties.setProperty("noElements", elem.getSimpleElements().size()+"");
        properties.setProperty("noRhythms", elem.getRhythms()+"");
        properties.setProperty("size", elem.getSize()+"");
        properties.setProperty("shape", elem.getShape().ordinal()+"");
        properties.setProperty("distance", elem.getDistanceType().ordinal()+"");
        properties.setProperty("imageID", elem.getImageID()+"");
        properties.setProperty("shapeID", elem.getShapeID()+"");
    }
    
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        noElements = Integer.parseInt(properties.getProperty("noElements"));
        rhythms = Integer.parseInt(properties.getProperty("noRhythms"));
        size = Integer.parseInt(properties.getProperty("size"));
        shape = Integer.parseInt(properties.getProperty("shape"));
        distType = Integer.parseInt(properties.getProperty("distance"));
        imageID = Integer.parseInt(properties.getProperty("imageID"));
        shapeID = Integer.parseInt(properties.getProperty("shapeID"));
    }
}