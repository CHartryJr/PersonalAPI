import heatreegames.GameFrame;
import heatreegames.controllers.MLSnakeAdapter;
import heatreegames.games.SnakeGame;
import Intellegence.neuralnet.Activation;
import Intellegence.neuralnet.NeuralNet;

import java.awt.event.KeyEvent;

import javax.management.RuntimeErrorException;
import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Random;

public class App 
{
    private static final int SUCCESSFUL_MOVE_THRESHOLD = 1000;
    private static final int MUTATION_INTERVAL = 200;
    private static NeuralNet net; 
    private static boolean eatenApple;
        

      public static void main(String[] args)
     {
        String currentWorkingDirectory = System.getProperty("user.dir");
        net = loadNetwork(currentWorkingDirectory);
        // Create and start the game frame
        SwingUtilities.invokeLater(() -> 
        {
            GameFrame gf = new GameFrame();
            gf.start();
            Thread trainer = new Thread(() ->  trainSnakeAI(gf,currentWorkingDirectory));
            trainer.start();
        });
    }

    private static void trainSnakeAI(GameFrame gf, String workingDir) 
    {
        int epoch = 0, successfulMoves = 0, applesCollected = 0;
        MLSnakeAdapter mlAdapter = new MLSnakeAdapter();
         eatenApple = false;
        workingDir += "/assets/BestSnakePlayer";
    
        while (successfulMoves < SUCCESSFUL_MOVE_THRESHOLD) 
        {
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
                    successfulMoves = 0;
                    continue; // Ensure game is running
                }
    
                game.swapController(mlAdapter);
                double[] environment = game.getEnviroment();
                net.forward();
    
                int predictedMove = predictMove(environment);
                int neuralMove = mlAdapter.getInput();
                            
                                        if (neuralMove != predictedMove) 
                                        {
                                            net.train(0, mapToNet(predictedMove));
                            
                                            if (epoch % MUTATION_INTERVAL == 0 && successfulMoves < SUCCESSFUL_MOVE_THRESHOLD / 2 && eatenApple) 
                                            {
                                                mlAdapter.swapEncephalon(net);
                                                net.mutate();
                                            }
                            
                                        // logTrainingProgress(environment);
                                        } 
                                        else 
                                        {
                                            successfulMoves++;
                                            System.out.println(String.format("made %d succesful moves", successfulMoves));
                                            if (successfulMoves == SUCCESSFUL_MOVE_THRESHOLD) 
                                            {
                                                saveBest(workingDir);
                                                break;
                                            }
                                        }
                            
                                        eatenApple = applesCollected < game.getApplesEaten();
                                        if (eatenApple) 
                                        {
                                            saveBest(workingDir);
                                            applesCollected = game.getApplesEaten();
                                        }
                                    }
                            
                                    epoch++;
                                    resetEatenAppleFlag(epoch);
                                    sleepThread(75);
                        }
                    }
                    
                    private static void logTrainingProgress(double[] environment) {
                        System.out.print("\rNeural Network: " + net + "\nGame Environment: ");
                        for (double x : environment) System.out.print(x + " ");
                        System.out.println("\nObserved: ");
                        for (double x : net.getCurrentInput()) System.out.print(x + " ");
                        System.out.println();
                    }
                    
                    private static void resetEatenAppleFlag(int epoch) {
                        if (epoch % 32 == 0) {
                            eatenApple = false;
                        }
                    }
                    
                    private static void sleepThread(int millis) {
                        try {
                            Thread.sleep(millis);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("Training thread interrupted: " + e.getMessage());
                        }
                    }
                    
                
                    private static int predictMove(double[]env) 
    {
        final int KEY_CODE_RANGE = 4;
        Random random = new Random();
        if (env != null) 
        {
           
                int xDiff = (int)Math.abs(env[1] - env[3]); // Snake head X - Apple X
                int yDiff = (int)Math.abs(env[2] - env[4]); // Snake head Y - Apple Y
                int nextMove= -1,opp_mov = 1;
    
                if (xDiff == 0) 
                {
                    nextMove = (env[2] > env[4]) ?KeyEvent.VK_UP : KeyEvent.VK_DOWN;
                } 
                else if (yDiff == 0) 
                {
                    nextMove = (env[1] > env[3]) ? KeyEvent.VK_LEFT  : KeyEvent.VK_RIGHT;
                } 
                else if (xDiff > yDiff) 
                {
                    nextMove = (env[1] > env[3]) ? KeyEvent.VK_LEFT  : KeyEvent.VK_RIGHT;
                } 
                else 
                {
                    nextMove = (env[2] > env[4]) ? KeyEvent.VK_UP : KeyEvent.VK_DOWN;
                }

                
                if (nextMove == opp_mov) 
                {
                    
                    nextMove = (xDiff <= yDiff) ? ((env[1] > env[3]) ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT)
                                                : ((env[2] > env[4]) ? KeyEvent.VK_UP :KeyEvent.VK_DOWN);
                }
    
                opp_mov = getOppositeMove(nextMove);
                return nextMove;
        }
        return random.nextInt(KEY_CODE_RANGE) +KeyEvent.VK_LEFT ;
    }
    

    private static int getOppositeMove(int move) {
        return switch (move) {
            case KeyEvent.VK_LEFT -> KeyEvent.VK_RIGHT;
            case KeyEvent.VK_RIGHT -> KeyEvent.VK_LEFT ;
            case KeyEvent.VK_UP -> KeyEvent.VK_DOWN;
            case KeyEvent.VK_DOWN ->KeyEvent.VK_UP;
            default -> -1;
        };
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

    static private double[] mapToNet(int index)
    {
        double [] mappedVectors = index == 37 ? new double[]{1,0,0,0}: index == 38 ?  new double[] {0,1,0,0} : index == 39 ? new double[] {0,0,1,0} : new double[] {0,0,0,1} ;// L , U, R ,D
        return mappedVectors ; 
    }

    public static void clearConsole() {
        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveBest(String workingDir)
    {
        NeuralNet temp = loadNetwork(workingDir);
        
        if(net.compareTo(temp) >= 0 )
        {
            net.setFitness(net.getFitness() + 1);
            saveNetwork(net, workingDir );
        }
        System.out.println("new Winner has been saved");

    }

}