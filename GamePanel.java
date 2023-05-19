
/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // dimensions of window
    public static final int GAME_WIDTH = 500;
    public static final int GAME_HEIGHT = 600;

    public Thread gameThread;
    public Image image;
    public Graphics graphics;

    public boolean stop = false;

    public Ball ball;
    public Paddle paddle;
    public AutoPaddle paddle2;
    public PlayerScore playerScore;
    public ComputerScore computerScore;

    public BigText endMessage;

    public GamePanel() {
        ball = new Ball(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
        paddle = new Paddle(0, GAME_HEIGHT / 2 - Paddle.PADDLE_LENGTH / 2);
        paddle2 = new AutoPaddle(GAME_WIDTH - AutoPaddle.PADDLE_THICKNESS, GAME_HEIGHT / 2 - Paddle.PADDLE_LENGTH / 2);
        playerScore = new PlayerScore(GAME_WIDTH, GAME_HEIGHT);
        computerScore = new ComputerScore(GAME_WIDTH, GAME_HEIGHT);
        endMessage = new BigText(GAME_WIDTH, GAME_HEIGHT);

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input

        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        // make this class run at the same time as other classes (without this each
        // class would "pause" while another class runs). By using threading we can
        // remove lag, and also allows us to do features like display timers in real
        // time!
        gameThread = new Thread(this);
        gameThread.start();
    }

    // paint is a method in java.awt library that we are overriding. It is a special
    // method - it is called automatically in the background in order to update what
    // appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g) {
        // we are using "double buffering here" - if we draw images directly onto the
        // screen, it takes time and the human eye can actually notice flashes of lag as
        // each pixel on the screen is drawn one at a time. Instead, we are going to
        // draw images OFF the screen, then simply move the image on screen as needed.
        image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
        graphics = image.getGraphics();
        draw(graphics);// update the positions of everything on the screen
        g.drawImage(image, 0, 0, this); // move the image on the screen

    }

    // call the draw methods in each class to update positions as things move
    public void draw(Graphics g) {
        Font oldFont = g.getFont();
        Font newFont = oldFont.deriveFont(oldFont.getSize() * 2F);
        g.setFont(newFont);
        paddle.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        playerScore.draw(g);
        computerScore.draw(g);
        if (stop) {
            oldFont = g.getFont();
            newFont = oldFont.deriveFont(oldFont.getSize());
            g.setFont(newFont);
            endMessage.draw(g);
        }
    }

    // call the move methods in other classes to update positions
    // this method is constantly called from run(). By doing this, movements appear
    // fluid and natural. If we take this out the movements appear sluggish and
    // laggy
    public void move() {
        if (!stop) {
            paddle.move();
            paddle2.move(ball);
            ball.move();
        }
    }

    // handles all collision detection and responds accordingly
    public void checkCollision() {
        if (!stop) {
            // score >= 10 -> stop
            if (playerScore.score >= 10)
                setWinner("you");
            else if (computerScore.score >= 10)
                setWinner("the computer");

            // stop player paddle if top or bottom edges hit
            if (paddle.y <= 0) {
                paddle.y = 0;
            }
            if (paddle.y >= GAME_HEIGHT - Paddle.PADDLE_LENGTH) {
                paddle.y = GAME_HEIGHT - Paddle.PADDLE_LENGTH;
            }

            // stop auto paddle if top or bottom edges hit
            if (paddle2.y <= 0) {
                paddle2.y = 0;
            }
            if (paddle2.y >= GAME_HEIGHT - AutoPaddle.PADDLE_LENGTH) {
                paddle2.y = GAME_HEIGHT - AutoPaddle.PADDLE_LENGTH;
            }

            // bounce ball if top or bottom edges hit
            if (ball.y <= 0) {
                ball.flipYDirection();
            }
            if (ball.y >= GAME_HEIGHT - Ball.BALL_DIAMETER) {
                ball.flipYDirection();
            }

            // bounce if player paddle hit
            if (0 <= ball.x - paddle.x && ball.x - paddle.x <= Paddle.PADDLE_THICKNESS
                    && -Ball.BALL_DIAMETER <= ball.y - paddle.y && ball.y - paddle.y <= Paddle.PADDLE_LENGTH) {
                ball.flipDirection(((ball.x - paddle.x) * 1.0 / Paddle.PADDLE_LENGTH));
                ball.x = Paddle.PADDLE_THICKNESS;

            }

            // reset ball if left edge hit and paddle not hit
            else if (ball.x <= 0) {
                ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                computerScore.score++;
            }

            // bounce if auto paddle hit
            if (0 <= paddle2.x - ball.x && paddle2.x - ball.x <= Ball.BALL_DIAMETER
                    && -Ball.BALL_DIAMETER <= ball.y - paddle2.y && ball.y - paddle2.y <= AutoPaddle.PADDLE_LENGTH) {
                ball.flipDirection(((ball.x - paddle2.x) / 1.0 * AutoPaddle.PADDLE_LENGTH));
                ball.x = GAME_WIDTH - AutoPaddle.PADDLE_THICKNESS - Ball.BALL_DIAMETER;
            }
            // reset ball if right edge hit and paddle not hit
            else if (ball.x >= GAME_WIDTH - Ball.BALL_DIAMETER) {
                ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                playerScore.score++;
            }
        }

    }

    public void setWinner(String winner) {
        stop = true;
        endMessage.message = "Congratuations to " + winner + "!";
    }

    // run() method is what makes the game continue running without end. It calls
    // other methods to move objects, check for collision, and update the screen
    public void run() {
        // the CPU runs our game code too quickly - we need to slow it down! The
        // following lines of code "force" the computer to get stuck in a loop for short
        // intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        while (true) { // this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            // only move objects around and update screen if enough time has passed
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    // if a key is pressed, we'll send it over to the PlayerBall class for
    // processing
    public void keyPressed(KeyEvent e) {
        if (!stop)
            paddle.keyPressed(e);
    }

    // if a key is released, we'll send it over to the PlayerBall class for
    // processing
    public void keyReleased(KeyEvent e) {
        if (!stop)
            paddle.keyReleased(e);
    }

    // left empty because we don't need it; must be here because it is required to
    // be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e) {

    }
}