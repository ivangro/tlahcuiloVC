package svg.gui.button;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.actions.ActionDeleteElement;
import svg.core.SVGAction;

/**
 * Class to implement the deletion of a SVG element
 * @author Ivan Guerrero
 */
public class RemoveButton extends JButton {
    private String iconPath;
    
    public RemoveButton() {
        iconPath = "/images/trashbin.png";
        setIcon(new ImageIcon(getClass().getResource(iconPath)));
    }
    
    public RemoveButton(Action action) {
        super(action);
        iconPath = "/images/trashbin.png";
        setIcon(new ImageIcon(getClass().getResource(iconPath)));
    }
    
    public SVGAction getSVGAction() {
        return new ActionDeleteElement();
    }

    /**
     * @return the iconPath
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * @param iconPath the iconPath to set
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}