package heatreegames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class GameScene extends JPanel implements ActionListener {
    protected boolean running, paused;
    protected Timer timer;
    protected static final int SCREEN_WIDTH = 800;
    protected static final int SCREEN_HEIGHT = 800;
    protected static final int UNIT_SIZE = 25;
    protected static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    protected static final int SCREEN_DELAY = 75;
    public GameFrame gf;

    public GameScene(GameFrame gf) 
    {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());
        this.gf = gf;
    }

    // Abstract methods for game-specific behavior
    protected abstract void startGame();
    protected abstract void draw(Graphics g);
    protected abstract void move();
    protected abstract void checkCollisions();
    protected abstract void gameOver(Graphics g);
    public abstract void stopGame();
    
    @Override
    protected void paintComponent(Graphics g)
     {
        super.paintComponent(g);
        if (running) 
        {
            draw(g);
        } 
        else
        {
            gameOver(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (running && !paused) {
            move();
            checkCollisions();
        }
        repaint();
    }

    // Handles key events
    public class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            handleKeyPress(e.getKeyCode());
        }
    }

    // Abstract method for key handling
    protected abstract void handleKeyPress(int keyCode);

    
}
