/*
 * Author: Grace Pu
 * Date: May 21
 * 
 * Description: 
 * Ball class defines behaviours for the ball
 * Child of Rectangle to draw and check collisions easily
 */

import java.awt.*;
import javax.swing.ImageIcon;

public class Ball extends Rectangle {
    private int yVelocity;
    private int xVelocity;
    private final int SPEED = 7; // movement speed of ball
    public static final int BALL_DIAMETER = 40; // size of ball
    private Image alien;

    // constructor creates ball at given location with given dimensions
    public Ball(int x, int y) {
        super(x, y, BALL_DIAMETER, BALL_DIAMETER);
        reset(x, y);
        alien = new ImageIcon("images/alien.png").getImage();
    }

    // reset ball to middle with random heading angle
    // pointed towards the user with a related acute angle greater than pi/12
    // (if it is less than pi/12, the game will be very slow and boring)
    public void reset(int i, int j) {
        double initialHeadingAngle = 13 * Math.PI / 12 + Math.random() * 5 * Math.PI / 6;

        // set x and y velocities using trigonometry
        xVelocity = (int) (SPEED * Math.cos(initialHeadingAngle));
        yVelocity = (int) (SPEED * Math.sin(initialHeadingAngle));

        // set x and y positions
        // (probably center but using i, j allows for flexibility for potential updates)
        x = i;
        y = j;
    }

    // called when vertical direction of ball should be flipped (paddle hit)
    public void flipYDirection() {
        yVelocity = -1 * yVelocity;
    }

    // called when horizontal direction of ball should be flipped (left/right hit)
    public void flipXDirection() {
        xVelocity = -1 * xVelocity;
    }

    // updates the location of the ball
    public void move() {
        y = y + yVelocity;
        x = x + xVelocity;
    }

    // draws ball to the screen at current location (x, y)
    public void draw(Graphics g) {
        g.drawImage(alien, x, y, null);
    }

}