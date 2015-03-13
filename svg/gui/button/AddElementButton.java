package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import svg.actions.ActionCreateElement;
import svg.core.SVGAction;
import svg.core.SVGElement;
import svg.elems.ElementFactory;
import svg.elems.SVGElementSpecs;

/**
 * Button to generate a new SVGElement with a Q letter
 * @author Ivan Guerrero
 */
public class AddElementButton extends JToggleButton implements CanvasListener {
    private String iconPath, imagePath;
    private int size, iconHeight, iconWidth, imageID;
    private int coordX, coordY;
    
    public AddElementButton() {
        iconPath = "/images/qIcon.png";
        super.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        size = -1;
    }
    
    public AddElementButton(SVGElementSpecs specs) {
        this(specs.getID(), specs.getImageHeight(), specs.getImageWidth(), specs.getImagePath(), specs.getIconPath());
    }
    
    public AddElementButton(int imageID, int iconHeight, int iconWidth, String imagePath, String iconPath) {
        this.imageID = imageID;
        this.iconHeight = iconHeight;
        this.iconWidth = iconWidth;
        this.imagePath = imagePath;
        this.iconPath = iconPath;
    }

    /**
     * @return the elem
     */
    @Override
    public SVGElement getElem() {
        SVGElement elem = ElementFactory.getInstance().createNewElement();
        if (size > 0)
            elem.setSize(size);
        if (coordX > 0 && coordY > 0) {
            elem.setCenter(coordX, coordY);
            elem.setUserAdded(true);
        }
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
        this.setSize(size);
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the iconHeight
     */
    public int getIconHeight() {
        return iconHeight;
    }

    /**
     * @param iconHeight the iconHeight to set
     */
    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    /**
     * @return the iconWidth
     */
    public int getIconWidth() {
        return iconWidth;
    }

    /**
     * @param iconWidth the iconWidth to set
     */
    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    /**
     * @return the imageID
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * @param imageID the imageID to set
     */
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    /**
     * @return the coordX
     */
    public int getCoordX() {
        return coordX;
    }

    /**
     * @param coordX the coordX to set
     */
    @Override
    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    /**
     * @return the coordY
     */
    public int getCoordY() {
        return coordY;
    }

    /**
     * @param coordY the coordY to set
     */
    @Override
    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }
    
    @Override
    public SVGAction getSVGAction() {
        return new ActionCreateElement();
    }

    @Override
    public void setElem(SVGElement elem) {
    }
}