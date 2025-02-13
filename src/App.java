import heatreegames.GameFrame;
import heatreegames.controllers.ExternalInputAdapter;
import heatreegames.games.SnakeGame;
import Intellegence.neuralnet.Activation;
import Intellegence.neuralnet.NeuralNet;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class App 
{
    private static final String ASSET_DIR = "/assets/Snake/";
    private static final String BEST_NETWORK_FILE = "BestSnakePlayer";
    private static final String BEST_SCORE_FILE = "BestScore.txt";
    private static final int MUTATION_INTERVAL = 1;
    private static final int SUCCESSFUL_MOVE_THRESHOLD = 10;
    private static final int THREAD_SLEEP_DURATION = 75;
    private static final int FITNESS_INCREMENT = 20;
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static Random rand;

    public static void main(String[] args) 
    {
        String currentWorkingDirectory = System.getProperty("user.dir");

        SwingUtilities.invokeLater(() -> 
        {
            GameFrame gameFrame = new GameFrame();
            gameFrame.start();

            Thread trainerThread = new Thread(() -> trainSnakeAI(gameFrame, currentWorkingDirectory));
            trainerThread.start();
        });
    }

    private static void trainSnakeAI(GameFrame gameFrame, String workingDirectory) 
    {
        boolean isNewRecord = false, isNewGame = false;
        int applesCollected = 0, highScore, previousScore = 0, currentFitness = 0,gameCounter = 1000;
        ExternalInputAdapter aiController = new ExternalInputAdapter();
        String networkPath = workingDirectory + ASSET_DIR + BEST_NETWORK_FILE;
        String scorePath = workingDirectory + ASSET_DIR + BEST_SCORE_FILE;
        long duration =0, startTime =0, endTime;

        NeuralNet neuralNet = loadNetwork(networkPath);
        rand = new Random(System.currentTimeMillis());
        highScore = loadScore(scorePath);
        neuralNet.setActivation(Activation.RECTIFIED_LINEAR_UNIT);
        neuralNet.setLearningRate(1E-2);
       
        while (applesCollected < SUCCESSFUL_MOVE_THRESHOLD) 
        {
            if (gameCounter == 0) 
            {
                clearConsole();
                System.out.println("New Model Loaded\n");
                lock.writeLock().lock();
                try {
                    NeuralNet tempNet = loadNetwork(networkPath);
                    neuralNet = (tempNet.compareTo(neuralNet) > 0) ? tempNet : neuralNet;
                } finally {
                    lock.writeLock().unlock();
                }
                gameCounter = 1000;
            }

            synchronized (gameFrame.getTreeLock()) 
            {
                if (!(gameFrame.getGame() instanceof SnakeGame)) 
                {
                    continue;
                }

                SnakeGame game = (SnakeGame) gameFrame.getGame();

                if (!game.gameIsRunning()) 
                {
                    if (isNewGame) // Evaluate play at the end of the game
                    {
                        isNewRecord = applesCollected > highScore;
                        endTime = System.currentTimeMillis();
                        if(duration > (endTime - startTime))
                        {
                            duration = (endTime - startTime);
                            currentFitness += (FITNESS_INCREMENT/3);
                        }
                        
                        if (isNewRecord == true || applesCollected > previousScore) 
                        {
                            lock.writeLock().lock();
                            try 
                            {
                                currentFitness += FITNESS_INCREMENT;
                                if (isNewRecord == true) 
                                {
                                    saveScore(scorePath, applesCollected);
                                    saveBest(networkPath, neuralNet);
                                    System.out.println(String.format("New High Score! Apples: %d with Fitness %d" , applesCollected,(int)neuralNet.getFitness()));
                                }
                                previousScore = Math.max(applesCollected, previousScore);
                                neuralNet.setFitness((neuralNet.getFitness() * 0.95 + currentFitness));
                                saveBest(networkPath, neuralNet);
                            } 
                            finally 
                            {
                                lock.writeLock().unlock();
                            }
                        } 
                        else 
                        {
                            lock.writeLock().lock();
                            try 
                            {
                                neuralNet.setFitness((neuralNet.getFitness() * 0.95 + currentFitness));
                                adaptiveMutation(neuralNet);
                            } 
                            finally 
                            {
                                lock.writeLock().unlock();
                            }
                            gameCounter -= 1;
                        }
                        applesCollected = 0;
                        currentFitness = 0;
                        isNewRecord = false;
                        rand = new Random(System.currentTimeMillis());
                    }
                    startTime = System.currentTimeMillis();
                    isNewGame = false;
                    continue;
                }
                game.swapController(aiController);
                double[] gameState = aiController.getEnviroment();
                isNewGame = true;
                if (gameState != null) 
                {
                    
                    boolean isBound  = isBound(gameState);
                    boolean isTracking = aiController.isTracking();
                    lock.readLock().lock();
                    try {
                        neuralNet.observe(gameState);
                        neuralNet.forward();
                        double [] aiPredict = exploration(neuralNet);
                        aiController.getExternalPrediction(aiPredict);
                        //System.out.print(String.format("\r %s", neuralNet));
                    } 
                    finally 
                    {
                        lock.readLock().unlock();
                    }
                    
                    currentFitness += isTracking ? (.06666667 * FITNESS_INCREMENT) : -(.1 * FITNESS_INCREMENT);
                    if (isBound) // If out of bounds
                        currentFitness -= (FITNESS_INCREMENT/3);

                    applesCollected = game.getApplesEaten();
                }
            }
            sleepThread(THREAD_SLEEP_DURATION);
        }
    }

    private static double[] exploration (NeuralNet net)
    {
        if(net.getFitness() > 1 )
            return net.predict();

        
        if (rand.nextDouble() < 0.1) // 10% chance to explore
        {
            return new double[] { rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble() };
        } 
        else 
        {
            return net.predict();
        }
    }
                    
    private static boolean isBound(double[] gameState) 
    {
        double x = gameState[1]; // Snake's X position
        double y = gameState[2]; // Snake's Y position
        double gameWidth = gameState[5]-25;//game width
        double gameHeight = gameState[6]-25;//game height
        double snakeSize = 25; // Assuming each movement step is 25 pixels
    
        return (x < 0 || x + snakeSize >= gameWidth || 
                y < 0 || y + snakeSize >= gameHeight);
    }
    
                
    private static int loadScore(String filePath) 
    {
        Path path = Paths.get(filePath);
        try 
        {
            if (Files.notExists(path)) 
            {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "0", StandardOpenOption.CREATE);
                return 0;
            }
            return Integer.parseInt(Files.readString(path).trim());
        } 
        catch (IOException | NumberFormatException e) 
        {
            System.err.println("Error loading score: " + e.getMessage());
            return 0;
        }
    }

    private static void saveScore(String filePath, int score) 
    {
        try 
        {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, String.valueOf(score), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } 
        catch (IOException e) 
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
            if (net == null) 
            {
                throw new IllegalArgumentException("No network to save");
            }
            net.saveStream(path);
        } 
        catch (Exception e) 
        {
            System.err.println("Failed to save neural network: " + e.getMessage());
        }
    }

    private static NeuralNet loadNetwork(String path) 
    {
        NeuralNet neuralNet = new NeuralNet(8, 2, 4);
        neuralNet.setActivation(Activation.SIGMOID);
        File networkFile = new File(path);

        if (networkFile.exists()) 
        {
            try 
            {
                neuralNet.loadStream(path);
            } 
            catch (Exception e) 
            {
                System.err.println("Failed to load neural network from " + path + ": " + e.getMessage());
            }
        }
        return neuralNet;
    }

    private static void clearConsole() 
    {
        try 
        {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) 
            {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } 
            else 
            {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Error clearing console: " + e.getMessage());
        }
    }

    private static void saveBest(String networkPath, NeuralNet net) 
    {
        NeuralNet bestNet = loadNetwork(networkPath);
        
        if (net.compareTo(bestNet) > 0 || rand.nextDouble() < 0.05) 
        {
            saveNetwork(net, networkPath);
            System.out.println("\nNew best network saved.");
        }
    }


    private static void adaptiveMutation(NeuralNet neuralNet) 
    {
       
        double mutationRate =  Math.exp(-Math.abs(neuralNet.getFitness()) / 50.0);; 

        int mutationAttempts = (int) (MUTATION_INTERVAL * (1 + Math.abs(neuralNet.getFitness()) / 100.0));

        for (int i = 0; i < mutationAttempts; ++i) 
        {
            if (rand.nextDouble() < mutationRate) 
            {
                neuralNet.mutate();
            }
        }
    }

   
}