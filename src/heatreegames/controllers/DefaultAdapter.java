package heatreegames.controllers;

import java.awt.event.KeyEvent;

public class DefaultAdapter extends  Adapter
{
    private int currentKeyPress  = -1;

    @Override
    public void keyPressed(KeyEvent e) 
    {
        currentKeyPress = e.getKeyCode();
    }

    @Override
    public  int getInput()
    {
       return currentKeyPress;
    }
    
    @Override
    public void keyReleased(KeyEvent e) 
    {
        currentKeyPress = -1; // Reset key press when released
    }

}
