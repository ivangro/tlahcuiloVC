package svg.core;

import svg.elems.ElementShape;
import svg.elems.GraphicElement;

/**
 * Configuration parameters for the app.<br>
 * IMPORTANT: Every file path should be added to the 'set main path' method to fix its absolute path.
 * @author Ivan Guerrero
 */
public class SVGConfig {
    /** File where the configuration parameters are stored */
    public static String CONFIG_FILE = "config/config.cfg";
    /** Directory where the basic images are stored */
    public static String BASIC_ELEMS_PATH = "blocks/carpet/";
    /** File where the shapes are stored */
    public static String SHAPE_STORE_FILE = "config/shapes.dat";
    /** Directory where the inspiring stories are stored */
    public static String INSPIRING_STORIES_PATH = "inspiringSet";
    /** File where the level curve values for each inspiring story are stored */
    public static String LEVEL_CURVE_FILE = "config/levelCurve.dat";
    
    public static String BASE_PATH = "";
    /** Determines the width of the drawing area */
    public static int CANVAS_WIDTH = 800;
    /** Determines the height of the drawing area */
    public static int CANVAS_HEIGHT = 550;
    /** Colors employed */
    public static String FORE_COLOR = "#CC0000";
    public static String SELECTION_COLOR = "#0000FF";
    public static String BACKGROUND_COLOR = "#FFE473";
    
    /** Elements for the text simple element */
        /** Determines the font type to be employed */
        public static String defaultFontType = "Forte";
        public static int defaultFontSize = 40;
        public static double FONT_WIDTH_FACTOR = 0.7393;
        public static double FONT_HEIGHT_FACTOR = 0.8784;
    /** Elements for the image simple element */
        public static String imagePath = "file:///E:/Ivan/Proyectos/SVGDrawing/src/svg/images/sydney.png";
        public static int IMAGE_HEIGHT = 43;
        public static int IMAGE_WIDTH = 36;
        public static double IMAGE_OPACITY = 1;
        /** Determines the image being employed */
        public static int CURRENT_IMAGE_ID;
        
    /** Determines when two objects are considered a unit */
    public static int MAX_UNIT_DISTANCE = 100;
    /** Offset allowed for a couple of elements to be considered a unit */
    public static double DISTANCE_OFFSET = 20;
    /** Offset allowed to determine if two units are symmetric */
    public static int MAX_SYMMETRY_OFFSET = 5;
    /** Offset allowed to determine if two units are balanced */
    public static double MAX_BALANCE_OFFSET = 10;
    
    /** File where the atoms generated are stored */
    public static String AtomsFile = "atoms.dat";
    /** Determines when a similarity between a context and an atom is enough to employ the atom data */
    public static double MIN_SIMILARITY_FOR_LEVEL = 60;
    /** Determines if the unit relations with itself are to be considered or not */
    public static boolean EMPLOY_SELF_RELATIONS_IN_CONTEXT = true;
    /** Parameters for reflection detection filter to stop drawing depending on the employed area */
    public static double MIN_EMPLOYED_AREA = 5;
    public static double MAX_EMPLOYED_AREA = 25;
    /** Parameters for reflection detection filter to stop drawing depending on the number of rhythms detected */
    public static int MIN_NUMBER_OF_RHYTHMS = 5;
    public static int MAX_NUMBER_OF_RHYTHMS = 15;
    /** Parameters for E-R cycle */
    public static int MAX_CYCLES = 3;
    public static int ENGAGEMENT_STEPS = 3;
    public static int REFLECTION_STEPS = 2;
    /** Minimum pattern value to be included */
    public static double MIN_PATTERN_DISTANCE = 0.5;
    public static GraphicElement GRAPHIC_ELEMENT = GraphicElement.Image;
    /** Default size for a simple element */
    public static int DEFAULT_ELEMENT_SIZE = 2;
    /** Default image number */
    public static int DEFAULT_IMAGE_NUMBER = 1;
    /** Maximum number of pixels to determine if two units share the same line */
    public static int INLINE_OFFSET = 20;
    /** Size of the grid */
    public static int GRID_SIZE = 30;
    
    /**
     * Employed to detect vertical lines. 
     * When the standard deviation of a set of points is greater than this value, the shape detected is a vertical line.
     */
    public static double VERTICAL_SHAPE_DEVIATION = 5;
    /** Employed to enable/disable the level prediction during engagement mode. */
    public static boolean LEVEL_PREDICTION_ENABLED = true;
    /** Employed to determine the coordinate priority: If VERTICAL, the priority is the Y coordinate, otherwise is the X coordinate */
    public static ElementShape ELEMENT_SORT_TYPE = ElementShape.HORIZONTAL;

    public static void setMainPath(String path) {
        BASE_PATH = path;
        BASIC_ELEMS_PATH = path + BASIC_ELEMS_PATH;
        SHAPE_STORE_FILE = path + SHAPE_STORE_FILE;
        INSPIRING_STORIES_PATH = path + INSPIRING_STORIES_PATH;
        LEVEL_CURVE_FILE = path + LEVEL_CURVE_FILE;
        AtomsFile = path + AtomsFile;
        CONFIG_FILE = path + CONFIG_FILE;
    }
}