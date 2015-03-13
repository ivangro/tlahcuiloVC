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
 * Method to apply vertical mirroring symmetry to the determined elements in the canvas
 * @author Ivan Guerrero
 */
public class DActionVMirroring extends SVGDAction {
    private int centerV;
    /** VMIRROR (("U"level-element) | "ALL")*/
    private static Pattern vMirrorPattern = Pattern.compile("\\s*VMIRROR\\s+((U(\\d+)-(\\d+))|ALL).*");
    
    public DActionVMirroring() {
        centerV = SVGConfig.CANVAS_WIDTH / 2;
        actionName = "VMIRROR";
    }
    
    @Override
    public void applyAction(SVGRepository repo) {
        List<SVGElement> left, right, middle;
        left = new SortedList<>(new SVGUnitComparator(true));
        right = new SortedList<>(new SVGUnitComparator(true));
        middle = new SortedList<>(new SVGUnitComparator(true));
        
        //Split the units in upper and lower units
        for (SVGElement unit : repo.getElements(1)) {
            if (unit.getCenterX() > centerV + SVGConfig.MAX_SYMMETRY_OFFSET)
                right.add(unit);
            else if (unit.getCenterX() < centerV - SVGConfig.MAX_SYMMETRY_OFFSET)
                left.add(unit);
            else
                middle.add(unit);
        }
        
        for (SVGElement unit : right) {
            for (SVGElement elem : ((SVGUnit)unit).getElements()) {
                SVGElement newElem;
                try {
                    newElem = (SVGElement)elem.clone();
                    newElem.setCenter(SVGConfig.CANVAS_WIDTH - newElem.getCenterX(), newElem.getCenterY());
                    repo.applyAction(new ActionCreateElement(), newElem, true);
                } catch (CloneNotSupportedException ex) {
                    Logger.getGlobal().log(Level.SEVERE, null, ex);
                }
            }
        }
        
        for (SVGElement unit : left) {
            for (SVGElement elem : ((SVGUnit)unit).getElements()) {
                SVGElement newElem;
                try {
                    newElem = (SVGElement)elem.clone();
                    newElem.setCenter(SVGConfig.CANVAS_WIDTH - newElem.getCenterX(), newElem.getCenterY());
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
                singleElem.setCenter(SVGConfig.CANVAS_WIDTH - singleElem.getCenterX(), singleElem.getCenterY());
            }
            repository.applyAction(new ActionCreateElement(), newElem, true);
            super.element = elem;
            desc = actionName + " " + elem.getID();
            repository.addDesignAction(this);
            actionLevel = repository.getCurrentLevel();
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
        
        m = vMirrorPattern.matcher(actionStr);
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
            if (noElement >= 0 && level >= 0 && repository.getElements(level).size() > noElement) {
                executeAction(level, noElement, execute, repository);
                return true;
            }
        }
        
        return false;
    }
}