package svg.reflection;

import java.awt.event.ActionEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.CreateUnitAction;
import svg.reflection.detector.*;

/**
 * Class tom implement a reflection action to the drawing
 * @author Ivan Guerrero
 */
public class ReflectionAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData container;
    private boolean reflectionStep;
    private List<IReflectionDetector> detectors;
    
    public ReflectionAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
        detectors = new ArrayList<>();
        //detectors.add(new CollisionDetector());
        detectors.add(new OutOfBoundsDetector());
        detectors.add(new DiversityDetector());
        detectors.add(new ElementAlignementDetector());
        detectors.add(new ElementDistributionDetector());
        detectors.add(new FocalPointDetector());
        detectors.add(new DensityDetector());
        detectors.add(new SymmetryDetector());
        detectors.add(new RhythmDetector());
        detectors.add(new BalanceDetector());
        //detectors.add(new UnboundElementDetector());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        reflectionStep = false;
        
        for (IReflectionDetector detector : detectors) {
            reflectionStep |= detector.execute(repository);
        }
        
        //If the action is performed from the GUI, refresh its data, otherwise update the repository
        if (container != null)
            container.refreshData();
        else
            repository.runDetectors();
    }

    public void breakImpasse() {
        CreateUnitAction action = new CreateUnitAction(repository, container);
        action.actionPerformed(null);
        if (container != null)
            container.refreshData();
        else
            repository.runDetectors();
        Logger.getGlobal().log(Level.INFO, "Impasse broken");
    }

    public boolean isReflectionStep() {
        return reflectionStep;
    }
}