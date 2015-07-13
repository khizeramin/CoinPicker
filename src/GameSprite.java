/**
 * Created by khize_000 on 3/20/2015.
 */
import java.awt.*;
import java.net.*;
import java.applet.*;
import java.awt.*;
import javax.vecmath.Vector2d;
import processing.core.PVector;

public class GameSprite extends Object{

    GameEntityLoader loader;
    private PVector _POS_ = new PVector();
    private PVector _VEL_ = new PVector();
    protected PVector steerForce;
    protected PVector NextVelocity;
    public static final Float _MASS_ = 1f;
    static int SCREENWIDTH = 540;
    static int SCREENHEIGHT = 540;

    GameSprite(Applet _aplt, Graphics2D g) {
        loader = new GameEntityLoader(_aplt);
        loader.setGraphics2d(g);
        loader.setAlive(false);
        NextVelocity = new PVector();
        _POS_ = new PVector(0, 0);
        _VEL_ = new PVector(0, 0);
        steerForce = new PVector();
    }
    //Try calling this method without using loader object.
    public void draw(){
        //loader.g2d.drawImage(loader.getImage(),loader._AT,loader._applet);
        loader.draw();
    }

    public void load(String filename) {
        loader.loadImage(filename);
    }

    public void updateBOTPosition() {

        PVector Accelerate = new PVector();
        steerForce.div(_MASS_);
        Accelerate.set(steerForce);
        _VEL_.add(Accelerate);
        //System.out.println(_VEL_);
        _POS_.add(_VEL_);
        //System.out.println(_POS_);

    }

    public void pointerTransform(float angle) {
        loader.setX(_POS_.x);
        loader.setY(_POS_.y);
        loader.pointerTransformation(angle);
    }
    public void transform() {
        loader.setX(_POS_.x);
        loader.setY(_POS_.y);
        loader.targetTransformation();
    }

    public void setPosition(PVector position) {
        this._POS_ = position;
    }
    PVector getPosition() {
        return _POS_;
    }

    public void setVelocity(PVector velocity) {
        this._VEL_ = velocity;
    }
    PVector getVelocity() {
        return _VEL_;
    }

    public void setAlive(boolean alive) {
        loader.setAlive(alive);
    }
    public boolean alive() {
        return loader.isAlive();
    }
    public int getWidth() {
        return loader.Width();
    }
    public int getHeight() {
        return loader.Height();
    }

    public Rectangle getBounds() {
        return loader.getBounds();
    }

    public boolean _COLLISION_(GameSprite sprite) {
        return getBounds().intersects(sprite.getBounds());
    }

    //An obstacle avoidance Algorithm can be implemented here...
    public void __avoid__ () {

    }
}
