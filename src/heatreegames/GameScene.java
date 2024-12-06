package heatreegames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import heatreegames.controllers.*;

public abstract class GameScene extends JPanel implements ActionListener 
{
    protected boolean running, paused;
    protected Timer timer;
    protected static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 800, UNIT_SIZE = 25, GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE, SCREEN_DELAY = 75;
    protected Adapter controller;
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

    public KeyAdapter swapController(Adapter ka) 
    {
        KeyAdapter temp = controller;
        this.removeKeyListener(controller);
        controller = ka;
        this.addKeyListener(ka);
        return temp;
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
        int input = controller.getInput(); // Get key press input
        
        if (input != -1) 
        {
            handleKeyPress(input); // Handle key press if any
        }

        if (running && !paused) 
        {
            move();
            checkCollisions();
        }

        repaint(); // Redraw the game screen
    }

    protected abstract void handleKeyPress(int keyCode);
}
