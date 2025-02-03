import heatreegames.GameFrame;
import heatreegames.controllers.MLSnakeAdapter;
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
    public static LocalTime startTime;
    public static void main(String[] args)
    {
        startTime = LocalTime.now();
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
        Boolean newRecord = false,newGame = false;
        final int MUTATION_INTERVAL = 20, SUCCESSFUL_MOVE_THRESHOLD = 10;
        int epoch = 0, applesCollectedInGame = 0, totalApplesCollected = 0, hightScore = 0,prevScore = 0;
        MLSnakeAdapter mlAdapter = new MLSnakeAdapter();
        final String locToNet ="/assets/Snake/BestSnakePlayer";
        String netDir = cwd + locToNet, savedScore = cwd + "/assets/Snake/BestSc ore.txt";
        NeuralNet net = null;
        hightScore = loadScore(savedScore);
        LocalTime runTime;
            
        while (applesCollectedInGame < SUCCESSFUL_MOVE_THRESHOLD) 
        {
            runTime = LocalTime.now();
            if(Math.abs(runTime.getHour() - startTime.getHour()) % 2 == 0)
                net = loadNetwork(netDir);
            mlAdapter.swapEncephalon(net);

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
                        if((applesCollectedInGame > hightScore) || (applesCollectedInGame > prevScore) )
                        {
                            net.setFitness(net.getFitness() +1);
                            saveBest(netDir,net);
                            //System.out.println("made it");
                        }
                        else
                            net.setFitness(net.getFitness() -1);

                        prevScore = applesCollectedInGame  > prevScore ? applesCollectedInGame  : prevScore;
                        applesCollectedInGame = 0;
                        newRecord = false;
                    }
                    newGame = false;
                    continue; 
                }
                else
                {
                    game.swapController(mlAdapter);
                    net.forward();

                    if(mlAdapter.isTracking())
                        net.setFitness(net.getFitness() + 1);
                    else
                        net.setFitness(net.getFitness() - 1);

                    if( (epoch % MUTATION_INTERVAL == 1) )
                        net.mutate();

                    newRecord =  applesCollectedInGame > totalApplesCollected;
                    if (newRecord) 
                    {
                        net.setFitness(net.getFitness() + 10);
                        saveScore(savedScore,applesCollectedInGame);
                        saveBest(netDir,net);
                        totalApplesCollected = game.getApplesEaten();
                    }
                
                    newGame = true;
                    ++epoch;
                }
            }
            sleepThread(75);
        }
    }

    private static int loadScore(String filePath) {
        Path path = Paths.get(filePath);
        try {
            if (Files.notExists(path)) {
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
    
    private static void saveScore(String filePath, int score) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent()); // Ensure parent directory exists
            Files.writeString(path, String.valueOf(score),
                              StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
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
            System.out.println("\nN ew best network saved.");
        }
    }
    
}