package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.MirroringAction;

/**
 *
 * @author Ivan Guerrero
 */
public class MirroringButton extends JButton {
    private SVGRepository repo;
    private String hIconPath = "/images/hMirrorIcon.png";
    private String vIconPath = "/images/vMirrorIcon.png";
    private String rIconPath = "/images/radialIcon.png";
    
    public MirroringButton() {
        super.setIcon(new ImageIcon(getClass().getResource(hIconPath)));
    }
    
    public MirroringButton(SVGRepository repo, IRefreshData dataContainer, MirroringAction.MirroringType type) {
        super(new MirroringAction(repo, dataContainer, type));
        this.repo = repo;
        switch (type) {
            case Horizontal:
                super.setIcon(new ImageIcon(getClass().getResource(hIconPath)));
                break;
            case Vertical:
                super.setIcon(new ImageIcon(getClass().getResource(vIconPath)));
                break;
            case Radial:
                super.setIcon(new ImageIcon(getClass().getResource(rIconPath)));
                break;
        }
    }
    
    public void setRepository(SVGRepository repo) {
        this.repo = repo;
    }

    /**
     * @return the hIconPath
     */
    public String gethIconPath() {
        return hIconPath;
    }

    /**
     * @param hIconPath the hIconPath to set
     */
    public void sethIconPath(String hIconPath) {
        this.hIconPath = hIconPath;
        setIcon(new ImageIcon(getClass().getResource(hIconPath)));
    }

    /**
     * @return the vIconPath
     */
    public String getvIconPath() {
        return vIconPath;
    }

    /**
     * @param vIconPath the vIconPath to set
     */
    public void setvIconPath(String vIconPath) {
        this.vIconPath = vIconPath;
        setIcon(new ImageIcon(getClass().getResource(vIconPath)));
    }
}
