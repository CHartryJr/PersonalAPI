package Intellegence.artifical_neural_network.ModelInterface;

import java.io.Serializable;
import Intellegence.predictiiveInterface.Predictor;

public abstract class Model implements Predictor,Comparable<Model>,Serializable
{
    private double fitness = 0;
    public double getFitness()
    {
        return fitness;
    }
    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    public void resetFitness()
    {
        this.fitness = 0;
    }

    public void adJustFitness(double fitness)
    {
        this.fitness += fitness;
    }
}
