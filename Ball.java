
/* Ball class defines behaviours for the ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

import javax.swing.ImageIcon;

public class Ball extends Rectangle {

    public int yVelocity;
    public int xVelocity;
    public final int SPEED = 5; // movement speed of ball
    public static int BALL_DIAMETER = 20; // size of ball
    private Image image;

    // constructor creates ball at given location with given dimensions
    public Ball(int x, int y) {
        super(x, y, BALL_DIAMETER, BALL_DIAMETER);
        reset(x, y);
        image = new ImageIcon("images/cannonball.png").getImage();
    }

    // reset ball to middle with random heading angle
    // pointed towards the user with a related acute angle greater than pi/12
    // (if it is less than pi/12, the game will be very boring)
    public void reset(int i, int j) {
        double a = Math.random() * 2 * Math.PI / 3;
        double initialHeadingAngle = 7 * Math.PI / 12 + ((a < Math.PI / 3) ? a : (a + Math.PI / 6));
        yVelocity = -(int) (SPEED * Math.cos(initialHeadingAngle));
        xVelocity = (int) (SPEED * Math.sin(initialHeadingAngle));
        x = i;
        y = j;
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
        BALL_DIAMETER = y / 20 + 20;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        // g.drawImage(image, x, y, null);
        g.setColor(Color.green);
        g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }

}