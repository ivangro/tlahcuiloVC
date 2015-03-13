package svg.actions.design;

import ivangro.utils.SortedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionCreateElement;
import svg.core.SVGConfig;
import svg.core.SVGDAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.utils.SVGUnitComparator;
import svg.elems.SVGUnit;

/**
 * Method to apply horizontal mirroring symmetry to the determined elements in the canvas
 * @author Ivan Guerrero
 */
public class DActionHMirroring extends SVGDAction {
    private int centerH;
    
    /** HMIRROR (("U"level-element) | "ALL") */
    private static Pattern hMirrorPattern = Pattern.compile("\\s*HMIRROR\\s+((U(\\d+)-(\\d+))|ALL).*");
    
    public DActionHMirroring() {
        centerH = SVGConfig.CANVAS_HEIGHT / 2;
        actionName = "HMIRROR";
    }
    
    @Override
    public void applyAction(SVGRepository repo) {
        List<SVGElement> upper, lower, middle;
        upper = new SortedList<>(new SVGUnitComparator(true));
        lower = new SortedList<>(new SVGUnitComparator(true));
        middle = new SortedList<>(new SVGUnitComparator(true));
        
        //Split the units in upper and lower units
        for (SVGElement unit : repo.getElements(1)) {
            if (unit.getCenterY() > centerH + SVGConfig.MAX_SYMMETRY_OFFSET)
                lower.add(unit);
            else if (unit.getCenterY() < centerH - SVGConfig.MAX_SYMMETRY_OFFSET)
                upper.add(unit);
            else
                middle.add(unit);
        }
        
        for (SVGElement unit : lower) {
            for (SVGElement elem : ((SVGUnit)unit).getElements()) {
                SVGElement newElem;
                try {
                    newElem = (SVGElement)elem.clone();
                    newElem.setCenter(newElem.getCenterX(), SVGConfig.CANVAS_HEIGHT - newElem.getCenterY());
                    repo.applyAction(new ActionCreateElement(), newElem, true);
                } catch (CloneNotSupportedException ex) {
                    Logger.getGlobal().log(Level.SEVERE, null, ex);
                }
            }
        }
        
        for (SVGElement unit : upper) {
            for (SVGElement elem : ((SVGUnit)unit).getElements()) {
                SVGElement newElem;
                try {
                    newElem = (SVGElement)elem.clone();
                    newElem.setCenter(newElem.getCenterX(), SVGConfig.CANVAS_HEIGHT - newElem.getCenterY());
                    repo.applyAction(new ActionCreateElement(), newElem, true);
                } catch (CloneNotSupportedException ex) {
                    Logger.getGlobal().log(Level.SEVERE, null, ex);
                }
            }
        }
        
        desc = actionName + " ALL";
        repo.addDesignAction(this);
        actionLevel = repo.getCurrentLevel();
        //Marks the last action as end of design action
        super.setDesignAction(repo);
    }
    
    @Override
    public void applyAction(SVGElement elem, SVGRepository repository) {
        try {
            SVGElement newElem = (SVGElement)elem.clone();
            for (SVGElement singleElem : newElem.getSimpleElements()) {
                singleElem.setCenter(singleElem.getCenterX(), SVGConfig.CANVAS_HEIGHT - singleElem.getCenterY());
            }
            repository.applyAction(new ActionCreateElement(), newElem, true);
            super.element = elem;
            desc = actionName + " " + elem.getID();
            actionLevel = repository.getCurrentLevel();
            repository.addDesignAction(this);
            //Marks the last action as end of design action
            super.setDesignAction(repository);
        } catch (CloneNotSupportedException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean parseAction(String actionStr, SVGRepository repository) {
        return parseAction(actionStr, repository, true);
    }
    
    @Override
    public boolean parseAction(String actionStr, SVGRepository repository, boolean execute) {
        Matcher m;
        int noElement = -1, level = -1;
        String appliesToAll;
        
        m = hMirrorPattern.matcher(actionStr);
        if (m.matches()) {
            appliesToAll = m.group(1);
            if (appliesToAll != null && appliesToAll.equals("ALL")) {
                executeAction(execute, repository);
                return true;
            }
            
            if (m.group(3) != null) {
                level = Integer.parseInt(m.group(3));
                actionLevel = level;
            }
            if (m.group(4) != null)
                noElement = Integer.parseInt(m.group(4));
            if (level >= 0 && noElement >= 0 && repository.getElements(level).size() > noElement) {
                executeAction(level, noElement, execute, repository);
                return true;
            }
        }
        
        return false;
    }
}