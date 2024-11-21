package heatreegames.games.snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Snake extends JPanel implements ActionListener
{
    private char dir;
    private boolean running, paused;
    private int body, applesEaten, appleLocx, appleLocy;
    private static final int SCREEEN_WIDTH = 700,SCREEEN_HEIGHT = 700, UNIT_SIZE = 25, GAME_UNITS = (SCREEEN_HEIGHT * SCREEEN_WIDTH)/UNIT_SIZE, SCREEN_DELAY = 75;
    private final int[] x ,y;
    private String name;
    private Random random;
    private Timer timer;

   
    /**
     * @param dir
     * @param running
     * @param body
     * @param name
     * @param random
     */
    public Snake()
     {
        super();
        this.x = new int[GAME_UNITS];
        this.y = new int [GAME_UNITS];
        this.dir = 'R';
        this.running = false;
        this.body = 6;
        this.name = "Snake";
        this.random = new Random();
        this.setPreferredSize(new Dimension(SCREEEN_WIDTH,SCREEEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();
    }

    public class myKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode()) 
            {
                case KeyEvent.VK_LEFT:
                    if(dir != 'R')
                         dir ='L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(dir != 'L')
                         dir ='R';
                    break;
                case KeyEvent.VK_UP:
                    if(dir != 'D')
                         dir ='U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(dir != 'U')
                         dir ='D';
                    break;
                case KeyEvent.VK_P:
                    if(running)
                    {
                        paused = !paused;
                        if (paused) {
                            timer.stop();
                        } else {
                            timer.start();
                        }
                    }
                    break;
                case KeyEvent.VK_J:
                    if (!running)
                        startGame();
                    break;
                default:
                    break;
            }
        }
    }

    public void newApple()
    {
        appleLocx = random.nextInt(SCREEEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleLocy = random.nextInt(SCREEEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        if(running)
        {
            for(int i = 0; i < SCREEEN_HEIGHT/UNIT_SIZE; ++i)
            {
                g.drawLine(i*UNIT_SIZE , 0, i*UNIT_SIZE, SCREEEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEEN_WIDTH , i*UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleLocx,appleLocy,UNIT_SIZE,UNIT_SIZE);

            for(int i = 0; i < body;++i)
            {
                if(i == 0)
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else
                {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metric = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten+" Apples", (SCREEEN_WIDTH - metric.stringWidth("Score: "+applesEaten+" Apples")) / 2, g.getFont().getSize());
            
        }
        else
        {
            gameOver(g);
        }
    }

    public void move()
    {
        for(int i = body; i > 0; --i)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (dir) 
        {
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
        
            default:
                break;
        }

    }

    public void checkApple()
    {
        if((x[0] == appleLocx) && (y[0] == appleLocy))
        {
            ++body;
            ++applesEaten;
            newApple();
        }

    }

    public void checkCollison()
    {
        for(int i = body; i > 0; --i)
        {
            //touch head with body
            if(x[0]==x[i] && y[0] ==y[i])
            {
                running = false;
            }
            // check head touch border
            if(x[0]< 0 )
            {
                running = false;
                
            }

            if (x[0] > SCREEEN_WIDTH) 
            {
                running = false;
                
            }

            if(y[0]< 0 )
            {
                running = false;
              
            }

            if( y[0] > SCREEEN_HEIGHT )
            {
                running = false;
                
            }

            if(!running)
            {
                timer.stop();
            }

        }
    
    }

    public void gameOver(Graphics g)
    {
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,UNIT_SIZE*4));
        FontMetrics metric = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEEN_WIDTH - metric.stringWidth("Game Over")) / 2, SCREEEN_HEIGHT / 2);
        g.setFont(new Font("Ink Free", Font.PLAIN, UNIT_SIZE * 2));
        metric = getFontMetrics(g.getFont());
        g.drawString("Press J to restart", (SCREEEN_WIDTH - metric.stringWidth("Press J to restart")) / 2, SCREEEN_HEIGHT / 2 + UNIT_SIZE * 4);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(running)
        {
            move();
            checkApple();
            checkApple();
            checkCollison();
        }
        repaint();
    }
        
    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) 
    {
        this.name = name;
    } 

    private  void startGame() 
    {
        body = 6;
        applesEaten = 0;
        dir = 'R';
        for (int i = 0; i < body; ++i) {
            x[i] = 0;
            y[i] = 0;
        }
        newApple();
        running = true;
        paused = false;
        timer = new Timer(SCREEN_DELAY, this);
        timer.start();
    }
}