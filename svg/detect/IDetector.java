package svg.detect;

import java.util.List;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Interface to be implemented by a detector employed to analyze a SVG drawing
 * @author Ivan Guerrero
 */
public interface IDetector {
    void detect(List<List<SVGElement>> elements, SVGRepository repository);
    
    String getDescription();
}
