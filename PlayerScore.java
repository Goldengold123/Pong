
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/

public class PlayerScore extends BigText {

  public static int score = 0;

  // constructor creates ball at given location with given dimensions
  public PlayerScore(int w, int h) {
    super("" + score, w / 2, 9 * h / 10);
  }
}