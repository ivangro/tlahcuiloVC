package svg.gui;

import svg.core.SVGElement;

/**
 * Interface to be implemented by a GUI element whose data needs to be refreshed
 * after an action is performed
 * @author Ivan Guerrero
 */
public interface IRefreshData {
    void refreshData();
    SVGElement getSelectedElement();
    void setSelectedElement(SVGElement elem);
    void deleteDrawing();
    void setCurrentStatus(String status);
    boolean isSnapToGrid();
}
