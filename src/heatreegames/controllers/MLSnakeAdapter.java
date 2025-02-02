package heatreegames.controllers;

import Intellegence.Encephalon;
import java.awt.event.KeyAdapter;
import java.util.Random;

public class MLSnakeAdapter extends KeyAdapter
{
    private Encephalon<double[], double[]> encephalon;
    private final Random random;
    private  final int KEY_CODE_RANGE = 4;
    private int nextMove = 1;
    private  double [] env;

    // Constructor with encephalon
    public MLSnakeAdapter(Encephalon<double[], double[]> encephalon)
    {
        super();
        this.encephalon = encephalon;
        this.random = new Random();
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
    // enviroment data {body, x[0], y[0], appleLocx, appleLocy, SCREEN_WIDTH, SCREEN_HEIGHT};
    private int predictMove() 
    {
        nextMove = random.nextInt(KEY_CODE_RANGE) + 37;
        String output = "";
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
                return nextMove;
            }

            return nextMove;
        }
        return nextMove;
    }
    
    
    public void setEnviroment(double[] enviroment) 
    {
       this.env = enviroment;
    }

    public double [] getEnviroment() 
    {
       return env;
    }
}
