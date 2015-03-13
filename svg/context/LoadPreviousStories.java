package svg.context;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGConfig;
import svg.core.SVGRepository;
import svg.story.FileManager;

/**
 * Class to load the previous stories stored at the stories directory
 * @author Ivan Guerrero
 */
public class LoadPreviousStories {
    private static LoadPreviousStories instance = new LoadPreviousStories();
    
    private List<SVGRepository> previousStories;
    
    private LoadPreviousStories() {
        previousStories = new ArrayList<>();
        loadStories();
    }
    
    public static LoadPreviousStories getInstance() {
        return instance;
    }
    
    private void loadStories() {
        if (previousStories.size() > 0)
            return;
        
        File storiesDir = new File(SVGConfig.INSPIRING_STORIES_PATH);
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".str");
            }
        };
        File[] files = storiesDir.listFiles(fileFilter);
        for (File file : files) {
            try {
                SVGRepository repository = SVGRepository.getNewInstance();
                repository.generateSVGDocument();
                FileManager.loadStory(file, repository, true);
                File designFile = new File(file.getParentFile(), file.getName().replace(".str", ".des"));
                FileManager.loadDesign(designFile, repository, false);
                repository.runDetectors();
                getPreviousStories().add(repository);
                Logger.getGlobal().log(Level.INFO, "Previous story {0} analyzed", designFile.getName());
            } catch (FileNotFoundException ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the previousStories
     */
    public List<SVGRepository> getPreviousStories() {
        return previousStories;
    }
}