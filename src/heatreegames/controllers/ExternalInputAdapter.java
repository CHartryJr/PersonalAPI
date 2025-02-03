package heatreegames.controllers;

import java.awt.event.KeyAdapter;
import java.util.Random;



public class ExternalInputAdapter extends KeyAdapter
{
    
    private boolean isTracking = false;
    private final Random random;
    private  final int KEY_CODE_RANGE = 4;
    private int nextMove = 1;
    private  double [] env, prevMov,prediction;

    public ExternalInputAdapter() 
    {
        super();
        this.random = new Random();
        prevMov = new double[]{0,0};
        env = null;
    }

    /**
     * this fuction returns the next move for game
     * @return
     */
    public int getNextMove()
    {
        return predictMove();
    }

    /**
     * get the prediction from outside source
     * @param externalPrediction
     */
    public void getExternalPrediction(double [] externalPrediction) 
    {
       this.prediction = externalPrediction;
    }

    /**
     * using euckidican distance  returns a boolean rep for how close model is to target.
     * @return
     */
    public boolean isTracking() 
    {
        return isTracking;
    }

    public void setEnviroment(double[] enviroment) 
    {
       this.env = enviroment;
    }

    public double [] getEnviroment() 
    {
       return env;
    }

    private int predictMove() 
    {
        nextMove = random.nextInt(KEY_CODE_RANGE) + 37;
        if(prediction != null)
        if (env != null) 
        {
            //String output = "|";
            isTracking = isCloser();
            nextMove = 37 + argMax(prediction);
                        // output = nextMove == 37 ? "Left": 
                        // nextMove == 38 ? "Up": 
                        // nextMove == 39 ? "Right": 
                        // nextMove == 40 ? "Down": "Unknown move "+ nextMove;
                        // System.out.print(" Agent moved "+output+"|");
                  
            prevMov[0] = env[1];
            prevMov[1] = env[3];
        }
        return nextMove;
    }
 
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

    /** 
    * enviroment data {bodycount, x[0], y[0], appleLocx, appleLocy, SCREEN_WIDTH, SCREEN_HEIGHT};
    */
    private boolean isCloser()
    {
        //is prevoius distance greater than new distance?
        return (euclideanDistance(prevMov[0], prevMov[1],env[3], env[4]) > euclideanDistance(env[1], env[2], env[3], env[4])); 
    }

    private double euclideanDistance(double x1, double y1, double x2, double y2) 
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
