package config;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import svg.core.SVGConfig;
import static svg.core.SVGConfig.*;
import svg.elems.GraphicElement;

/**
 *
 * @author Ivan Guerrero
 */
public class Configuration {
    //private static String configFile = "config/config.cfg";
    private static String logFile = "svgDrawing.log";
    private static Configuration instance = new Configuration();
    
    private Configuration() {}
    
    public static Configuration getInstance() {
        return instance;
    }
    
    public void loadConfig() {
        try {
            loadClassPath();
            configLogger();
            
            //File file = new File(Configuration.class.getClassLoader().getResource(configFile).toURI());
            File file = new File(CONFIG_FILE);
            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);
            CANVAS_HEIGHT = Integer.parseInt(buffer.readLine());
            CANVAS_WIDTH = Integer.parseInt(buffer.readLine());
            FORE_COLOR = buffer.readLine();
            BACKGROUND_COLOR = buffer.readLine();
            //GRAPHIC_ELEMENT = GraphicElement.valueOf(buffer.readLine());
            GraphicElement.valueOf(buffer.readLine());
            defaultFontType = buffer.readLine();
            defaultFontSize = Integer.parseInt(buffer.readLine());
            FONT_WIDTH_FACTOR = Double.parseDouble(buffer.readLine());
            FONT_HEIGHT_FACTOR = Double.parseDouble(buffer.readLine());
            //imagePath = buffer.readLine();
            buffer.readLine();
            //IMAGE_HEIGHT = Integer.parseInt(buffer.readLine());
            Integer.parseInt(buffer.readLine());
            //IMAGE_WIDTH = Integer.parseInt(buffer.readLine());
            Integer.parseInt(buffer.readLine());
            MAX_UNIT_DISTANCE = Integer.parseInt(buffer.readLine());
            DISTANCE_OFFSET = Double.parseDouble(buffer.readLine());
            MAX_SYMMETRY_OFFSET = Integer.parseInt(buffer.readLine());
            MIN_EMPLOYED_AREA = Double.parseDouble(buffer.readLine());
            MAX_EMPLOYED_AREA = Double.parseDouble(buffer.readLine());
            MIN_NUMBER_OF_RHYTHMS = Integer.parseInt(buffer.readLine());
            MAX_NUMBER_OF_RHYTHMS = Integer.parseInt(buffer.readLine());
            MAX_CYCLES = Integer.parseInt(buffer.readLine());
            ENGAGEMENT_STEPS = Integer.parseInt(buffer.readLine());
            REFLECTION_STEPS = Integer.parseInt(buffer.readLine());
            
            buffer.close();
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveConfig() {
        try {
            //File file = new File(Configuration.class.getClassLoader().getResource(configFile).toURI());
            File file = new File(CONFIG_FILE);
            FileWriter writer = new FileWriter(file);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(CANVAS_HEIGHT+"\n");
            buffer.write(CANVAS_WIDTH+"\n");
            buffer.write(FORE_COLOR+"\n");
            buffer.write(BACKGROUND_COLOR+"\n");
            buffer.write(GRAPHIC_ELEMENT.name() + "\n");
            buffer.write(defaultFontType + "\n");
            buffer.write(defaultFontSize+"\n");
            buffer.write(FONT_WIDTH_FACTOR+"\n");
            buffer.write(FONT_HEIGHT_FACTOR+"\n");
            buffer.write(imagePath+"\n");
            buffer.write(IMAGE_HEIGHT+"\n");
            buffer.write(IMAGE_WIDTH+"\n");
            buffer.write(MAX_UNIT_DISTANCE+"\n");
            buffer.write(DISTANCE_OFFSET+"\n");
            buffer.write(MAX_SYMMETRY_OFFSET+"\n");
            buffer.write(MIN_EMPLOYED_AREA+"\n");
            buffer.write(MAX_EMPLOYED_AREA+"\n");
            buffer.write(MIN_NUMBER_OF_RHYTHMS+"\n");
            buffer.write(MAX_NUMBER_OF_RHYTHMS+"\n");
            buffer.write(MAX_CYCLES + "\n");
            buffer.write(ENGAGEMENT_STEPS + "\n");
            buffer.write(REFLECTION_STEPS + "\n");
            
            buffer.close();
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }

    private void configLogger() {
        try {
            FileHandler handler = new FileHandler(logFile, false);
            handler.setFormatter(new XMLFormatter());
            handler.setLevel(Level.INFO);
            Logger.getGlobal().addHandler(handler);
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadClassPath() throws IOException {
        String name = this.getClass().getName().replace('.', '/');
        String s = this.getClass().getResource("/" + name + ".class").toString();
        if (s.startsWith("jar:")) {
            s = s.replace('/', File.separatorChar);
            s = s.substring(0, s.indexOf(".jar")+4);
            if (System.getProperty("os.name").indexOf("win") >= 0)
                s = s.substring(s.lastIndexOf(':')-1);//In windows -1
            else
                s = s.substring(s.lastIndexOf(':')+1);//In linux +1
            s = s.substring(0, s.indexOf("ERDrawingComposer.jar"));
            SVGConfig.setMainPath(s + "src/");
        }
        else {
            File f = new File(".");
            String path = f.getCanonicalPath().replaceAll("\\\\", "/");
            SVGConfig.setMainPath(path + "/src/");
        }
    }
}