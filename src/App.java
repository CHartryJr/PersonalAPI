import heatreegames.GameFrame;
import heatreegames.controllers.ExternalInputAdapter;
import heatreegames.games.SnakeGame;
import Intellegence.neuralnet.Activation;
import Intellegence.neuralnet.NeuralNet;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;


public class App 
{
    public static void main(String[] args)
    {
      
        String currentWorkingDirectory = System.getProperty("user.dir");
        // Create and start the game frame
        SwingUtilities.invokeLater(() -> 
        {
            GameFrame gf = new GameFrame();
            gf.start();
            Thread trainer = new Thread(() ->  trainSnakeAI(gf,currentWorkingDirectory));
            trainer.start();
        });
    }

    private static void trainSnakeAI(GameFrame gf, String cwd) 
    {
        Boolean newRecord = false, newGame = false,cleared =false;
        final int MUTATION_INTERVAL = 20, SUCCESSFUL_MOVE_THRESHOLD = 10;
        int epoch = 0, applesCollectedInGame = 0, hightScore = 0,prevScore = 0,curFitness =0;
        ExternalInputAdapter mlAdapter = new ExternalInputAdapter();
        final String locToNet ="/assets/Snake/BestSnakePlayer";
        String netDir = cwd + locToNet, savedScore = cwd + "/assets/Snake/BestScore.txt";
        NeuralNet net = loadNetwork(locToNet);
        hightScore = loadScore(savedScore);
        LocalTime runTime;
        double [] gameEnv; 
            
        while (applesCollectedInGame < SUCCESSFUL_MOVE_THRESHOLD) 
        {
            runTime = LocalTime.now();
            if(runTime.getMinute() %10 == 0 && cleared )
            {
                clearConsole();
                System.out.println("New Model Is Set \n");
                NeuralNet temp = loadNetwork(netDir);
                net = temp.compareTo(net) > 0 ? temp : net;
                cleared =!cleared;
            }

            synchronized (gf.getTreeLock()) 
            {
                if (!(gf.getGame() instanceof SnakeGame)) 
                {
                    continue; // Wait for SnakeGame to start
                }

                SnakeGame game = (SnakeGame) gf.getGame();

                if (!game.gameIsRunning()) 
                {
                    if(!newGame)
                    {
                        newRecord =  applesCollectedInGame > hightScore;
                        if( newRecord == true  || (applesCollectedInGame > prevScore) )
                        {
                            if (newRecord) 
                            {
                                curFitness += 30;
                                saveScore(savedScore,applesCollectedInGame);
                                saveBest(netDir,net);
                                System.out.println("New Score obtained Apples : "+ applesCollectedInGame);
                            }
                            prevScore = applesCollectedInGame;
                            net.setFitness(net.getFitness() + curFitness);
                            saveBest(netDir,net);
        
                        }
                        else
                            net.setFitness(net.getFitness() -1);

                        // reset in game chages
                        applesCollectedInGame = 0;
                        curFitness = 0;
                        newRecord = false;
                        if(runTime.getMinute() %10 == 1 )
                            cleared = true;
                        
                    }
                    newGame = false;
                    
                    continue; 
                }
                else
                {
                    game.swapController(mlAdapter);
                    gameEnv = mlAdapter.getEnviroment();

                    if(gameEnv != null)
                    {
                        net.observe(mlAdapter.getEnviroment());
                        net.forward();
                        mlAdapter.getExternalPrediction(net.predict()); 

                        if(mlAdapter.isTracking())
                            curFitness +=1;
                        else
                            curFitness -= 1;

                        if( (epoch % MUTATION_INTERVAL == 1) )
                            net.mutate();

                        applesCollectedInGame = game.getApplesEaten();
                        System.out.print(String.format("\r Current Fitness %d Current Apples Eaten During Game %d |ST|" ,curFitness,applesCollectedInGame) );
                        newGame = true;
                        ++epoch;
                        
                    }
                    
                }
            }
            
            sleepThread(75);
        }
    }

    private static int loadScore(String filePath) 
    {
        Path path = Paths.get(filePath);
        try {
            if (Files.notExists(path)) 
            {
                Files.createDirectories(path.getParent()); // Ensure parent directory exists
                Files.writeString(path, "0", StandardOpenOption.CREATE);
                return 0;
            }
            return Integer.parseInt(Files.readString(path).trim());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading score: " + e.getMessage());
            return 0;
        }
    }
    
    private static void saveScore(String filePath, int score) 
    {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent()); // Ensure parent directory exists
            Files.writeString(path, String.valueOf(score),
                              StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) 
        {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }
    
                    
    private static void sleepThread(int millis) 
    {
        try 
        {
            Thread.sleep(millis);
        }
            catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
            System.err.println("Training thread interrupted: " + e.getMessage());
        }
    }
                        
    private static void saveNetwork(NeuralNet net, String path) 
    {
        try 
        {
            if(net == null)
                throw new Exception("No Network to save");
            net.saveStream(path);
        } 
        catch (Exception e)
        {
            System.err.println("Failed to save neural network: " + e.getMessage());
        }
    }

    private static NeuralNet loadNetwork( String path) 
    {
        NeuralNet nn = new NeuralNet(7, 2, 4);
        nn.setActivation(Activation.SIGMOID);
        try 
        {
            if(new File(path ).exists())
            {
                nn.loadStream(path);
                return nn;
            }
            else
                return nn;

        } 
        catch (Exception e)
        {
            System.err.println("Failed to load neural network from " + path + ": " + e.getMessage());
        }
        return nn;
    }

    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.err.println("Error clearing console: " + e.getMessage());
        }
    }
    
    private static void saveBest(String workingDir, NeuralNet net) 
    {
        NeuralNet temp = loadNetwork(workingDir);
        if (net.compareTo(temp) > 0) {
            saveNetwork(net, workingDir);
            System.out.println("\nNew best network saved.");
        }
    }
    
}