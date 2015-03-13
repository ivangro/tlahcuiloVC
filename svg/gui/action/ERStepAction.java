package svg.gui.action;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import static svg.core.SVGConfig.*;
import svg.core.SVGRepository;
import svg.engagement.EngagementAction;
import svg.gui.IRefreshData;
import svg.reflection.ReflectionAction;
/**
 * Class to perform a Engagement-Reflection step
 * @author Ivan Guerrero
 */
public class ERStepAction extends AbstractAction{
    private SVGRepository repository;
    private IRefreshData container;
    
    public ERStepAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int steps = 0, reflectionSteps;
        boolean next;
        EngagementAction engagement = new EngagementAction(repository, container);
        ReflectionAction reflection = new ReflectionAction(repository, container);
        
        for (int i=0; i<ENGAGEMENT_STEPS; i++) {
            engagement.actionPerformed(ae);
        }
        reflectionSteps = 0;
        do {
            reflection.actionPerformed(ae);
            reflectionSteps++;
            next = reflection.isReflectionStep();
        } while (next && reflectionSteps < REFLECTION_STEPS);
        steps++;
        Logger.getLogger(CreateDrawingAction.class.getName()).log(Level.INFO, "Reflection steps performed: {0}" ,reflectionSteps);
        if (repository.getGuidelines().canTerminate()) {
            Logger.getLogger(CreateDrawingAction.class.getName()).log(Level.INFO, "End of drawing suggested");
        }
    }
}
