package heatreegames.games;

import heatreegames.GameFrame;
import heatreegames.GameMenu;
import heatreegames.GameScene;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.Timer;


public class SnakeGame extends GameScene 
{
    private char dir;
    private int body, applesEaten, appleLocx, appleLocy;
    private final int[] x, y;
    private Random random;

    public SnakeGame(GameFrame menu) 
    {
        super(menu);
        this.x = new int[GAME_UNITS];
        this.y = new int[GAME_UNITS];
        this.random = new Random();
        this.dir = 'R';
        startGame();
    }

    @Override
    protected void startGame() 
    {
        body = 6;
        applesEaten = 0;
        dir = 'R';
        for (int i = 0; i < body; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        spawnApple();
        running = true;
        paused = false;
        timer = new Timer(SCREEN_DELAY, this);
        timer.start();
    }

    private void spawnApple() {
        appleLocx = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleLocy = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    @Override
    protected void draw(Graphics g) {
        // Draw grid
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        // Draw apple
        g.setColor(Color.RED);
        g.fillOval(appleLocx, appleLocy, UNIT_SIZE, UNIT_SIZE);

        // Draw snake
        for (int i = 0; i < body; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Display score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    protected void move() {
        for (int i = body; i > 0; i--) 
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (dir) 
        {
            case 'R' -> x[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
        }
    }

    @Override
    protected void checkCollisions() {
        // Check self-collision
        for (int i = body; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // Check wall collisions
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        // Check apple collision
        if (x[0] == appleLocx && y[0] == appleLocy) {
            body++;
            applesEaten++;
            spawnApple();
        }

        if (!running) {
            timer.stop();
        }
    }

    @Override
    protected void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, UNIT_SIZE * 4));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setFont(new Font("Ink Free", Font.PLAIN, UNIT_SIZE * 2));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Press J to restart", (SCREEN_WIDTH - metrics.stringWidth("Press J to restart")) / 2, SCREEN_HEIGHT / 2 + UNIT_SIZE * 4);
        g.drawString("Press ESC to return to menu", (SCREEN_WIDTH - metrics.stringWidth("Press ESC to return to menu")) / 2, SCREEN_HEIGHT / 2 + UNIT_SIZE * 6);
    }

    @Override
    protected void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> {
                if (dir != 'R') dir = 'L';
            }
            case KeyEvent.VK_RIGHT -> {
                if (dir != 'L') dir = 'R';
            }
            case KeyEvent.VK_UP -> {
                if (dir != 'D') dir = 'U';
            }
            case KeyEvent.VK_DOWN -> {
                if (dir != 'U') dir = 'D';
            }
            case KeyEvent.VK_P -> {
                paused = !paused;
                if (paused) timer.stop();
                else timer.start();
            }
            case KeyEvent.VK_J -> {
                if (!running) startGame();
            }
            case KeyEvent.VK_ESCAPE-> {
                stopGame(); // Stop the game before returning to the menu
                this.gf.switchScene(new GameMenu(gf)); // Switch to the GameMenu scene
            }
        }
    }

    @Override
    public void stopGame()
     {
        // Stop the game
        running = false;
        if (timer != null) 
        {
            timer.stop();
        }
        // Additional cleanup logic can go here if needed
    }
}
