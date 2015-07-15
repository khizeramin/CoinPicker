/**********************************************************************************************************
 * Created by Khizer Amin on 3/20/2015.
 * The main game class for the CoinPicker. this class implements
 * the methods and runs the game within a thread.
 **********************************************************************************************************/


import java.awt.*;
import java.applet.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.*;
import processing.core.PVector;

public class Game extends Applet
        implements Runnable, KeyListener {

    /**********************************
     * Setting the display coordinates.
     **********************************/
    static int SCREENWIDTH = 540;
    static int SCREENHEIGHT = 540;

    /**********************************
     * Setting the number of obstacles.
     **********************************/
    static int OBSTACLES = 7;
    boolean key_SEEK, key_FLEE;
    /****************************************
     * Game objects declaration
     ****************************************/
    GameEntityLoader game_Environment;
    GameSprite _RoBoT_;
    GameSprite _COIN_;
    int _rand_X_coin_norm,_rand_Y_coin_norm;
    int _rand_X_coin_danger,_rand_Y_coin_danger;

    int currentState = 0;
    Graphics2D g2d;
    Graphics g;
    BufferedImage backBuffer;

    /**************************
     * Creating a thread object
     **************************/
    Thread game_thread;
    Thread second_thread;
    long sTime = System.currentTimeMillis();
    long effectiveTIME = 0;
    long thresholdTIME = 0;
    /**************************
     * Creating a random object
     **************************/
    Random rand = new Random();
    int _rand_X_norm;
    int _rand_Y_norm;
    int _rand_X_danger;
    int _rand_Y_danger;
    /**********************
     * Behavior Enumerators
     **********************/
    public GameBehaviorEnums behaviorEnums;

    /**************************
     * pointer speed
     **************************/
    public static final int SPEED = 5;

    public void init(){

        /********************************************************
         * Creating a buffer for all the images and sprites data.
         ********************************************************/
        backBuffer = new BufferedImage(SCREENWIDTH, SCREENHEIGHT, BufferedImage.TYPE_INT_RGB);
        /**************************************************************************
         * Creating a graphics2d which can be used to draw into this bufferedimage,
         * it is sort of a paint brush.
         **************************************************************************/
        g2d = backBuffer.createGraphics();
        /********************************************************
         * Creating and Loading the background environment image.
         ********************************************************/
        game_Environment = new GameEntityLoader(this);
        game_Environment.loadImage("C:/Users/khize_000/Documents/Java/CoinPicker/images/grid.png");

        /******************************************************
         * Loading the "GameSprite" objects into the environment
         ******************************************************/
        _RoBoT_ = new GameSprite(this, g2d);
        _RoBoT_.load("C:/Users/khize_000/Documents/Java/CoinPicker/images/pointer.png");
        _RoBoT_.setPosition(new PVector(60, 60));
        _RoBoT_.setVelocity(new PVector(3, 3));
        _RoBoT_.setAlive(true);
        behaviorEnums = GameBehaviorEnums.SEEK;

        /**************************************
         * Loading normal coin into the system.
         **************************************/
        _rand_X_coin_norm = rand.nextInt(SCREENWIDTH);
        _rand_Y_coin_norm = rand.nextInt(SCREENHEIGHT);

        _COIN_ = new GameSprite(this, g2d);
        _COIN_.load("C:/Users/khize_000/Documents/Java/CoinPicker/images/coin.png");
        _COIN_.setPosition(new PVector(210, 210));
        _COIN_.setAlive(true);


        addKeyListener(this);
    }

    /*************************************************************************************************
     * Overriding the update method,
     * instead of letting the applet calls the default update method...
     *************************************************************************************************/
    public void update(Graphics g){

        /************************************************
         * Instructions for Switching between algorithm's.
         ************************************************/


        g2d.drawImage(game_Environment.getImage(), 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);

        draw_RoBoT_();

        draw_COIN_();

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("default", Font.BOLD, 16));
        g2d.drawString("Seeking: s", 450, 20);
        g2d.drawString("Fleeing: f", 450, 35);

        paint(g);


    }

    public void keyTyped(KeyEvent k){}
    public void keyReleased(KeyEvent k){

    }
    public void keyPressed(KeyEvent k){

        int key = k.getKeyCode();
        switch (key){
            case KeyEvent.VK_S:
                behaviorEnums = GameBehaviorEnums.SEEK;
                break;
            case KeyEvent.VK_F:
                behaviorEnums = GameBehaviorEnums.FLEE;
                break;
        }
    }
    /*********************************************
     * Starting the only thread in the program...
     *********************************************/
    public void start() {
        game_thread = new Thread(this);
        game_thread.start();
    }

    public void draw_RoBoT_(){
        float theta = heading(_RoBoT_.getVelocity()) + (float)Math.PI/2;
        _RoBoT_.pointerTransform(theta);
        _RoBoT_.draw();
    }

    public void draw_COIN_() {

        _COIN_.transform();
        _COIN_.draw();

    }

    public void stop() {
        game_thread = null;
    }

    public void run() {
        //Try executing this statement without "this" keyword.
        Thread temp_Thread = Thread.currentThread();
        while (temp_Thread == game_thread)
        {
            try
            {
                Thread.sleep(20);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            __systemUpdate__();

            repaint();
        }
    }

    public void paint(Graphics g) {
        /*********************************************************************
         * This method draws the background for all the images and sprites.
         *********************************************************************/
        g.drawImage(backBuffer, 0, 0, this);
    }

    public void __systemUpdate__() {

//        if(key_SEEK)
//        {
//            behaviorEnums = GameBehaviorEnums.SEEK;
//        }
//        else if(key_FLEE)
//        {
//            behaviorEnums = GameBehaviorEnums.FLEE;
//        }

        switchBehviour(_COIN_.getPosition(), behaviorEnums);
        update_RoBoT_();
        update_COIN_();

        CollisionTest_COIN();
    }


    public void switchBehviour(PVector TargetPosition, GameBehaviorEnums behaviorEnums) {

        switch (behaviorEnums)
        {
            case SEEK:
                _bot_Motion__SEEK(TargetPosition);
                break;
            case FLEE:
                _bot_Motion__FLEE(TargetPosition);
                break;
        }

    }

    public void _bot_Motion__FLEE(PVector target) {

        /*******************************************
         * ........| The Flee Algorithm|............
         * *****************************************
         * Calculate the position of the target
         * and move in the opposite direction
         * with maximum speed.
         *******************************************/
        _RoBoT_.NextVelocity = PVector.sub(_RoBoT_.getPosition(), target);
        _RoBoT_.NextVelocity.normalize();
        _RoBoT_.NextVelocity.mult(SPEED);
        _RoBoT_.steerForce.set(_RoBoT_.NextVelocity);
        _RoBoT_.steerForce.sub(_RoBoT_.getVelocity());
        System.out.println(_RoBoT_.NextVelocity);

    }

    public void _bot_Motion__SEEK(PVector target) {

        /*******************************************
         * ........| The Seek Algorithm|............
         * *****************************************
         * Calculate the position of the target
         * and move towards it with maximum speed.
         *******************************************/

        _RoBoT_.NextVelocity = PVector.sub(target, _RoBoT_.getPosition());
        _RoBoT_.NextVelocity.normalize();
        _RoBoT_.NextVelocity.mult(SPEED);
        _RoBoT_.steerForce.set(_RoBoT_.NextVelocity);
        _RoBoT_.steerForce.sub(_RoBoT_.getVelocity());

    }
    public void CollisionTest_COIN() {
        if(_RoBoT_._COLLISION_(_COIN_))
        {
            _rand_X_norm = rand.nextInt(SCREENWIDTH);
            _rand_Y_norm = rand.nextInt(SCREENHEIGHT);
            _COIN_.setPosition(new PVector(_rand_X_norm, _rand_Y_norm));

        }

    }

    public float heading(PVector _focus_){
        return -1.0F * (float)Math.atan2((double)(-_focus_.y), (double)_focus_.x);
    }
}

