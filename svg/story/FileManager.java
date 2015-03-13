package svg.story;

import java.io.*;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import svg.core.SVGAction;
import svg.core.SVGRepository;

/**
 * Class to save a drawing or the story of how the drawing was performed in a file
 * @author Ivan Guerrero
 */
public class FileManager {
    public static void saveFile(File file, SVGRepository repository) 
            throws FileNotFoundException, SVGGraphics2DIOException, IOException {
        OutputStream out = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(out);
        SVGGraphics2D generator = new SVGGraphics2D(repository.getDocument());
        generator.stream(repository.getDocument().getDocumentElement(), writer);
        writer.close();
    }
    
     public static void saveStory(File file, SVGRepository repository) throws FileNotFoundException, IOException {
        OutputStream out = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(out);
        for (SVGAction action : repository.getActions()) {
            writer.write(action.getActionForStory() + "\n");
        }
        writer.close();
    }

    public static void saveDesign(File file, SVGRepository repository) throws FileNotFoundException, IOException {
        OutputStream out = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(out);
        writer.write(repository.getDesignActionDescriptions());
        writer.close();
    }

    public static void loadStory(File file, SVGRepository repository, boolean keepDesignActions) throws FileNotFoundException {
        StoryLoader loader = new StoryLoader();
        loader.setKeepDesignActions(keepDesignActions);
        loader.loadStory(file, repository);
    }
    
    public static void loadDesign(File file, SVGRepository repository, boolean execute) throws FileNotFoundException {
        DesignLoader loader = new DesignLoader();
        loader.loadStory(file, repository, execute);
    }
}