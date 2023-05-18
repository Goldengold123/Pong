
/* Ball class defines behaviours for the ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

// import javax.swing.ImageIcon;

public class Ball extends Rectangle {

    private final static double pi1 = 3.141592653; // rounded version to prevent precision errors
    private final static double pi2 = 3.141592654; // rounded version to prevent precision errors

    public double incline;
    public double theta;

    public double xReal;
    public double yReal;
    public double xVelocity;
    public double yVelocity;

    public final int SPEED = 5; // movement speed of ball
    public static int BALL_DIAMETER = 20; // size of ball
    // private Image image;

    // constructor creates ball at given location with given dimensions
    public Ball(int x, int y, double i) {
        super(x, y, BALL_DIAMETER, BALL_DIAMETER);
        reset(x, y);
        xReal = x;
        yReal = y;
        incline = i;
        // image = new ImageIcon("images/cannonball.png").getImage();
    }

    // reset ball to middle with random heading angle
    // pointed towards the user with a related acute angle greater than pi/12
    // (if it is less than pi/12, the game will be very boring)
    public void reset(int i, int j) {
        theta = 5 * Math.PI / 3;
        // theta = 7 * Math.PI / 6 + Math.random() * 2 * Math.PI / 3;
        yVelocity = -(SPEED * Math.cos(theta));
        xVelocity = (SPEED * Math.sin(theta));
        xReal = i;
        yReal = j;
        x = i;
        y = j;
    }

    // called whenever the movement of the ball changes in the y-direction (up/down)
    public void flipYDirection() {
        theta = 2 * Math.PI - theta;
    }

    // // called whenever the movement of the ball changes in the x-direction
    // // (left/right)
    // public void flipXDirection() {
    // xVelocity = -1 * xVelocity;
    // }

    public void flipHeading() {
        System.out.println(theta + " " + xVelocity + " " + yVelocity);
        if (Math.abs(theta - Math.PI) <= 1e-2) {
            if (theta > Math.PI)
                theta = Math.PI / 3;
            else
                theta = 5 * Math.PI / 3;
        } else if (theta <= 1e-2) {
            theta = 2 * Math.PI / 3;
        } else if (2 * Math.PI - theta <= 1e-2) {
            theta = 4 * Math.PI / 3;
        }
        if (pi1 <= theta && theta < 3 * pi2 / 2) {
            // theta = 2 * Math.PI - (Math.PI - incline) * (theta - Math.PI) / incline;
            theta = Math.PI + incline - (Math.PI - incline) * (Math.PI + incline - theta) / incline;
        } else if (3 * pi1 / 2 <= theta && theta < 2 * pi2) {
            // theta = Math.PI + (Math.PI - incline) * (2 * Math.PI - theta) / incline;
            theta = 2 * Math.PI - incline - (Math.PI - incline) * (incline + theta - 2 * Math.PI) / incline;
        } else if (pi1 / 2 <= theta && theta < pi2) {
            // theta = incline * (Math.PI - theta) / (Math.PI - incline);
            theta = incline - incline * (theta - incline) / (Math.PI - incline);
        } else if (0 <= theta && theta < pi2 / 2) {
            // theta = Math.PI - incline * (theta) / (Math.PI - incline);
            theta = Math.PI - incline + incline * (Math.PI - incline - theta) / (Math.PI - incline);
        }
        System.out.println(theta);
        System.out.println();
    }

    // called frequently from both Ball class and GamePanel class
    // updates the current location of the ball
    public void move() {
        xVelocity = SPEED * Math.cos(theta);
        yVelocity = SPEED * Math.sin(theta);
        yReal += yVelocity;
        xReal += xVelocity;
        y = (int) yReal;
        x = (int) xReal;
        BALL_DIAMETER = (int) (yReal / 10 + 10);
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        // g.drawImage(image, x, y, null);
        g.setColor(Color.green);
        g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }

}