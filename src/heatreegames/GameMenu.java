package heatreegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameMenu extends GameScene 
{
    private List<String> gameNames;
    private int selectedIndex;
    private final String gamesFolder = System.getProperty("user.dir")+"/src/heatreegames/games"; // Adjust path as needed
    private boolean showingCredits = false;

     public GameMenu(GameFrame parentFrame) 
    {
        super(parentFrame);
        this.gameNames = new ArrayList<>();
        this.selectedIndex = 0;

        // Load game names dynamically from the folder
        loadGames();

        // Add Quit and Credits options
        gameNames.add("Quit");
        gameNames.add("Credits");

        // Set up panel properties
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MenuKeyAdapter());
        running = true;
    }

    private void loadGames()
    {
        File folder = new File(gamesFolder);
        
        
        // Ensure the folder exists and is a directory
        if (folder.exists() && folder.isDirectory())
        {
            File[] files = folder.listFiles();
        
            if (files != null)
             {
                for (File file : files) 
                {
                    // Debugging: Print the file names and check if they're directories
                  //  System.out.println("Found file: " + file.getName() + ", isDirectory: " + file.isDirectory());
                    if (!file.isDirectory()) 
                    {
                        gameNames.add(file.getName());  // Add only directories to the list
                    }
                }
            } 
            else 
            {
                System.err.println("Failed to list files in the directory.");
            }
        }
         else 
        {
            System.err.println("Invalid directory: " + gamesFolder);
        }
    }
    
    

    @Override
    public void draw(Graphics g)
     {
        if (showingCredits)
        {
            drawCredits(g);
            return;
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        // Title
        String title = "Select a Game";
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(title, (getWidth() - metrics.stringWidth(title)) / 2, 50);

        // List of games
        for (int i = 0; i < gameNames.size(); i++) 
        {
            if (i == selectedIndex) 
            {
                g.setColor(Color.YELLOW); // Highlight selected
            } 
            else
            {
                g.setColor(Color.WHITE);
            }
            String gameName = gameNames.get(i);
            g.drawString(gameName, 100, 150 + i * 50); // Spaced 50px apart
        }
    }

    private void drawCredits(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));

        // Credits Title
        String title = "Credits";
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(title, (getWidth() - metrics.stringWidth(title)) / 2, 50);

        // Credits Content
        String[] credits = 
        {
            "Developed by: Carl Hartry Jr.",
            "Framework by: Heatree Games",
            "Special Thanks: Bro Code",
            "Press B to return to menu"
        };

        for (int i = 0; i < credits.length; i++) {
            g.drawString(credits[i], 50, 150 + i * 50); // Spaced 50px apart
        }
    }

    public void update() 
    {
        repaint();
    }

    private void handleSelection() 
    {
        if (selectedIndex == gameNames.size() - 2) 
        { // Quit option (second to last)
            System.exit(0);  // Quit the game
        } 
        else if (selectedIndex == gameNames.size() - 1)
         { // Credits option (last)
            showingCredits = true; // Show Credits
            repaint(); // Update screen to show credits
        } 
        else
        {
            startSelectedGame(); // Start selected game
        }
    }

    private void startSelectedGame() 
    {
        String gameName = gameNames.get(selectedIndex);
        String tokens []= gameName.split("\\.");
        gameName = tokens[0];
        String className = "heatreegames.games." + gameName ;
    
        try 
        {
            // Dynamically load the game class using the correct class name
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class<?> gameClass = classLoader.loadClass(className);

    
            // Create an instance of the game scene class
            GameScene selectedGame = (GameScene) gameClass.getDeclaredConstructor(GameFrame.class).newInstance(gf);
    
            // Switch to the selected game scene
            gf.switchScene(selectedGame);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game: " + gameName,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private class MenuKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (showingCredits) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    showingCredits = false; // Return to menu
                    repaint();
                }
                return; // Ignore other keys while in credits view
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (selectedIndex > 0) {
                        selectedIndex--;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (selectedIndex < gameNames.size() - 1) {
                        selectedIndex++;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    handleSelection(); // Perform the selected action
                    break;
                default:
                    break;
            }
            repaint(); // Update the menu view after selection or navigation
        }
    }

    @Override
    protected void startGame() {
        // Not needed in menu
    }

    @Override
    protected void move() {
        // Not needed in menu
    }

    @Override
    protected void checkCollisions() {
        // Not needed in menu
    }

    @Override
    protected void gameOver(Graphics g) {
        // Not needed in menu
    }

    @Override
    protected void handleKeyPress(int keyCode) {
    }

    @Override
    public void stopGame() {
        // Not needed in menu
    }
}
