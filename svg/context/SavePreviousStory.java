package svg.context;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGConfig;
import svg.core.SVGRepository;
import svg.story.FileManager;
import svg.story.LevelCurveManager;

/**
 *
 * @author Ivan Guerrero
 */
public class SavePreviousStory {
    private static SavePreviousStory instance = new SavePreviousStory();
    
    private SavePreviousStory() {}
    
    public static SavePreviousStory getInstance() {
        return instance;
    }
    
    /**
     * Saves the current drawing as an inspiring story
     * @param repository 
     */
    public void saveDrawing(SVGRepository repository) {
        try {
            File file = new File(SVGConfig.INSPIRING_STORIES_PATH);
            if (!file.exists())
                file.mkdir();
            File[] stories = file.listFiles(new FilenameFilter() {
                                  @Override
                                  public boolean accept(File dir, String name)  {
                                      return name.endsWith(".str");
                                  }
                               });
            int maxIndex = 0;
            for (File story : stories) {
                String name = story.getName();
                name = name.replace("inspiringStory", "");
                name = name.replace(".str", "");
                int index = Integer.parseInt(name);
                if (index > maxIndex)
                    maxIndex = index;
            }
            maxIndex++;
            file = new File(SVGConfig.INSPIRING_STORIES_PATH + "/inspiringStory" + maxIndex + ".str");
            FileManager.saveStory(file, repository);
            file = new File(SVGConfig.INSPIRING_STORIES_PATH + "/inspiringStory" + maxIndex + ".des");
            FileManager.saveDesign(file, repository);
            saveLevelCurve(repository, maxIndex);
        } catch (IOException fe) {
            Logger.getGlobal().log(Level.WARNING, "Error saving an inspiring story {0}", fe);
        }
    }

    private void saveLevelCurve(SVGRepository repository, int maxIndex) throws IOException {
        File file = new File(SVGConfig.LEVEL_CURVE_FILE);
        if (!file.exists())
            file.createNewFile();
        List<Integer> levelCurve = LevelCurveManager.calculateLevelCurve(repository);
        FileWriter writer = new FileWriter(file, true);
        String curve = maxIndex + ": ";
        for (Integer i : levelCurve) {
            curve += i + " ";
        }
        writer.append(curve.trim() + "\n");
        writer.close();
    }
}