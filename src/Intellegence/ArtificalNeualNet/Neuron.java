package Intellegence.ArtificalNeualNet;
import java.util.Random;
/**
 * Neuron class to 
 */
class Neuron
{
    private double weightsIn[];
    private double bias;
    private double output;
    private Random rand;

    protected Neuron()
    {
        weightsIn = new double[]{0};
        bias = 0.0d;
        output = 0.0d;
    }
   
    /**
     * @apiNote Used to create batch of new In/out weights based off of the number of neurons in the prevous and outer later.
     * @param layerIn
 
     */
    protected void init(int layerIn)
    {
        rand = new Random();
        bias =  Math.pow(-1,(rand.nextInt(2)+1)) * rand.nextDouble();
        weightsIn = randomizedWeightsList(layerIn);
    }

    protected double [] randomizedWeightsList(int size)
    {
        double [] weights = new double[size];
        for (int i = 0; i < size; ++i) 
        {
            weights[i] = Math.pow(-1,(rand.nextInt(2)+1)) * rand.nextDouble();
        }
        return weights;
    }

    /**
     * @return the weightsIn
     */
    protected double[] getWeights() 
    {
        return weightsIn;
    }

    /**
     * @param weightsIn the weightsIn to set
     */
    protected void setWeightsIn(double[] weightsIn) 
    {
        this.weightsIn = weightsIn;
    }

    /**
     * @return the bias
     */
    protected double getBias()
    {
        return bias;
    }

    /**
     * @param bias the bias to set
     */
    public void setBias(double bias) 
    {
        this.bias = bias;
    }    

     /**
     * @apiNote uses tyhe 
     * @param index
     * @return  summation of neuron's output weight(index)
     * @implNote Z = Summation (Wi + Xi) + b, Z is what we are returning
     */
    protected double summation(double[] inputs) 
    {
        if (inputs.length != weightsIn.length)  
        {
            throw new IllegalArgumentException("Input length must match the number of input weights.");
        }

        double sum = 0.0;

        // Dot product of inputs and weights
        for (int i = 0; i < inputs.length; i++) 
        {
            sum += inputs[i] * weightsIn[i];
        }
        // Add bias to the summation
        sum += bias;
        return sum;
    }

    /**
     * @return the output
     */
    protected double getOutput() 
    {
        return output;
    }

    /**
     * @param output the output to set
     */
    protected void setOutput(double output) 
    {
        this.output = output;
    }
}
