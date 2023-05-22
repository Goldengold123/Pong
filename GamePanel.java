/*
GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called
Child of JPanel because JPanel contains methods for drawing to the screen
Implements KeyListener interface to listen for keyboard input
Implements Runnable interface to use "threading" - let the game do two things at once
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // VARIABLE DECLARATION

    // dimensions of window
    public static final int GAME_WIDTH = 700;
    public static final int GAME_HEIGHT = 600;

    // variables for drawing screen
    public Thread gameThread;
    public Image image;
    public Graphics graphics;

    // game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int endState = 2;

    // menu command
    public int cmd = 0;

    // objects of the game
    public Ball ball;
    public Paddle paddle;
    public AutoPaddle npc;
    public PlayerScore playerScore;
    public ComputerScore computerScore;
    public JButton start = new JButton("Start!");
    public BigText endMessage;

    public GamePanel() {
        ball = new Ball(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
        paddle = new Paddle(GAME_WIDTH / 2 - Paddle.PADDLE_LENGTH / 2, GAME_HEIGHT - Paddle.PADDLE_THICKNESS);
        npc = new AutoPaddle(GAME_WIDTH / 2 - AutoPaddle.PADDLE_LENGTH / 2, 0);
        playerScore = new PlayerScore(GAME_WIDTH, GAME_HEIGHT);
        computerScore = new ComputerScore(GAME_WIDTH, GAME_HEIGHT);
        endMessage = new BigText(GAME_WIDTH / 7, GAME_HEIGHT / 3);

        gameState = 0;

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input

        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        // thread this class
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

    public void drawTitle(Graphics g) {
        int x, y;
        // title
        g.setFont(new Font("Impact", Font.PLAIN, 32));
        x = 5 + GAME_WIDTH / 3;
        y = 5 + GAME_HEIGHT / 4;
        g.setColor(Color.gray);
        g.drawString("WELCOME TO PONG!", x, y);
        x -= 5;
        y -= 5;
        g.setColor(Color.white);
        g.drawString("WELCOME TO PONG!", x, y);

        // instructions
        x = GAME_WIDTH / 15;
        y = GAME_HEIGHT / 3;
        g.setFont(new Font("Verdana", Font.PLAIN, 16));
        g.drawString("The objective of this game is to prevent the alien from invading your", x, y);
        y += 20;
        g.drawString("side of the game screen (bottom). Everytime the alien hits the bottom", x, y);
        y += 20;
        g.drawString("or top edges, the opponent gains a point. The first to 10 points wins!", x, y);
        y += 20;
        g.drawString("The top UFO (yellow) is controlled by the computer. The bottom UFO", x, y);
        y += 20;
        g.drawString("(orange) is controlled by you: use 'a' (left) and 'd' (right).", x, y);
        x = 11 * GAME_WIDTH / 24;
        y += 40;
        g.drawString("Good luck!", x, y);

        // options
        y = 2 * GAME_HEIGHT / 3;
        g.setFont(new Font("Verdana", Font.PLAIN, 24));
        g.drawString("START", x, y);
        if (cmd == 0)
            g.drawString(">", x - 30, y);
        x = 45 * GAME_WIDTH / 96;
        y += 50;
        g.drawString("QUIT", x, y);
        if (cmd == 1)
            g.drawString(">", x - 30, y);
    }

    public void drawGame(Graphics g) {
        paddle.draw(g);
        npc.draw(g);
        ball.draw(g);
        playerScore.draw(g);
        computerScore.draw(g);
    }

    public void drawEnd(Graphics g) {
        endMessage.draw(g);
    }

    // call the draw methods in each class to update positions as things move
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setColor(Color.white);
        if (gameState == titleState) {
            drawTitle(g);
        } else if (gameState == endState) {
            drawGame(g);
        } else {
            drawEnd(g);
        }
    }

    // call the move methods in other classes to update positions
    // this method is constantly called from run(). By doing this, movements appear
    // fluid and natural. If we take this out the movements appear sluggish and
    // laggy
    public void move() {
        if (gameState == playState) {
            paddle.move();
            npc.move(ball);
            ball.move();
        }
    }

    // handles all collision detection and responds accordingly
    public void checkCollision() {
        if (gameState == playState) {
            // score >= 10 -> stop
            if (PlayerScore.score >= 10)
                setWinner("you");
            else if (ComputerScore.score >= 10)
                setWinner("the computer");

            // stop player paddle if left or right edges hit
            if (paddle.x <= 0) {
                paddle.x = 0;
            }
            if (paddle.x >= GAME_WIDTH - Paddle.PADDLE_LENGTH) {
                paddle.x = GAME_WIDTH - Paddle.PADDLE_LENGTH;
            }

            // stop auto paddle if left or right edges hit
            if (npc.x <= 0) {
                npc.x = 0;
            }
            if (npc.x >= GAME_WIDTH - AutoPaddle.PADDLE_LENGTH) {
                npc.x = GAME_WIDTH - AutoPaddle.PADDLE_LENGTH;
            }

            // bounce ball if left or right edges hit
            if (ball.x <= 0) {
                ball.flipXDirection();
            }
            if (ball.x >= GAME_WIDTH - Ball.BALL_DIAMETER) {
                ball.flipXDirection();
            }

            // bounce if player paddle hit
            if (0 <= paddle.y - ball.y && paddle.y - ball.y <= Ball.BALL_DIAMETER
                    && -Ball.BALL_DIAMETER <= ball.x - paddle.x && ball.x - paddle.x <= Paddle.PADDLE_LENGTH) {
                ball.flipYDirection();
                ball.y = GAME_HEIGHT - Paddle.PADDLE_THICKNESS - Ball.BALL_DIAMETER;
            }
            // reset ball if bottom edge hit and paddle not hit
            else if (ball.y >= GAME_HEIGHT - Ball.BALL_DIAMETER) {
                ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                ComputerScore.score++;
            }

            // bounce if auto paddle hit
            if (0 <= ball.y - npc.y && ball.y - npc.y <= AutoPaddle.PADDLE_THICKNESS
                    && -Ball.BALL_DIAMETER <= ball.x - npc.x && ball.x - npc.x <= AutoPaddle.PADDLE_LENGTH) {
                ball.flipYDirection();
                ball.y = AutoPaddle.PADDLE_THICKNESS;
            }
            // reset ball if top edge hit and paddle not hit
            else if (ball.y <= npc.y) {
                ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                PlayerScore.score++;
            }
        }
    }

    public void setWinner(String winner) {
        gameState = 2;
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
        if (gameState == titleState) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                cmd = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                cmd = 1;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (cmd == 0)
                    gameState = playState;
                else if (cmd == 1)
                    System.exit(0);
            }
        }
        if (gameState == playState)
            paddle.keyPressed(e);
    }

    // if a key is released, we'll send it over to the PlayerBall class for
    // processing
    public void keyReleased(KeyEvent e) {
        if (gameState == playState)
            paddle.keyReleased(e);
    }

    // left empty because we don't need it; must be here because it is required to
    // be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e) {

    }
}