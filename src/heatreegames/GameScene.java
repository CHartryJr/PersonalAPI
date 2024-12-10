package heatreegames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class GameScene extends JPanel implements ActionListener 
{
    protected boolean running, paused;
    protected Timer timer;
    protected static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 800, UNIT_SIZE = 25, GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE, SCREEN_DELAY = 75;
    protected KeyAdapter controller;
    public GameFrame gf;

    public GameScene(GameFrame gf) 
    {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        controller = new DefaultAdapter();
        this.addKeyListener(controller);
        this.gf = gf;
    }

    public KeyAdapter swapController(KeyAdapter ka) 
    {
        KeyAdapter previous = this.controller;
    
        // Remove existing controller safely
        if (this.controller != null) 
        {
            this.removeKeyListener(this.controller);
        }
    
        // Set the new controller and attach it
        this.controller = ka;
        if (ka != null) 
        {
            this.addKeyListener(ka);
        }
    
        return previous;
    }
    

    // Abstract methods for game-specific behavior
    public abstract double[] getEnviroment();
    
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
        move();
        checkCollisions();
        repaint();
    }


    public void togglePause() 
    {
        paused = !paused;
    
        if (paused) 
        {
            timer.stop();
        } 
        else 
        {
            timer.start();
        }
    }
    
    protected void handlePauseKey(int keyCode) 
    {
        if (keyCode == KeyEvent.VK_P) 
        {
            togglePause();
        }
    }
    
    class DefaultAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) 
        {
            handleKeyPress(e.getKeyCode());
        }
    }

    synchronized public boolean gameIsRunning()
    {
        return running;
    }

    protected abstract void handleKeyPress(int keyCode);
}
