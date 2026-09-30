package Intellegence.artifical_neural_network;
import java.util.Arrays;

abstract class Tensor implements Layer
{
    private Tensor nextLayer,previousLayer;
    private Activation activation;
    private ErrorCalculation errorCalculation;
    private double[] weights;
    private double[] biases;
    private double learningRate;
    private int numberOfNeurons;
    private int numberofWeightsPerNeuron;
   
    
    public Tensor(int numberOfNeurons, int numberofWeightsPerNeuron, Activation activation)
    {
        this.numberOfNeurons = numberOfNeurons;
        this.numberofWeightsPerNeuron = numberofWeightsPerNeuron;
        this.activation = activation;
        this.errorCalculation = ErrorCalculation.SQUARED_ERROR; // Default error calculation method
        this.weights = Arrays.stream(new double[numberOfNeurons * numberofWeightsPerNeuron]).map(i -> (Math.random()*2) - 1).toArray();
        this.biases = Arrays.stream(new double[numberOfNeurons]).map(i -> (Math.random()*2) - 1).toArray();
        this.learningRate = 0.01; // Default learning rate
    }

    abstract void setPreviousLayer(Tensor previousLayer);
    abstract void setNextLayer(Tensor nextLayer);

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double[] getBiases() {
        return biases;
    }

    public void setBiases(double[] biases) {
        this.biases = biases;
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }


    public int getNumberofWeightsPerNeuron() {
        return numberofWeightsPerNeuron;
    }

    public ErrorCalculation getErrorCalculation() {
        return errorCalculation;
    }

    public void setErrorCalculation(ErrorCalculation errorCalculation) {
        this.errorCalculation = errorCalculation;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) 
    {
        this.activation = activation;
    }


    public void insertLayer(Tensor nextLayer, Tensor previousLayer)
    {
        this.nextLayer = nextLayer;
        this.previousLayer = previousLayer;
    }

    public void appendLayer(Tensor nextLayer)
    {
        this.nextLayer = nextLayer;
    }

    public Tensor getNextLayer() {
        return nextLayer;
    }

    public Tensor getPreviousLayer() {
        return previousLayer;
    }

    public Tensor removeLayer()
    {
        this.nextLayer.previousLayer = this.previousLayer;
        this.previousLayer.nextLayer = this.nextLayer;
        this.nextLayer = null;
        this.previousLayer = null;
        return this;
    }

    @Override
    public double[] forward(double[] input)
    {
        double[] output = new double[numberOfNeurons];
        for (int i = 0; i < numberOfNeurons; i++)
        {
            double sum = biases[i];
            for (int j = 0; j < numberofWeightsPerNeuron; j++)
            {
                sum += weights[i * numberofWeightsPerNeuron + j] * input[j];
            }
            output[i] = activation.apply(sum);
        }
        return output;
    }

    @Override
    public double[] backward(double[] outputGradient)
    {
        double[] inputGradient = new double[numberofWeightsPerNeuron];
        for (int i = 0; i < numberOfNeurons; ++i)
        {
            double derivative = activation.derive(outputGradient[i]);
            for (int j = 0; j < numberofWeightsPerNeuron; j++)
            {
                inputGradient[j] += weights[i * numberofWeightsPerNeuron + j] * derivative;
            }
        }
        return inputGradient;
    }

    public String getShape()
    {
        return String.format("[%d,%d]", numberOfNeurons, numberofWeightsPerNeuron);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Activation: %s\n", this, activation.toString()));
        sb.append(String.format("Shape: %s\n", getShape()));
        
        sb.append("Weights: ");
        for (int i = 0; i < weights.length; ++i)
        {
            if(i % numberofWeightsPerNeuron == 0)
                sb.append("[ ");
            
            sb.append(weights[i]).append(" ");

            if(weights.length - 1 == i || (i + 1) % numberofWeightsPerNeuron == 0)
                sb.append(" ] ");
        }

        sb.append("\nBiases:[");
        for (double bias : biases)
        {
            sb.append(bias).append(" ");
        }
        sb.append("]\n");

        return sb.toString();
    }

}
