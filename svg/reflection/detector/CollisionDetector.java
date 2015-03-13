package svg.reflection.detector;

import ivangro.utils.Statistics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.actions.ActionMoveElement;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.reflection.SVGPair;

/**
 * Detects when inside a unit exists uneven collisions and removes them
 * @author Ivan Guerrero
 */
public class CollisionDetector implements IReflectionDetector{
    private SVGRepository repository;
    private int totalCollisions;
    
    /** Determines when to remove collisions from a unit */
    private static final double MAX_STD_DEV = 1.5;
    
    @Override
    public boolean execute(SVGRepository repository) {
        this.repository = repository;
        svg.detect.CollisionDetector detector = new svg.detect.CollisionDetector();
        boolean res = false;
        totalCollisions = 0;
        
        for (SVGElement unit : repository.getElements(1)) {
            List<SVGPair> collisions = new ArrayList<>();
            for (SVGElement elem : unit.getSimpleElements()) {
                SVGElement collision = detector.hasCollision(unit.getSimpleElements(), elem);
                if (collision == null)
                    collision = detector.hasTangent(unit.getSimpleElements(), elem);
                
                if (collision != null && !collisions.contains(new SVGPair(elem, collision))) {
                    collisions.add(new SVGPair(elem, collision));
                }
            }
            res |= removeCollisions(collisions, unit.getCenterX(), unit.getCenterY());
        }
        return res;
    }

    private boolean removeCollisions(List<SVGPair> collisions, int cx, int cy) {
        boolean res = false;
        double[] dist = new double[collisions.size()];
        for (int i=0; i<collisions.size(); i++) {
            SVGPair collision = collisions.get(i);
            dist[i] = Math.sqrt(Math.pow(collision.getA().getCenterX() - collision.getB().getCenterX(), 2) + 
                                Math.pow(collision.getA().getCenterY() - collision.getB().getCenterY(), 2));
        }
        double stdDev = Statistics.standardDeviation(dist);
        if (stdDev < MAX_STD_DEV) {
            //Logger.getLogger(ReflectionAction.class.getName()).log(Level.INFO, "Collision found, but not enough Std.Dev: {0}", stdDev);
            return false;
        }
        else
            Logger.getGlobal().log(Level.FINE, "Removing collision: {0}", stdDev);
        
        for (int i=0; i<collisions.size(); i++) {
            SVGPair pair = collisions.get(i);
            SVGElement collision = pair.getA();
            
            //Move the collisioned element far away from the reference and from the center
            int offsetx, offsety;
            offsetx = (Math.random() > 0.5) ? -collision.getWidth() : collision.getWidth();
            offsety = (Math.random() > 0.5) ? -collision.getHeight() : collision.getHeight();
            
            int lastLevel = repository.getCurrentLevel();
            ActionMoveElement move = new ActionMoveElement();
            move.setNewX(collision.getCenterX() + offsetx);
            move.setNewY(collision.getCenterY() + offsety);
            repository.applyAction(move, collision);
            repository.setCurrentLevel(lastLevel);
            Logger.getGlobal().log(Level.FINE, "Collision removed {0}", collision);
            totalCollisions++;
            res = true;
        }
        return res;
    }

    /**
     * @return the totalCollisions
     */
    public int getTotalCollisions() {
        return totalCollisions;
    }
}
