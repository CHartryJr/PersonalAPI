package heatreegames;

import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * will be used a super class for all games 
 * 
 */
public abstract class GamePanel extends JPanel implements ActionListener
{
    private String name;
    
    public abstract void StartGame();
   
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    } 

}
