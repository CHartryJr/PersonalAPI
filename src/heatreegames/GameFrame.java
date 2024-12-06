package heatreegames;

import javax.swing.JFrame;

public class GameFrame extends JFrame 
{
    private GameScene currentScene;

    public GameFrame() 
    {
        this.currentScene = new GameMenu(this);
        this.add(currentScene);
        this.setTitle("Heartree Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public GameScene getGame()
    {
        return currentScene;
    }

    public boolean start() {
        this.setVisible(true);
        return this.isVisible();
    }

    public void switchScene(GameScene scene)
     {
        // Stop current game if it is an AbstractGame instance
        if (currentScene instanceof GameScene) 
        {
            GameScene currentGame = currentScene;
            currentGame.stopGame(); // Ensure AbstractGame has a proper stop method
        }

        // Remove the current scene and set the new one
        this.remove(currentScene);
        this.currentScene = scene;
        this.add(scene);
        scene.requestFocusInWindow();
        // Refresh the frame to display the new scene
        this.revalidate();
        this.repaint();
    }
}
