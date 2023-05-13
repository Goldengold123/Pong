
/* Ball class defines behaviours for the ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class Ball extends Rectangle {

    public int yVelocity;
    public int xVelocity;
    public final int SPEED = 7; // movement speed of ball
    public static final int BALL_DIAMETER = 20; // size of ball

    // constructor creates ball at given location with given dimensions
    public Ball(int x, int y) {
        super(x, y, BALL_DIAMETER, BALL_DIAMETER);
        double a = Math.random() * 2 * Math.PI / 3;
        double initialHeadingAngle = 7 * Math.PI / 12 + ((a < Math.PI / 3) ? a : (a + Math.PI / 6));
        xVelocity = (int) (SPEED * Math.cos(initialHeadingAngle));
        yVelocity = (int) (SPEED * Math.sin(initialHeadingAngle));
    }

    // called whenever the movement of the ball changes in the y-direction (up/down)
    public void flipYDirection() {
        yVelocity = -1 * yVelocity;
    }

    // called whenever the movement of the ball changes in the x-direction
    // (left/right)
    public void flipXDirection() {
        xVelocity = -1 * xVelocity;
    }

    // called frequently from both Ball class and GamePanel class
    // updates the current location of the ball
    public void move() {
        y = y + yVelocity;
        x = x + xVelocity;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }

}