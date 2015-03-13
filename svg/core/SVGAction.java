package svg.core;

/**
 * Basic elements for a SVG action applied to an element.<br>
 * Any action that implements this interface is employed with the applyAction method of the SVGRepository class.
 * @author Ivan Guerrero
 */
public interface SVGAction extends Cloneable {
    /** Employed to apply an action to an element and update its properties */
    void applyAction(SVGElement element);
    /** Method to determine the repository where the action will be applied */
    void setRepository(SVGRepository repository);
    /** Determines if the action is the end of a design action */
    void setEndOfDesignAction();
    /** Determines if the action sets the end of a block of actions established as a design action */
    boolean isEndOfDesignAction();
    /** Obtains an explicative text for the action */
    String getActionText();
    /** Obtains the store the action as part of a previous story */
    String getActionForStory();
    /** Method to clone the action */
    Object clone();
}
