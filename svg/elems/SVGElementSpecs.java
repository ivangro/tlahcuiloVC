package svg.elems;

import java.util.*;

/**
 * Class to store the specification to create a new element
 * @author Ivan Guerrero
 */
public class SVGElementSpecs {
    private int ID;
    private String imagePath;
    private String iconPath;
    private int imageHeight;
    private int imageWidth;
    private int imageAlpha;
    private List<Integer> availableSizes;
    
    public SVGElementSpecs() {
        availableSizes = new ArrayList<>();
        imageHeight = 20;
        imageWidth = 40;
        imageAlpha = 100;
        imagePath = "";
        iconPath = "";
        ID = 1;
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
        if (imagePath.startsWith("file:///"))
            this.imagePath = imagePath;
        else
            this.imagePath = "file:///" + imagePath;
    }

    /**
     * @return the imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param imageHeight the imageHeight to set
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @return the imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageWidth the imageWidth to set
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return the imageAlpha
     */
    public int getImageAlpha() {
        return imageAlpha;
    }

    /**
     * @param imageAlpha the imageAlpha to set
     */
    public void setImageAlpha(int imageAlpha) {
        this.imageAlpha = imageAlpha;
    }

    /**
     * @return the availableSizes
     */
    public List<Integer> getAvailableSizes() {
        return availableSizes;
    }

    /**
     * @param availableSizes the availableSizes to set
     */
    public void setAvailableSizes(List<Integer> availableSizes) {
        this.availableSizes = availableSizes;
    }

    /**
     * Adds a size for each number between 1 and the given sizes
     * @param sizes 
     */
    public void setAvailableSizes(int sizes) {
        for (int i=1; i<=sizes; i++) {
            availableSizes.add(i);
        }
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
    
    public int getID() {
        return ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }
    
    @Override
    public String toString() {
        return "Specs " + ID;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SVGElementSpecs) {
            SVGElementSpecs specs = (SVGElementSpecs)obj;
            return specs.ID == ID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.ID;
        return hash;
    }
}