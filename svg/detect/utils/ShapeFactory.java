package svg.detect.utils;

import ivangro.shapes.*;
import java.awt.Point;
import java.util.List;
import java.util.Random;
import svg.actions.design.UnitSpecs;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.ElementFactory;
import svg.elems.SVGUnit;

/**
 * Class to generate a shape from a group
 * @author Ivan Guerrero
 */
public class ShapeFactory {
    /**
     * Create a shape based on the given group
     * @param element
     * @return 
     */
    public static Shape create(SVGElement element) {
        Shape shape = new Shape();
        for (SVGElement elem : element.getSimpleElements()) {
            shape.addPoint(elem.getCenterX(), elem.getCenterY());
        }
        return shape;
    }

    /**
     * Creates a group based on the given specifications
     * @param repository
     * @param specs
     * @return 
     */
    public static SVGElement create(SVGRepository repository, UnitSpecs specs) {
        SVGUnit unit = new SVGUnit();
        Shape shape = null;
        int size;
        
        if (specs.getShapeID() >= 0) {
            shape = repository.getShapeStore().getShape(specs.getShapeID());
        }
        
        if (shape == null) {
            Random random = new Random();
            List<Shape> shapes = repository.getShapeStore().getShapes();
            shape = shapes.get(random.nextInt(shapes.size()));
        }
        
        unit.setShapeID(shape.getId());
        
        switch (specs.getDistanceType()) {
            case OVERLAP: size = (int)Math.round(shape.getRatioFactor() * 0.5);break;
            case CLOSE: size = (int)Math.round(shape.getRatioFactor() * 0.8);break;
            case TANGENT: size = (int)Math.round(shape.getRatioFactor());break;
            case FAR: size = (int)Math.round(shape.getRatioFactor() * 1.2);break;
            default: size = (int)Math.round(shape.getRatioFactor());
        }
        int centerX, centerY;
        centerX = SVGConfig.CANVAS_WIDTH / 2;
        centerY = SVGConfig.CANVAS_HEIGHT / 2;
        List<Point> points = shape.getPoints(centerX, centerY, size);
        
        unit.setCenter(centerX, centerY);
        int ids = 0;
        SVGElement elem = ElementFactory.getInstance().createNewElement(specs.getSize(), specs.getImageID());
        elem.setCenter(centerX, centerY);
        elem.setID(ids + "");
        ids++;
        unit.addElement(elem);
        for (Point p : points) {
            elem = ElementFactory.getInstance().createNewElement(specs.getSize(), specs.getImageID());
            elem.setCenter(p.x, p.y);
            elem.setID(ids + "");
            unit.addElement(elem);
            ids++;
        }
        
        return unit;
    }
}