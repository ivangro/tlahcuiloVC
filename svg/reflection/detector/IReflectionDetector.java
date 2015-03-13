package svg.reflection.detector;

import svg.core.SVGRepository;

/**
 * Interface to be implemented by all the classes employed
 * to detect some aspects during the reflection phase
 * @author Ivan Guerrero
 */
public interface IReflectionDetector {
    /**
     * Executes the given detector
     * @param repository The repository to which the detector will be ran
     * @return True if additional detections can be performed during the current reflection phase.<br>
     * False if the ranges of the results correspond to the desired values.
     */
    boolean execute(SVGRepository repository);
}
