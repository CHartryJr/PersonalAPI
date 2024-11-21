package heatreegames;

import javax.swing.JFrame;



public class GameFrame extends JFrame
{
    private  GamePanel currentGame;
    public GameFrame ()
    {
        currentGame = new GamePanel();
        this.add(currentGame);
        this.setTitle("Heartree Game -"+ currentGame.getName());
        this.
    }
}
