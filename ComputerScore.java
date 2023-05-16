
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class ComputerScore extends Rectangle {

    public int score;
    public static int GAME_WIDTH;
    public static int GAME_HEIGHT;

    // constructor creates ball at given location with given dimensions
    public ComputerScore(int w, int h) {
        score = 0;
        GAME_WIDTH = w;
        GAME_HEIGHT = h;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawString("" + score, 3 * GAME_WIDTH / 4, GAME_HEIGHT / 10);
    }

}