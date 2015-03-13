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
 * Method to apply horizontal translational symmetry to the determined elements in the canvas
 * @author Ivan Guerrero
 */
public class DActionHSymmetry extends SVGDAction {
    private int centerH;
    /** HSYMMETRY (("U"level-element) | "ALL") */
    private static Pattern hSymmetryPattern = Pattern.compile("\\s*HSYMMETRY\\s+((U(\\d+)-(\\d+))|ALL).*");
    
    public DActionHSymmetry() {
        centerH = SVGConfig.CANVAS_HEIGHT / 2;
        actionName = "HSYMMETRY";
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
                    newElem.setCenter(newElem.getCenterX(), newElem.getCenterY() - centerH);
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
                    newElem.setCenter(newElem.getCenterX(), newElem.getCenterY() + centerH);
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
            int offset = (newElem.getCenterY() > centerH + SVGConfig.MAX_SYMMETRY_OFFSET) ? -centerH : centerH;
            newElem.setCenter(newElem.getCenterX(), newElem.getCenterY() + offset);
            repository.applyAction(new ActionCreateElement(), newElem, true);
            
            super.element = elem;
            desc = actionName + " " + elem.getID();
            repository.addDesignAction(this);
            actionLevel = repository.getCurrentLevel();
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
        
        m = hSymmetryPattern.matcher(actionStr);
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
                noElement = Integer.parseInt(m.group(2));
            if (level >= 0 && noElement >= 0 && repository.getElements(level).size() > noElement) {
                executeAction(level, noElement, execute, repository);
                return true;
            }
        }
        
        return false;
    }
}