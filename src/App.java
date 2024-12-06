import heatreegames.GameFrame;
import heatreegames.controllers.MLAdapter;
import heatreegames.games.SnakeGame;
import intellegence.neuralnet.NeuralNet;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import java.io.File;

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
            Thread trainer = new Thread(() ->  trainSnakeAI(gf, currentWorkingDirectory));
            trainer.start();
        });
    }

    private static void trainSnakeAI(GameFrame gf, String workingDir) 
    {
        int epoch = 0;
        int maxEpoch = 1_000_000_000;
        int successfulMoves = 0;
        MLAdapter mlAdapter = new MLAdapter();
        NeuralNet net = laodNetwork( workingDir);
        mlAdapter.swapEncephalon(net);
        

        while (epoch < maxEpoch && successfulMoves < 30) 
        {
            synchronized (gf.getTreeLock()) 
            {
                if (!(gf.getGame() instanceof SnakeGame)) 
                {
                    continue; // Wait for SnakeGame to start
                }

                SnakeGame game = (SnakeGame) gf.getGame();
                game.swapController(mlAdapter);

                double[] environment = game.getEnviroment();
                net.observe(environment);

                // Calculate move prediction
                int predictedMove = predictMove(environment);
                int neuralMove = (int) net.predict()[0];

                if (neuralMove != predictedMove) 
                {
                    net.forward();
                    System.out.print("\r"+net);
                    net.train(0, new double[]{predictedMove});
                    if (epoch % 200_000 == 0 && successfulMoves < 15) 
                    {
                        mlAdapter.swapEncephalon(net);
                        net.mutate();
                    }
                } 
                else 
                {
                    successfulMoves++;
                    if (successfulMoves == 30) 
                    {
                        saveNetwork(net, workingDir + "/assets/BestSnakePlayer");
                        break;
                    }
                }

                // Fitness adjustment
                if (environment[1] == environment[3] && environment[2] == environment[4]) 
                {
                    net.setFitness(net.getFitness() + 1);
                    saveNetwork(net, workingDir + "/assets/BestSnakePlayer");
                }
            }

            try 
            {
                Thread.sleep(500); // Adjust as per gameplay speed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Training thread interrupted: " + e.getMessage());
                return;
            }
            
            epoch++;
        }
    }

    private static int predictMove(double[] env) 
    {
        int xDiff = (int) Math.abs(env[1] - env[3]);
        int yDiff = (int) Math.abs(env[2] - env[4]);

        if (xDiff < yDiff && xDiff != 0) 
        {
            return env[1] > env[3] ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT;
        } 
        else 
        {
            return env[2] > env[4] ? KeyEvent.VK_DOWN : KeyEvent.VK_UP;
        }
    }

    private static void saveNetwork(NeuralNet net, String path) 
    {
        try 
        {
            net.saveStream(path);
        } 
        catch (Exception e)
        {
            System.err.println("Failed to save neural network: " + e.getMessage());
        }
    }

    private static NeuralNet laodNetwork( String path) 
    {
        NeuralNet nn = new NeuralNet(5, 1, 1);
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
            System.err.println("Failed to save neural network: " + e.getMessage());
        }
        return nn;
    }
}