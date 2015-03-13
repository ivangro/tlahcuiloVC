package svg.gui.button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import svg.actions.ActionMoveElement;
import svg.core.SVGAction;
import svg.core.SVGElement;
import svg.gui.ICanvasListener;

/**
 * Button to perform the move action for a SVG element
 * @author Ivan Guerrero
 */
public class MoveButton extends JToggleButton implements CanvasListener {
    private int coordX, coordY;
    private SVGElement elem;
    private ICanvasListener mainFrame;
    
    public MoveButton() {
        super.setIcon(new ImageIcon(getClass().getResource("/images/moveIcon.png")));
    }
    
    public MoveButton(ICanvasListener mainFrame) {
        super.setIcon(new ImageIcon(getClass().getResource("/images/moveIcon.png")));
        this.mainFrame = mainFrame;
        addListeners();
    }
    
    @Override
    public SVGAction getSVGAction() {
        ActionMoveElement action = new ActionMoveElement();
        action.setNewX(coordX);
        action.setNewY(coordY);
        elem.setUserAdded(false);
        return action;
    }

    @Override
    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    @Override
    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    @Override
    public SVGElement getElem() {
        return elem;
    }
    
    @Override
    public void setElem(SVGElement elem) {
        this.elem = elem;
    }

    private void addListeners() {
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                bttnMoveActionPerformed();
            }
        });
    }
    
    private void bttnMoveActionPerformed() {
        mainFrame.adminListener(this);
    }
}
