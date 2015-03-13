package svg.engagement;

import svg.core.SVGRepository;

/**
 * Interface to be implemented for every class employed to generate atoms based on the contexts of a drawing
 * @author Ivan Guerrero
 */
public interface IAtomGenerator {
    void detectAtoms(SVGRepository repository, boolean updateParams);

}
