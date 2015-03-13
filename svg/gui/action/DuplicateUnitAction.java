package svg.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import org.apache.batik.swing.JSVGCanvas;
import svg.actions.design.DActionCopyUnit;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 * Class to perform a unit copy action
 * @author Ivan Guerrero
 */
public class DuplicateUnitAction extends AbstractAction {
    private SVGRepository repository;
    private JSVGCanvas canvas;
    private MouseListener listener;
    private SVGElement element;
    private IRefreshData container;
    
    public DuplicateUnitAction(SVGRepository repository, JSVGCanvas canvas, IRefreshData container) {
        this.repository = repository;
        this.canvas = canvas;
        this.container = container;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        element = container.getSelectedElement();
        listener = new MyMouseListener(repository, canvas, element, container);
        canvas.addMouseListener(listener);
    }
}

class MyMouseListener implements MouseListener {
    private SVGRepository repository;
    private JSVGCanvas canvas;
    private SVGElement element;
    private IRefreshData container;
    
    MyMouseListener(SVGRepository repository, JSVGCanvas canvas, SVGElement element, IRefreshData container) {
        this.repository = repository;
        this.canvas = canvas;
        this.element = element;
        this.container = container;
    }
    @Override
    public void mouseReleased(MouseEvent me) {
        DActionCopyUnit action = new DActionCopyUnit();
        int offsetX, offsetY;
        offsetX = me.getX() - element.getCenterX();
        offsetY = me.getY() - element.getCenterY();
        if (container.isSnapToGrid()) {
            offsetX = offsetX / SVGConfig.GRID_SIZE * SVGConfig.GRID_SIZE;
            offsetY = offsetY / SVGConfig.GRID_SIZE * SVGConfig.GRID_SIZE;
        }
        action.setOffsetX(offsetX);
        action.setOffsetY(offsetY);
        action.applyAction(element, repository);
        canvas.removeMouseListener(this);
        container.refreshData();
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {}
    @Override
    public void mousePressed(MouseEvent me) {}
    @Override
    public void mouseEntered(MouseEvent me) {}
    @Override
    public void mouseExited(MouseEvent me) {}
}