/******************************************
 * Created by Khizer Amin on 3/20/2015.
 * Abstract class for the CoinPicker.
 ******************************************/
 

public class AbstractCoinsPicker extends Object{

    protected double x, y;
    protected double vel_X, vel_Y;
    protected boolean alive;

    AbstractCoinsPicker()
    {
        setAlive(false);
        setX(0.0);
        setY(0.0);
        setVel_X(0.0);
        setVel_Y(0.0);
    }
    /**********************
     * Abstract setter methods.
     **********************/
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
    public void setVel_X(double vel_X) {this.vel_X = vel_X;}
    public void setVel_Y(double vel_Y) {this.vel_Y = vel_Y;}
    public void setAlive(boolean alive) {this.alive = alive;}

    /***********************
     * Abstract getter methods.
     ***********************/
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public boolean isAlive() {return this.alive;}


}
