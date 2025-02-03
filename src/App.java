import heatreegames.GameFrame;
import heatreegames.controllers.ExternalInputAdapter;
import heatreegames.games.SnakeGame;
import Intellegence.neuralnet.Activation;
import Intellegence.neuralnet.NeuralNet;
import java.util.Random;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalTime;

public class App {
    private static final String ASSET_DIR = "/assets/Snake/";
    private static final String BEST_NETWORK_FILE = "BestSnakePlayer";
    private static final String BEST_SCORE_FILE = "BestScore.txt";
    private static final int MUTATION_INTERVAL = 30;
    private static final int SUCCESSFUL_MOVE_THRESHOLD = 10;
    private static final int THREAD_SLEEP_DURATION = 75;
    private static final int FITNESS_INCREMENT = 30;

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
        boolean isNewRecord = false, isNewGame = false, isCleared = false;
        int  applesCollected = 0, highScore, previousScore = 0, currentFitness = 0;
        Random random;
        ExternalInputAdapter aiController = new ExternalInputAdapter();
        String networkPath = workingDirectory + ASSET_DIR + BEST_NETWORK_FILE;
        String scorePath = workingDirectory + ASSET_DIR + BEST_SCORE_FILE;
        NeuralNet neuralNet = loadNetwork(networkPath);
        highScore = loadScore(scorePath);
        
        while (applesCollected < SUCCESSFUL_MOVE_THRESHOLD) 
        {
            LocalTime currentTime = LocalTime.now();

            if (currentTime.getMinute() % 40 == 0 && isCleared) 
            {
                clearConsole();
                System.out.println("New Model Loaded\n");
                NeuralNet tempNet = loadNetwork(networkPath);
                neuralNet = (tempNet.compareTo(neuralNet) > 0) ? tempNet : neuralNet;
                isCleared = false;
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
                    if (!isNewGame) 
                    {
                        isNewRecord = applesCollected > highScore;

                        if (isNewRecord || applesCollected > previousScore) 
                        {
                            if (isNewRecord) 
                            {
                                currentFitness += FITNESS_INCREMENT;
                                saveScore(scorePath, applesCollected);
                                saveBest(networkPath, neuralNet);
                                System.out.println("New High Score! Apples: " + applesCollected);
                            }

                            previousScore = applesCollected;
                            neuralNet.setFitness(neuralNet.getFitness() + currentFitness);
                            saveBest(networkPath, neuralNet);
                        } 
                        else 
                        {
                            neuralNet.setFitness(neuralNet.getFitness() - 1);
                            random = new Random();
                            for(int i = 0; i < MUTATION_INTERVAL;++i)
                            {
                                if(random.nextDouble() > .8d)
                                    neuralNet.mutate();
                                neuralNet.mutate();
                            }
                        }

                        applesCollected = 0;
                        currentFitness = 0;
                        isNewRecord = false;

                        if (currentTime.getMinute() % 10 == 1) 
                        {
                            isCleared = true;
                        }
                    }
                    isNewGame = false;
                    continue;
                }

                game.swapController(aiController);
                double[] gameState = aiController.getEnviroment();

                if (gameState != null) 
                {
                    neuralNet.observe(gameState);
                    neuralNet.forward();
                    aiController.getExternalPrediction(neuralNet.predict());
                    currentFitness += aiController.isTracking() ? 1 : -1;
                    if((gameState[1] == gameState[5]-50 || gameState[2] == gameState[6]-50)  || (gameState[1] == -25 || gameState[2] == -25))
                        currentFitness -= 30;
                    applesCollected = game.getApplesEaten();
                    System.out.printf("\r Current Fitness: %d | Apples Eaten: %d  ", currentFitness, applesCollected);
                    isNewGame = true;
                  
                }
            }
            sleepThread(THREAD_SLEEP_DURATION);
        }
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
        NeuralNet neuralNet = new NeuralNet(7, 1, 4);
        neuralNet.setActivation(Activation.SIGMOID);
        File networkFile = new File(path);

        if (networkFile.exists()) 
        {
            try 
            {
                neuralNet.loadStream(path);
            } catch (Exception e) {
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
            } else {
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
        if (net.compareTo(bestNet) > 0) {
            saveNetwork(net, networkPath);
            System.out.println("\nNew best network saved.");
        }
    }
}