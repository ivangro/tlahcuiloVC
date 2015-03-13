package svg.eval;

import svg.core.SVGRepository;

/**
 * Interface to be implemented by all the drawing evaluators
 * @author Ivan Guerrero
 */
public interface IEvaluator {
    IEvaluationResult evaluate(SVGRepository repository);
}
