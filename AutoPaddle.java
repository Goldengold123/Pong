/*
 * Author: Grace Pu
 * Date: May 21
 * 
 * Description: 
 * AutoPaddle class defines behaviours for the computer-controlled paddle
 * Child of Rectangle to draw and check collisions easily
 */

import java.awt.*;
import javax.swing.ImageIcon;

public class AutoPaddle extends Rectangle {
    private int xVelocity; // velocity of autopaddle
    private final int SPEED = 5; // movement speed of autopaddle
    public static final int PADDLE_LENGTH = 100; // length of autopaddle
    public static final int PADDLE_THICKNESS = 20; // thickness of autopaddle
    private Image ufo;

    // constructor creates autopaddle at given location with given dimensions
    public AutoPaddle(int x, int y) {
        super(x, y, PADDLE_THICKNESS, PADDLE_LENGTH);
        ufo = new ImageIcon("images/ufo2.png").getImage();
    }

    // updates the location of the autopaddle
    // if ball on left of paddle, move left; if ball on right of paddle, move right
    public void move(Ball b) {
        // if paddle right under ball, don't move
        if (b.x + Ball.BALL_DIAMETER / 2 == x + AutoPaddle.PADDLE_LENGTH / 2)
            // needed to prevent division by 0
            xVelocity = 0;
        // otherwise use math to determine which direction to set xVelocity to
        else
            // the long math expression after "SPEED * " determines the sign of xVelocity
            // by dividing absolute value and actual value
            // of the difference between ball center and autopaddle center
            xVelocity = SPEED * (Math.abs(b.x + Ball.BALL_DIAMETER / 2 - x - AutoPaddle.PADDLE_LENGTH / 2)
                    / (b.x + Ball.BALL_DIAMETER / 2 - x - AutoPaddle.PADDLE_LENGTH / 2));

        x = x + xVelocity; // only x position needs to be updated b/c y position is constant
    }

    // draws autopaddle to the screen at current location (x,y)
    public void draw(Graphics g) {
        g.drawImage(ufo, x, y, null);
    }
}