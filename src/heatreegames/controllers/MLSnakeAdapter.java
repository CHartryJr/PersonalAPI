package heatreegames.controllers;

import Intellegence.Encephalon;
import java.awt.event.KeyAdapter;
import java.util.Random;

public class MLSnakeAdapter extends KeyAdapter
{
    private Encephalon<double[], double[]> encephalon;
    private boolean isTracking = false;
    private final Random random;
    private  final int KEY_CODE_RANGE = 4;
    private int nextMove = 1;
    private  double [] env, prevMov;

    // Constructor with encephalon
    public MLSnakeAdapter(Encephalon<double[], double[]> encephalon)
    {
        super();
        this.encephalon = encephalon;
        this.random = new Random();
        prevMov = new double[]{0,0};
        env = null;
    }

    // Default constructor
    public MLSnakeAdapter()
    {
        this(null); // Call the parameterized constructor
    }

    public int getInput()
    {
        return predictMove();
    }

    // Swaps the encephalon instance
    public void swapEncephalon(Encephalon<double[], double[]> newEncephalon)
    {
        this.encephalon = newEncephalon;
    }

    // Utility method: Returns the index of the max value in a probability array
    private int argMax(double[] probabilities) 
    {
        int maxIndex = 0;
        for (int i = 1; i < probabilities.length; i++) 
        {
            if (probabilities[i] > probabilities[maxIndex]) 
            {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    private int predictMove() 
    {
        nextMove = random.nextInt(KEY_CODE_RANGE) + 37;
        String output = "";
        isTracking = isCloser(euclideanDistance(prevMov[0], prevMov[1],env[2], env[4]));
        if (env != null) 
        {
            if (encephalon != null) 
            {
                encephalon.observe(env);
                nextMove = 37 + argMax(encephalon.predict());
                output = nextMove == 37 ? "Left": 
                nextMove == 38 ? "Up": 
                nextMove == 39 ? "Right": 
                nextMove == 40 ? "Down": "Unknown move "+ nextMove;
                System.out.print("\r Agent moved "+output);
                
            }
            prevMov[0] = env[1];
            prevMov[1] = env[3];
        }
        return nextMove;
    }
    
    /** 
     * enviroment data {bodycount, x[0], y[0], appleLocx, appleLocy, SCREEN_WIDTH, SCREEN_HEIGHT};
     */
    public void setEnviroment(double[] enviroment) 
    {
       this.env = enviroment;
    }

    public double [] getEnviroment() 
    {
       return env;
    }

    public boolean isTracking() {
        return isTracking;
    }

    private boolean isCloser( double prevDis)
    {
        return (prevDis <= euclideanDistance(env[1], env[3], env[2], env[4])); 
    }

    private double euclideanDistance(double x1, double y1, double x2, double y2) 
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    

}
