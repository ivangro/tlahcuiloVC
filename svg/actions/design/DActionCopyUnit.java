package svg.actions.design;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionCreateElement;
import svg.core.SVGDAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 *
 * @author Ivan Guerrero
 */
public class DActionCopyUnit extends SVGDAction {
    private int offsetX, offsetY;
    
    /** COPY "U"level-element offsetX offsetY */
    private static Pattern copyUnitPattern = Pattern.compile("\\s*COPY\\s+U(\\d+)-(\\d+)\\s+(-?\\d+)\\s+(-?\\d+).*");
    
    public DActionCopyUnit() {
        actionName = "COPY";
    }
    
    @Override
    public void applyAction(SVGRepository repository) {
        //TODO: Implement or prevent to be added
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void applyAction(SVGElement element, SVGRepository repository) {
        try {
            SVGElement newElement = (SVGElement)element.clone();
            newElement.setCenter(element.getCenterX() + getOffsetX(), element.getCenterY() + getOffsetY());
            repository.applyAction(new ActionCreateElement(), newElement);
            actionLevel = repository.getCurrentLevel();
            
            super.element = element;
            repository.addDesignAction(this);
            String id = (element.getID().startsWith("U")) ? element.getID() : "U0-" + element.getID();
            desc = actionName + " " + id + " " + getOffsetX() + " " + getOffsetY();
            //Mark the last action as an end of design action
            super.setDesignAction(repository);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DActionCopyUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean parseAction(String actionStr, SVGRepository repository) {
        Matcher m;
        int noElement, level, lastLevel;
        m = copyUnitPattern.matcher(actionStr);
        if (m.matches()) {
            lastLevel = repository.getCurrentLevel();
            level = Integer.parseInt(m.group(1));
            noElement = Integer.parseInt(m.group(2));
            if (repository.getElements(level).size() > noElement) {
                setOffsetX(Integer.parseInt(m.group(3)));
                setOffsetY(Integer.parseInt(m.group(4)));
                SVGElement elem = repository.getElements(level).get(noElement);
                repository.setCurrentLevel(level);
                applyAction(elem, repository);
                repository.setCurrentLevel(lastLevel);
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean parseAction(String actionStr, SVGRepository repository, boolean execute) {
        if (execute)
            return parseAction(actionStr, repository);
        
        Matcher m;
        int noElement, level;
        m = copyUnitPattern.matcher(actionStr);
        if (m.matches()) {
            level = Integer.parseInt(m.group(1));
            actionLevel = level;
            noElement = Integer.parseInt(m.group(2));
            if (repository.getElements(level).size() > noElement) {
                setOffsetX(Integer.parseInt(m.group(3)));
                setOffsetY(Integer.parseInt(m.group(4)));
                SVGElement elem = repository.getElements(level).get(noElement);
                super.element = elem;
                repository.addDesignAction(this);
                desc = actionName + " " + element.getID() + " " + getOffsetX() + " " + getOffsetY();
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        setOffsetX(Integer.parseInt(properties.getProperty("offsetX")));
        setOffsetY(Integer.parseInt(properties.getProperty("offsetY")));
    }

    /**
     * @return the offsetX
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * @param offsetX the offsetX to set
     */
    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        properties.setProperty("offsetX", getOffsetX() + "");
    }

    /**
     * @return the offsetY
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * @param offsetY the offsetY to set
     */
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        properties.setProperty("offsetY", getOffsetY() + "");
    }
}