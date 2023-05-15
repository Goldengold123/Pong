
/* Ball class defines behaviours for the ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    public int yVelocity;
    public final int SPEED = 5;
    public static final int PADDLE_LENGTH = 150;
    public static final int PADDLE_THICKNESS = 10;

    // constructor creates ball at given location with given dimensions
    public Paddle(int x, int y) {
        super(x, y, PADDLE_THICKNESS, PADDLE_LENGTH);
    }

    // called whenever the movement of the ball changes in the y-direction (up/down)
    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    // called from GamePanel when any keyboard input is detected
    // updates the direction of the ball based on user input
    // if the keyboard input isn't any of the options (d, a, w, s), then nothing
    // happens
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            setYDirection(-SPEED);
            move();
        }
        if (e.getKeyChar() == 's') {
            setYDirection(SPEED);
            move();
        }
    }

    // called from GamePanel when any key is released (no longer being pressed down)
    // Makes the ball stop moving in that direction
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            setYDirection(0);
            move();
        }
        if (e.getKeyChar() == 's') {
            setYDirection(0);
            move();
        }
    }

    // called frequently from both Ball class and GamePanel class
    // updates the current location of the ball
    public void move() {
        y = y + yVelocity;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x, y, PADDLE_THICKNESS, PADDLE_LENGTH);
    }

}