package svg.gui.button;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Ivan Guerrero
 */
public class EndOfDesignButton extends JButton {
    private String imagePath;
    
    public EndOfDesignButton(Action action) {
        super(action);
        imagePath = "/images/endOfDesignIcon.png";
        super.setIcon(new ImageIcon(getClass().getResource(imagePath)));
    }
    
    public EndOfDesignButton() {
        this(null);
    }
}
