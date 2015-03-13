package svg.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionCreateElement;
import svg.actions.ActionDeleteElement;
import svg.actions.ActionModifyElement;
import svg.actions.ActionMoveElement;
import svg.core.SVGAction;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.ElementFactory;

/**
 * Class to reproduce a drawing from a story saved into a file
 * @author Ivan Guerrero
 */
public class StoryLoader {
    private static Pattern move, add, delete, modify;
    /** Moves an element to the given location     MOVE     element     newX        newY     endOfDesign              */
    private static final String movePattern = "\\s*MOVE\\s*(\\d+)\\s*(-?\\d+)\\s*(-?\\d+)\\s*(%)?.*";
    /** Adds a new element to the group:          ADD    x           y           size       I-imageNumber endOfDesign */
    private static final String addPattern = "\\s*ADD\\s*(-?\\d+)\\s*(-?\\d+)\\s*(\\d+)?\\s*(I-\\d+)?\\s*(%)?.*";
    /** Deletes an element from the canvas           DELETE     element   endOfDesign                                 */
    private static final String deletePattern = "\\s*DELETE\\s*(\\d+)\\s*(%)?.*";
    /** Modifies the size and imageID attributes of an element
     *                                               UPDATE     element  S-size      I-imageID    endOfDesign         */
    private static final String modifyPattern = "\\s*UPDATE\\s*(\\d+)\\s*S-(\\d+)\\s*I-(\\d+)\\s*(%)?.*";
    
    private Scanner scanner;
    private boolean keepDesignActions;
    
    static {
        move = Pattern.compile(movePattern);
        add = Pattern.compile(addPattern);
        delete = Pattern.compile(deletePattern);
        modify = Pattern.compile(modifyPattern);
    }
    
    public StoryLoader() {
    }
    
    public void loadStory(File file, SVGRepository repo) throws FileNotFoundException {
        scanner = new Scanner(file);
        repo.generateSVGDocument(keepDesignActions);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (!nextLine.startsWith("//"))
                parseAction(nextLine, repo);
        }
    }

    public boolean parseAction(String nextLine, SVGRepository repo) {
        Matcher m;
        SVGElement elem;
        SVGAction action;
        
        m = move.matcher(nextLine);
        if (m.matches()) {
            int id, x, y;
            id = Integer.parseInt(m.group(1));
            x = Integer.parseInt(m.group(2));
            y = Integer.parseInt(m.group(3));
            elem = repo.getElementByID(id + "");
            action = new ActionMoveElement();
            ((ActionMoveElement)action).setNewX(x);
            ((ActionMoveElement)action).setNewY(y);
            if (m.group(4) != null)
                action.setEndOfDesignAction();
            repo.applyAction(action, elem, true);
            return true;
        }
        m = add.matcher(nextLine);
        if (m.matches()) {
            int x, y;
            x = Integer.parseInt(m.group(1));
            y = Integer.parseInt(m.group(2));
            int size = (m.group(3) != null) ? Integer.parseInt(m.group(3)) : SVGConfig.DEFAULT_ELEMENT_SIZE;
            int imageID = (m.group(4) != null) ? Integer.parseInt(m.group(4).substring(2)) : SVGConfig.DEFAULT_IMAGE_NUMBER;
            elem = ElementFactory.getInstance().createNewElement(size, imageID);
            elem.setCenter(x, y);
            action = new ActionCreateElement();
            if (m.group(5) != null)
                action.setEndOfDesignAction();
            repo.applyAction(action, elem, true);
            return true;
        }
        m = delete.matcher(nextLine);
        if (m.matches()) {
            String id;
            id = m.group(1);
            elem = repo.getElementByID(id);
            action = new ActionDeleteElement();
            if (m.group(2) != null)
                action.setEndOfDesignAction();
            repo.applyAction(action, elem, true);
            return true;
        }
        m = modify.matcher(nextLine);
        if (m.matches()) {
            String id;
            id = m.group(1);
            elem = repo.getElementByID(id + "");
            int size = (m.group(2) != null) ? Integer.parseInt(m.group(2)) : SVGConfig.DEFAULT_ELEMENT_SIZE;
            int imageID = (m.group(3) != null) ? Integer.parseInt(m.group(3)) : SVGConfig.DEFAULT_IMAGE_NUMBER;
            action = new ActionModifyElement();
            ((ActionModifyElement)action).setNewImageID(imageID);
            ((ActionModifyElement)action).setNewSize(size);
            if (m.group(4) != null)
                action.setEndOfDesignAction();
            repo.applyAction(action, elem, true);
            return true;
        }
        
        return false;
    }

    /**
     * @return the keepDesignActions
     */
    public boolean isKeepDesignActions() {
        return keepDesignActions;
    }

    /**
     * @param keepDesignActions the keepDesignActions to set
     */
    public void setKeepDesignActions(boolean keepDesignActions) {
        this.keepDesignActions = keepDesignActions;
    }
}