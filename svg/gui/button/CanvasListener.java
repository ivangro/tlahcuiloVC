package svg.gui.button;

import svg.core.SVGAction;
import svg.core.SVGElement;

/**
 *
 * @author Ivan Guerrero
 */
public interface CanvasListener {
    public SVGAction getSVGAction();
    public void setCoordX(int coordX);
    public void setCoordY(int coordY);
    public boolean isSelected();
    public SVGElement getElem();
    public void setElem(SVGElement elem);
}
