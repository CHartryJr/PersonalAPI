package Intellegence.artifical_neural_network.LayerInterface;
import java.util.Arrays;
import Intellegence.artifical_neural_network.Utilities.Activation;
import Intellegence.artifical_neural_network.Utilities.PredictionType;

public abstract class Tensor implements Layer, Cloneable
{
    private Activation activation;

    private double[] weights,biases,input,output,delta;
    private double learningRate;
    private int numberOfNeurons, numberOfWeightsPerNeuron;// row x colums
    private int  layerSize;
    private boolean verbose;
   
    public Tensor(int numberOfNeurons, int numberofWeightsPerNeuron, Activation activation)
    {
        this.numberOfNeurons = numberOfNeurons;
        this.numberOfWeightsPerNeuron = numberofWeightsPerNeuron;
        this.activation = activation;
        this.weights = Arrays.stream(new double[numberOfNeurons * numberofWeightsPerNeuron]).map(i -> (Math.random()*2) - 1).toArray();
        this.biases = Arrays.stream(new double[numberOfNeurons]).map(i -> (Math.random()*2) - 1).toArray();
        this.learningRate = 0.001; // Default learning rate
        this.layerSize = numberOfNeurons * numberofWeightsPerNeuron;
        this.output = new double[numberOfNeurons];
        this.delta = new double[numberOfNeurons];
        this.verbose = false; // Default verbosity
    }

    public double[] getWeights() 
    {
        return weights;
    }

    protected void setWeights(double[] weights) 
    {
        this.weights = weights;
    }

    public double[] getBiases() 
    {
        return biases;
    }

    protected void setBiases(double[] biases) 
    {
        this.biases = biases;
    }

    public int getNumberOfNeurons() 
    {
        return numberOfNeurons;
    }

    public int getNumberOfWeightsPerNeuron() 
    {
        return numberOfWeightsPerNeuron;
    }

    public double getLearningRate() 
    {
        return learningRate;
    }

    protected void setLearningRate(double learningRate) 
    {
        this.learningRate = learningRate;
    }

    public double[] getDelta() {
        return delta;
    }

    protected void setDelta(double[] delta) 
    {
        this.delta = delta;
    }

    public Activation getActivation() 
    {
        return activation;
    }

    public void setActivation(Activation activation) 
    {
        this.activation = activation;
    }


     public double[] getInput() 
    {
        return input;
    }

    public double[] getOutput() 
    {
        return output;
    }

    public String getShape()
    {
        return String.format("[%d,%d]", numberOfNeurons, numberOfWeightsPerNeuron);
    }

    @Override
    public double [] forward(double[] input) 
    {
        isValidInput(input);
        this.input = input;
        double[] sums = biases.clone();
        int weightsPerNeuron = numberOfWeightsPerNeuron;

        for (int k = 0; k < layerSize; ++k) 
        {
            int neuron = k / weightsPerNeuron;
            int inputIndex = k % weightsPerNeuron;

            sums[neuron] += weights[k] * input[inputIndex];

            if (inputIndex == weightsPerNeuron - 1) 
            {
                output[neuron] = activation.apply(sums[neuron]);
            }
        }
        return output;
    }

    public double[] forward(Tensor Layer)
    {
        isValidInput(Layer.getOutput());
        this.input = Layer.getOutput();
        double[] sums = biases.clone();
        int weightsPerNeuron = numberOfWeightsPerNeuron;

        for (int k = 0; k < layerSize; ++k) 
        {
            int neuron = k / weightsPerNeuron;
            int inputIndex = k % weightsPerNeuron;

            sums[neuron] += weights[k] * input[inputIndex];

            if (inputIndex == weightsPerNeuron - 1) 
            {
                output[neuron] = activation.apply(sums[neuron]);
            }
        }
        return output;
    }

    @Override
    public double[] backward(double[] outputGradient) 
    {
        double[] deltaIn = new double[numberOfWeightsPerNeuron];

        for (int k = 0; k < layerSize; ++k) 
        {
            int neuron = k / numberOfWeightsPerNeuron;
            int inputIndex = k % numberOfWeightsPerNeuron;

            // Propagate gradient to the previous layer
            deltaIn[inputIndex] += weights[k] * activation.derive(outputGradient[neuron]);

            // Update each weight once
            weights[k] -= learningRate * activation.derive(outputGradient[neuron]) * input[inputIndex];

            // Update each bias once, on this neuron's last input
            if (inputIndex == numberOfWeightsPerNeuron - 1) 
            {
                biases[neuron] -= learningRate * activation.derive(outputGradient[neuron]);
            }
        }

        this.delta = deltaIn.clone();
        return deltaIn;
    }

    public double[] backward(Tensor layer) 
    {

        double[] deltaIn = new double[numberOfWeightsPerNeuron],outputGradient = layer.getDelta();

        for (int k = 0; k < layerSize; ++k) 
        {
            int neuron = k / numberOfWeightsPerNeuron;
            int inputIndex = k % numberOfWeightsPerNeuron;

            // Propagate gradient to the previous layer
            deltaIn[inputIndex] += weights[k] * activation.derive(outputGradient[neuron]);

            // Update each weight once
            weights[k] -= learningRate * activation.derive(outputGradient[neuron]) * input[inputIndex];

            // Update each bias once, on this neuron's last input
            if (inputIndex == numberOfWeightsPerNeuron - 1) 
            {
                biases[neuron] -= learningRate * activation.derive(outputGradient[neuron]);
            }
        }

        this.delta = deltaIn.clone();
        return deltaIn;
    }  
 
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Activation: %s\n", this, activation.toString()));
        sb.append(String.format("Shape: %s\n", getShape()));
        if(verbose)
        {
            sb.append("Weights: ");
            for (int i = 0; i < weights.length; ++i)
            {
                if(i % numberOfWeightsPerNeuron == 0)
                    sb.append("[ ");
                
                sb.append(weights[i]).append(" ");

                if(weights.length - 1 == i || (i + 1) % numberOfWeightsPerNeuron == 0)
                    sb.append(" ] ");
            }

            sb.append("\nBiases:[");
            for (double bias : biases)
            {
                sb.append(bias).append(" ");
            }
            sb.append("]\n");

            sb.append("Output:[");
            for (double out : output)
            {
                sb.append(out).append(" "); 
            }
            sb.append("]\n");
        }
        
        return sb.toString();
    }

    @Override 
    public Tensor clone() 
    {
        try 
        {
            Tensor cloned = (Tensor) super.clone();
            cloned.weights = weights.clone();
            cloned.biases = biases.clone();
            cloned.input = input.clone();
            cloned.output = output.clone();
            cloned.delta = delta.clone();
            return cloned;
        } 
        catch (CloneNotSupportedException e) 
        {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    private boolean isValidInput(double[] input)
    {
        boolean isValid = false;

        if (input.length == numberOfWeightsPerNeuron)
        {
            isValid = true;
        }
        else
        {
            System.err.printf("Invalid input length: expected %d, got %d\n", numberOfWeightsPerNeuron, input.length);
        }

        return isValid;
    }

}