package heatreegames.controllers;

import Intellegence.Encephalon;

import java.awt.event.KeyAdapter;
import java.util.Random;

public class MLSnakeAdapter extends KeyAdapter
{
    private Encephalon<double[], double[]> encephalon;
    private final Random random;
    private  final int KEY_LEFT = 37;
    private  final int KEY_UP = 38;
    private  final int KEY_RIGHT = 39;
    private  final int KEY_DOWN = 40;
    private  final int KEY_CODE_RANGE = 4;
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

    private int predictMove() 
    {
        if(env != null)
        {
            if(encephalon != null)
            {
                encephalon.observe(env);
                return  37 + argMax(encephalon.predict());
            }
            else
            {
                int xDiff = (int) Math.abs(env[1] - env[3]);
                int yDiff = (int) Math.abs(env[2] - env[4]);
                int nextMove = 0;
                if (xDiff < yDiff && xDiff != 0) 
                {
                    nextMove = env[1] > env[3] ? KEY_LEFT : KEY_RIGHT;
                } 
                else 
                {
                    nextMove = env[2] < env[4] ? KEY_DOWN : KEY_UP;
                }
                return nextMove;
            }
        }
        return random.nextInt(KEY_CODE_RANGE) + KEY_LEFT;
    }
        

}
