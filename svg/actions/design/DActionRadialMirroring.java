package svg.actions.design;

import java.util.ArrayList;
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

/**
 * Class to apply radial mirroring to a given element or to the whole canvas
 * @author Ivan Guerrero
 */
public class DActionRadialMirroring extends SVGDAction {
    /** RSYMMETRY (("U"level-element) | "ALL) */
    private static Pattern radialPattern = Pattern.compile("\\s*RSYMMETRY\\s+((U(\\d+)-(\\d+))|ALL).*");
    
    public DActionRadialMirroring() {
        actionName = "RSYMMETRY";
    }
    
    @Override
    public void applyAction(SVGRepository repo) {
        List<SVGElement> list = new ArrayList<>();
        for (SVGElement elem : repo.getElements(0)) {
            list.add(elem);
        }
        
        for (SVGElement elem : list) {
            SVGElement newElem;
            try {
                newElem = (SVGElement)elem.clone();
                newElem.setCenter(SVGConfig.CANVAS_WIDTH - newElem.getCenterX(), SVGConfig.CANVAS_HEIGHT - newElem.getCenterY());
                repo.applyAction(new ActionCreateElement(), newElem, true);
            } catch (CloneNotSupportedException ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
            }
        }
        
        desc = actionName + " ALL";
        repo.addDesignAction(this);
        actionLevel = repo.getCurrentLevel();
        super.setDesignAction(repo);
    }
    
    @Override
    public void applyAction(SVGElement elem, SVGRepository repository) {
        int lastLevel = repository.getCurrentLevel();
        try {
            repository.setCurrentLevel(0);
            SVGElement newElem = (SVGElement)elem.clone();
            for (SVGElement singleElem : newElem.getSimpleElements()) {
                singleElem.setCenter(SVGConfig.CANVAS_WIDTH - singleElem.getCenterX(), SVGConfig.CANVAS_HEIGHT - singleElem.getCenterY());
                repository.applyAction(new ActionCreateElement(), singleElem, true);
            }
            super.element = elem;
            desc = actionName + " " + elem.getID();
            repository.addDesignAction(this);
            actionLevel = repository.getCurrentLevel();
            //Marks the last action as end of design action
            super.setDesignAction(repository);
        } catch (CloneNotSupportedException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        repository.setCurrentLevel(lastLevel);
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
        
        m = radialPattern.matcher(actionStr);
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