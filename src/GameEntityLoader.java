/*************************************************************************************
 * Created by khize_000 on 3/20/2015.
 *
 * The "GameEntityLoader" class handles bitmapped Images for the applet.
 *************************************************************************************/

import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.net.*;

public class GameEntityLoader extends AbstractCoinsPicker {

    protected Applet _applet;
    protected AffineTransform _AT;
    protected Image image;
    protected Graphics2D g2d;

    GameEntityLoader(Applet _aplt) {
        _applet = _aplt;
        setImage(image);
        setAlive(true);
    }

    /***************************
     * Get image for the applet
     ***************************/
    public Image getImage() {
        return image;
    }

    /***************************
     * Set image for the applet
     ***************************/
    public void setImage(Image image) {
        this.image = image;
    }

    /***************************
     * Load the Image
     ***************************/
    public void loadImage(String filename) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(filename);
        while(getImage().getWidth(_applet) <= 0);
        double affine_X = getImage().getWidth(_applet) - Height()/2;
        double affine_Y = getImage().getHeight(_applet) - Width()/2;
        _AT = AffineTransform.getTranslateInstance(affine_X, affine_Y);
    }

    public int Width() {
        if (image != null)
            return getImage().getWidth(_applet);
        else
            return 0;
    }

    public int Height() {
        if (image != null)
            return getImage().getHeight(_applet);
        else
            return 0;
    }

    /*******************************************************
     * Applying translation and transformation on the image
     *******************************************************/
    public void pointerTransformation(float angle) {
        _AT.setToIdentity();
        _AT.translate((int) getX() + Width() / 2, (int) getY() + Height() / 2);
        _AT.translate(-Width()/2, -Height()/2);
        _AT.rotate(angle);
    }

    public void targetTransformation() {
        _AT.setToIdentity();
        _AT.translate((int) getX() + Width() / 2, (int) getY() + Height() / 2);
        _AT.translate(-Width()/2, -Height()/2);
    }
    public void draw() {
        g2d.drawImage(image, _AT, _applet);
    }

    public void setGraphics2d(Graphics2D g) {
        g2d = g;
    }

    public Rectangle getBounds() {
        Rectangle r;
        r = new Rectangle((int)getX(), (int)getY(), Width(), Height());

        return r;
    }
}
