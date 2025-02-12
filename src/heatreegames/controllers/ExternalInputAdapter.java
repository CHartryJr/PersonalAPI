package heatreegames.controllers;
import java.awt.event.KeyAdapter;
import java.util.Random;
import java.util.concurrent.locks.*;

public class ExternalInputAdapter extends KeyAdapter 
{
    private boolean isTracking = false;
    private final Random random;
    private final int KEY_CODE_RANGE = 4;
    private int nextMove = 1;
    private double[] env, prevMov, prediction;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ExternalInputAdapter() 
    {
        super();
        this.random = new Random();
        prevMov = new double[]{0, 0};
        env = null;
    }

    public int getNextMove() 
    {
        return predictMove();
    }

    public void getExternalPrediction(double[] externalPrediction) 
    {
        lock.writeLock().lock();
        try {
            this.prediction = externalPrediction;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isTracking() 
    {
        return isTracking;
    }

    public void setEnviroment(double[] enviroment) 
    {
        lock.writeLock().lock();
        try {
            this.env = enviroment;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public double[] getEnviroment() 
    {
        lock.readLock().lock();
        try {
            return env;
        } finally {
            lock.readLock().unlock();
        }
    }

    private int predictMove() 
    {
        lock.writeLock().lock();
        try 
        {
            nextMove = random.nextInt(KEY_CODE_RANGE) + 37;
            if (prediction != null && env != null) 
            {
                //printLog();
                isTracking = isCloser();
                nextMove = 37 + argMax(prediction);
                prevMov[0] = env[1];
                prevMov[1] = env[3];
            }
            return nextMove;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private int argMax(double[] probabilities) 
    {
        int maxIndex = 0;
        for (int i = 1; i < probabilities.length; i++) 
        {
            if (Double.compare(probabilities[i],probabilities[maxIndex]) == 1) 
            {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private boolean isCloser() 
    {
        lock.readLock().lock();
        try {
            return (euclideanDistance(prevMov[0], prevMov[1], env[3], env[4]) >
                    euclideanDistance(env[1], env[2], env[3], env[4]));
        } finally {
            lock.readLock().unlock();
        }
    }

    private double euclideanDistance(double x1, double y1, double x2, double y2) 
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

     void  printLog()
    {
        String move = nextMove == 37 ? "Left":
                      nextMove == 38 ? "Up":
                      nextMove == 39 ? "right":
                      nextMove == 40 ? "Down" : "Unkown";
        System.out.print(String.format("\r Current Model Movement %s  Mapping{%f ,%f}  predictions{%f,%f,%f,%f} Model Tracking %b"
        ,move,env[1],env[3],prediction[0],prediction[1] ,prediction[2],prediction[3],isTracking));
    }
}
