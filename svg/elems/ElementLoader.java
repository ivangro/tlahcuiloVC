package svg.elems;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Class to load the basic elements and store them at the ElementFactory
 * @author Ivan Guerrero
 */
public class ElementLoader {
    public ElementLoader() {
        
    }
    
    public void loadElements(File basicElementsDir) {
        loadElements(basicElementsDir, null);
    }
    
    /**
     * Loads the basic elements available at the given directory.
     * If no filename is given, all the .png files are analyzed
     * @param basicElementsDir The directory to look for basic elements
     * @param filename The file name of the basic elements
     */
    public void loadElements(File basicElementsDir, final String filename) {
        File[] files = basicElementsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                boolean extension = string.endsWith(".jpg") || string.endsWith(".png");
                //boolean background = !string.startsWith("background");
                String myName = (string.indexOf(".") > 0) ? string.substring(0, string.indexOf(".")) : string;
                boolean name = (filename == null || (string.contains(filename) && !myName.equalsIgnoreCase(filename)));
                
                boolean res = extension && name;
                //Logger.getGlobal().log(Level.INFO, "Image analized {0}: {1} {2} {3} {4}", new Object[]{string, extension, name, filename, myName});
                return res;
            }
        });
        
        int i=0;
        ElementFactory factory = ElementFactory.getInstance();
        
        for (File file : files) {
            try {
                BufferedImage img = ImageIO.read(file);
                SVGElementSpecs specs = new SVGElementSpecs();
                specs.setAvailableSizes(3);
                specs.setID(i);
                i++;
                specs.setIconPath(file.getAbsolutePath());
                specs.setImageAlpha(100);
                specs.setImageHeight(img.getHeight());
                specs.setImagePath(file.getAbsolutePath());
                specs.setImageWidth(img.getWidth());
                String name = file.getName();
                if (name.contains("focalPoint")) {
                    specs.setID(100);
                    i--;
                    factory.setFocalPointSpecs(specs);
                    Logger.getGlobal().log(Level.INFO, "Focal point image detected");
                }
                else if (name.contains("background")) {
                    specs.setID(200);
                    i--;
                    factory.setBackgroundSpecs(specs);
                    Logger.getGlobal().log(Level.INFO, "Background image detected");
                }
                else {
                    factory.addSVGElementSpecs(specs);
                }
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }
        Logger.getGlobal().log(Level.INFO, "Image specs added: {0}", factory.getSVGElementSpecs().size());
    }
}
