package svg.elems;

/**
 * Equal: Share the same ID
 * Equivalent: Share the same attributes (noElements, shape, distance)
 * Mirrored: Share the same attributes and have mirrored shapes
 * Similar: Share same type (ElementText, AbstractUnit, ...)
 * Distinct
 */
public enum SVGComparisonResult {equal, equivalent, mirrored, similar, distinct};