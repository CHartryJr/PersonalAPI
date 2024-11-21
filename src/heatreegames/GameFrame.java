package heatreegames;
import javax.swing.JFrame;
import heatreegames.games.snake.Snake;

public class GameFrame extends JFrame
{
    private  Snake currentGame;
    public GameFrame ()
    {
        currentGame = new Snake();
        this.add(currentGame);
        this.setTitle("Heartree Game -"+ currentGame.getName());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this. pack();
        this.setLocationRelativeTo(null);
    }
    public boolean start()
    {
        this.setVisible(true);
        return this.isVisible();
    }
}
