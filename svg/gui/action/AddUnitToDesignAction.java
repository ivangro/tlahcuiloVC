package svg.gui.action;

import ivangro.shapes.Shape;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import svg.actions.design.DActionCreateUnit;
import svg.actions.design.UnitSpecs;
import svg.core.SVGAction;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.utils.ShapeFactory;
import svg.elems.ElementShape;
import svg.gui.IRefreshData;

/**
 * Adds the selected unit to the design as a create unit action
 * @author Ivan Guerrero
 */
public class AddUnitToDesignAction extends AbstractAction {
    private SVGRepository repository;
    private SVGElement element;
    private IRefreshData container;
    
    public AddUnitToDesignAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        String desc;
        
        element = container.getSelectedElement();
        DActionCreateUnit action = new DActionCreateUnit();
        action.setElement(element);
        UnitSpecs specs = new UnitSpecs();
        specs.setDistanceType(element.getDistanceType());
        specs.setElements(element.getSimpleElements().size());
        if (element.getShape() != ElementShape.MULTIPLE) {
            specs.setShape(element.getShape());
            desc = "";
        }
        else {
            Shape shape = ShapeFactory.create(element);
            repository.getShapeStore().addShape(shape);
            specs.setShapeID(shape.getId());
            desc = " " + shape.getId();
        }
        specs.setSize(element.getSize());
        specs.setRhythms(SVGConfig.MIN_NUMBER_OF_RHYTHMS);
        specs.setImageID(element.getImageID());
        
        desc = action.getActionName() + " " + specs.getElements() + " " + specs.getRhythms() + " " + 
               specs.getSize() + " " + specs.getShape().ordinal() + " " + specs.getDistanceType().ordinal() + " " +
               specs.getImageID() + desc;
        
        action.setActionText(desc);
        repository.addDesignAction(action);
        List<SVGAction> actions = repository.getActions();
        if (actions.size() > 0)
            actions.get(actions.size()-1).setEndOfDesignAction();
        
        container.refreshData();
    }
}