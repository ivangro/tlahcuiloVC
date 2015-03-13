package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import svg.elems.ElementFactory;
import svg.elems.ElementImage;

/**
 *
 * @author Ivan Guerrero
 */
public class ImageButton extends JToggleButton {
    private String iconPath;
    private int size;
    
    public ImageButton() {
        iconPath = "/images/imageIcon.png";
        super.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        size = -1;
    }
    
    public ElementImage getElem() {
        ElementImage elem = (ElementImage)ElementFactory.getInstance().createNewElement();
        if (size > 0)
            elem.setSize(size);

        return elem;
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
    
    public final void setQSize(int size) {
        this.size = size;
    }
}
