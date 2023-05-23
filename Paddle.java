/*
 * Author: Grace Pu
 * Date: May 21
 * 
 * Description: 
 * Paddle class defines behaviours for the paddle
 * Child of Rectangle to draw and check collisions easily
 */

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Paddle extends Rectangle {
    private int xVelocity; // velocity of paddle
    private final int SPEED = 5; // movement speed of paddle
    public static final int PADDLE_LENGTH = 100; // length of paddle
    public static final int PADDLE_THICKNESS = 20; // thickness of paddle
    private Image ufo;

    // constructor creates paddle at given location with given dimensions
    public Paddle(int x, int y) {
        super(x, y, PADDLE_THICKNESS, PADDLE_LENGTH);
        ufo = new ImageIcon("images/ufo.png").getImage();
    }

    // called whenever the movement of the paddle changes in horizontal direction
    private void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }

    // called from GamePanel when any keyboard input is detected
    // updates the direction of the paddle based on user input
    // if the keyboard input isn't any of the options, then nothing happens
    public void keyPressed(KeyEvent e) {
        // if a or left arrow pressed, move left
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            setXDirection(-SPEED);
            move();
        }

        // if d or right arrow pressed, move right
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            setXDirection(SPEED);
            move();
        }
    }

    // called from GamePanel when any key is released (no longer being pressed down)
    // Makes the paddle stop moving in that direction
    public void keyReleased(KeyEvent e) {
        // if a or left arrow released
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            setXDirection(0);
            move();
        }

        // if d or right arrow released
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            setXDirection(0);
            move();
        }
    }

    // updates the location of the paddle
    public void move() {
        x = x + xVelocity; // only x position needs to be updated b/c y position constant
    }

    // draws paddle to the screen at current location (x,y)
    public void draw(Graphics g) {
        g.drawImage(ufo, x, y, null);
    }
}