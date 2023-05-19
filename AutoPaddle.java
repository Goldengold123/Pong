
/* Ball class defines behaviours for the ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class AutoPaddle extends Rectangle {

    public int yVelocity;
    public final int SPEED = 5;
    public static final int PADDLE_LENGTH = 150;
    public static final int PADDLE_THICKNESS = 10;

    // constructor creates ball at given location with given dimensions
    public AutoPaddle(int x, int y) {
        super(x, y, PADDLE_THICKNESS, PADDLE_LENGTH);
    }

    // called frequently from both Ball class and GamePanel class
    // updates the current location of the ball
    public void move(Ball b) {
        yVelocity = ((Math.abs((b.y + Ball.BALL_DIAMETER / 2) - (y + AutoPaddle.PADDLE_LENGTH / 2)) <= SPEED) ? 0
                : SPEED * (Math.abs(b.y + Ball.BALL_DIAMETER / 2 - y - AutoPaddle.PADDLE_LENGTH / 2)
                        / (b.y + Ball.BALL_DIAMETER / 2 - y - AutoPaddle.PADDLE_LENGTH / 2)));
        y = y + yVelocity;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x, y, PADDLE_THICKNESS, PADDLE_LENGTH);
    }

}