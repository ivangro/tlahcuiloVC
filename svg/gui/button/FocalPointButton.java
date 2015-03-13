package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.FocalPointAction;

/**
 * Button to insert a focal point inside a drawing
 * @author Ivan Guerrero
 */
public class FocalPointButton extends JButton {
    private SVGRepository repository;
    
    public FocalPointButton() {
        this(null, null);
    }
    
    public FocalPointButton(SVGRepository repository, IRefreshData dataContainer) {
        super(new FocalPointAction(repository, dataContainer));
        super.setIcon(new ImageIcon(getClass().getResource("/images/focalPointIcon.png")));
        this.repository = repository;
    }
    
    public void setSVGRepository(SVGRepository repository) {
        this.repository = repository;
    }
}
