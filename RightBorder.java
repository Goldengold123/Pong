
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class RightBorder extends Rectangle {

    public double incline;
    public static int GAME_WIDTH;
    public static int GAME_HEIGHT;
    public int x[];
    public int y[];

    // constructor creates ball at given location with given dimensions
    public RightBorder(int w, int h, double i) {
        incline = i;
        GAME_WIDTH = w;
        GAME_HEIGHT = h;
        x = new int[] { GAME_WIDTH, GAME_WIDTH, GAME_WIDTH - (int) (GAME_HEIGHT / Math.tan(incline)) };
        y = new int[] { 0, GAME_HEIGHT, 0 };
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillPolygon(x, y, 3);
    }

}