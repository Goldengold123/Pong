/*
 * Author: Grace Pu
 * Date: May 21
 * 
 * Description: 
 * GamePanel class runs the game and controls some of the events (e.g. game loop)
 * Child of JPanel because JPanel contains methods for drawing to the screen
 * Implements Runnable interface to use threading
 * Implements KeyListener interface to listen for keyboard input
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
    public int cmd;

    // objects of the game
    public Ball ball;
    public Paddle paddle;
    public AutoPaddle npc;

    // scores
    public int playerScore = 0;
    public int computerScore = 0;

    // constructor - initialize game objects
    public GamePanel() {
        ball = new Ball(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
        paddle = new Paddle(GAME_WIDTH / 2 - Paddle.PADDLE_LENGTH / 2, GAME_HEIGHT - Paddle.PADDLE_THICKNESS);
        npc = new AutoPaddle(GAME_WIDTH / 2 - AutoPaddle.PADDLE_LENGTH / 2, 0);

        gameState = 0; // set game state to title screen
        cmd = 0; // set menu item to top

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT)); // preferred window size

        // thread this class
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Override paint method in java.awt library
    @Override
    public void paint(Graphics g) {
        // use double buffering
        image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
        graphics = image.getGraphics();
        draw(graphics);// update the positions of everything on the screen
        g.drawImage(image, 0, 0, this); // move the image on the screen

    }

    // method to draw title screen
    // separated from general draw method for organization
    public void drawTitle(Graphics g) {
        int x, y; // use variables to indicate position to draw text -> cleaner code

        // title text
        g.setFont(new Font("Impact", Font.PLAIN, 32));

        // title shadow for text effect
        x = 5 + GAME_WIDTH / 3;
        y = 5 + GAME_HEIGHT / 4;
        g.setColor(Color.gray);
        g.drawString("WELCOME TO PONG!", x, y);

        // actual title
        x -= 5;
        y -= 5;
        g.setColor(Color.white);
        g.drawString("WELCOME TO PONG!", x, y);

        // instructions + good luck text
        g.setFont(new Font("Verdana", Font.PLAIN, 16));
        x = GAME_WIDTH / 15;
        y = GAME_HEIGHT / 3;
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

        // menu text
        g.setFont(new Font("Verdana", Font.PLAIN, 24));

        // START menu item
        y = 2 * GAME_HEIGHT / 3;
        g.drawString("START", x, y);
        if (cmd == 0) // if START menu item highlighted, draw ">"
            g.drawString(">", x - 30, y);

        // QUIT menu item
        x = 45 * GAME_WIDTH / 96;
        y += 50;
        g.drawString("QUIT", x, y);
        if (cmd == 1) // if QUIT menu item highlighted, draw ">"
            g.drawString(">", x - 30, y);
    }

    // method to draw game screen
    // separated from general draw method for organization
    public void drawGame(Graphics g) {
        int x, y; // use variables to indicate position to draw text -> cleaner code

        paddle.draw(g);
        npc.draw(g);
        ball.draw(g);

        // draw score text
        g.setFont(new Font("Impact", Font.PLAIN, 32));

        // player score
        x = GAME_WIDTH / 2;
        y = 9 * GAME_HEIGHT / 10;
        g.drawString("" + playerScore, x, y);

        // computer score
        x = GAME_WIDTH / 2;
        y = GAME_HEIGHT / 10;
        g.drawString("" + computerScore, x, y);
    }

    // method to draw end screen
    // separated from general draw method for organization
    public void drawEnd(Graphics g) {
        int x, y; // use variables to indicate position to draw text -> cleaner code

        // win or lose message
        g.setFont(new Font("Impact", Font.PLAIN, 32));
        y = GAME_HEIGHT / 3;

        if (gameState == 2) { // player won
            x = GAME_WIDTH / 4;
            g.drawString("Congratulations! You won!", x, y);

        } else if (gameState == -2) { // player lost
            x = 3 * GAME_WIDTH / 8;
            g.drawString("Oops... You lost.", x, y);
        }

        // menu text
        g.setFont(new Font("Verdana", Font.PLAIN, 24));

        // PLAY AGAIN menu item
        x = 10 * GAME_WIDTH / 24;
        y = 2 * GAME_HEIGHT / 3;
        g.drawString("PLAY AGAIN", x, y);
        if (cmd == 0) // if PLAY AGAIN menu item highlighted, draw ">"
            g.drawString(">", x - 30, y);

        // QUIT menu item
        x = 45 * GAME_WIDTH / 96;
        y += 50;
        g.drawString("QUIT", x, y);
        if (cmd == 1) // if QUIT menu item highlighted, draw ">"
            g.drawString(">", x - 30, y);
    }

    // call the draw methods in each class to update positions as things move
    public void draw(Graphics g) {
        // antialiasing text so it appears smoother
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g.setColor(Color.white); // set color to white

        if (gameState == titleState) { // title state -> draw title screen
            drawTitle(g);
        } else if (gameState == playState) { // play state -> draw game screen
            drawGame(g);
        } else if (Math.abs(gameState) == endState) { // end state -> draw end screen
            drawEnd(g);
        }
    }

    // calls move methods in other classes to update positions of objects
    // constantly called from run() -> fluid and more natural movement
    public void move() {
        // only applies to the objects when gameState is playState
        if (gameState == playState) {
            paddle.move();
            npc.move(ball);
            ball.move();
        }
    }

    // handles all collision detection and responds accordingly
    public void checkCollision() {
        // only applies to the objects when gameState is playState
        if (gameState == playState) {
            if (playerScore >= 10) {// playerScore >= 10 -> player wins
                gameState = 2;
                cmd = 0;
            } else if (computerScore >= 10) {// computerScore >= 10 -> player loses
                gameState = -2;
                cmd = 0;
            }

            // stop player paddle if left or right edges hit
            if (paddle.x <= 0) {
                paddle.x = 0;
            }
            if (paddle.x >= GAME_WIDTH - Paddle.PADDLE_LENGTH) {
                paddle.x = GAME_WIDTH - Paddle.PADDLE_LENGTH;
            }

            // stop autopaddle if left or right edges hit
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
            // reset ball and increase computer score if bottom edge hit and paddle not hit
            else if (ball.y >= GAME_HEIGHT - Ball.BALL_DIAMETER) {
                ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                computerScore++;
            }

            // bounce if autopaddle hit
            if (0 <= ball.y - npc.y && ball.y - npc.y <= AutoPaddle.PADDLE_THICKNESS
                    && -Ball.BALL_DIAMETER <= ball.x - npc.x && ball.x - npc.x <= AutoPaddle.PADDLE_LENGTH) {
                ball.flipYDirection();
                ball.y = AutoPaddle.PADDLE_THICKNESS;
            }
            // reset ball and increase player score if top edge hit and autopaddle not hit
            else if (ball.y <= npc.y) {
                ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                playerScore++;
            }
        }
    }

    // run method keeps game running
    // call methods to move objects, check collision, update screen
    public void run() {
        // tick speed
        long lastTime = System.nanoTime();
        double amountOfTicks = 60; // tick speed is 60
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        while (true) { // infinite game loop
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

    // manage key pressed
    @Override
    public void keyPressed(KeyEvent e) {
        // title state -> move menu item cursor
        if (gameState == titleState) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) { // top menu item
                cmd = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) { // bottom menu item
                cmd = 1;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) { // apply menu item
                if (cmd == 0) // start game
                    gameState = playState;
                else if (cmd == 1) // quit
                    System.exit(0);
            }
        }
        // play state -> send key pressed to Paddle class to manage
        else if (gameState == playState)
            paddle.keyPressed(e);
        // end state -> move menu item cursor
        else if (Math.abs(gameState) == endState) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) { // top menu item
                cmd = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) { // bottom menu item
                cmd = 1;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) { // apply menu item
                if (cmd == 0) {// restart game
                    // reset scores to 0
                    playerScore = 0;
                    computerScore = 0;
                    // reset positions of objects
                    ball.reset(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);
                    paddle.x = GAME_WIDTH / 2 - Paddle.PADDLE_LENGTH / 2;
                    paddle.y = GAME_HEIGHT - Paddle.PADDLE_THICKNESS;
                    npc.x = GAME_WIDTH / 2 - AutoPaddle.PADDLE_LENGTH / 2;
                    npc.y = 0;
                    // set game state to play state
                    gameState = playState;
                } else if (cmd == 1) // quit
                    System.exit(0);
            }
        }

        // CHEAT KEYS FOR TESTING PURPOSES
        if (e.getKeyCode() == KeyEvent.VK_8)
            gameState = 2;
        if (e.getKeyCode() == KeyEvent.VK_9)
            gameState = -2;
    }

    // manage key release
    @Override
    public void keyReleased(KeyEvent e) {
        // if play state, send key released to Paddle class to manage
        if (gameState == playState)
            paddle.keyReleased(e);
    }

    // empty b/c not used
    // must be here to be overrided by KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {
    }
}