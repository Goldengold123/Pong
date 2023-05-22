
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class SmallText extends Rectangle {

    public String message;
    public int x;
    public int y;

    // constructor creates ball at given location with given dimensions
    public SmallText(String m, int w, int h) {
        message = m;
        x = w;
        y = h;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        String line = "";
        int cnt = 0;
        g.setFont(new Font("Verdana", Font.PLAIN, 16));
        g.setColor(Color.white);
        for (int i = 0; i < message.length(); i++) {
            if (line.length() >= 70) {
                g.drawString(line, x, y + cnt * 20);
                line = "";
                cnt++;
            }
            line += message.substring(i, i + 1);
        }
        g.drawString(line, x, y + cnt * 20);
    }

}