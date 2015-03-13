package svg.gui.action;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import static svg.core.SVGConfig.*;
import svg.core.SVGRepository;
import svg.engagement.EngagementAction;
import svg.gui.IRefreshData;
import svg.reflection.ReflectionAction;

/**
 * Action to implement the E-R cycle for drawing generation
 * @author Ivan Guerrero
 */
public class CreateDrawingAction extends AbstractAction{
    private SVGRepository repository;
    private IRefreshData container;
    
    public CreateDrawingAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int steps = 0, reflectionSteps;
        int option;
        boolean next;
        
        //If this method is called from the GUI, ask to delete the current drawing
        if (container != null) {
            option = JOptionPane.showConfirmDialog(null, "Do you want to delete the current drawing?", 
                                          "Delete current drawing", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                container.deleteDrawing();
            }
        }
        
        Logger.getGlobal().log(Level.INFO, "Drawing generation started...");
        EngagementAction engagement = new EngagementAction(repository, container);
        ReflectionAction reflection = new ReflectionAction(repository, container);
        
        do {
            //Engagement Phase
            for (int i=0; i<ENGAGEMENT_STEPS; i++) {
                Logger.getGlobal().log(Level.INFO, "Engagement step: {0}-{1}", new Object[]{steps, i});
                engagement.actionPerformed(ae);
            }
            
            //Reflection Phase
            reflectionSteps = 0;
            do {
                Logger.getGlobal().log(Level.INFO, "Reflection step: {0}-{1}", new Object[]{steps, reflectionSteps});
                reflection.actionPerformed(ae);
                reflectionSteps++;
                next = reflection.isReflectionStep();
            } while (next && reflectionSteps < REFLECTION_STEPS);
            
            //Final analysis
            steps++;
            if (repository.getGuidelines().canTerminate()) {
                Logger.getGlobal().log(Level.INFO, "End of drawing suggested");
                break;
            }
        } while (steps <= MAX_CYCLES);
        Logger.getGlobal().log(Level.INFO, "Steps performed: {0}" ,steps);
    }
}