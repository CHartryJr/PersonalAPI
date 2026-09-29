package Intellegence.artifical_neural_network;

abstract class Tensor implements Layer
{
    double[] weights;
    double[] biases;
    int numberOfNeurons;
    int numberofWeightsPerNeuron;
    Activation activation;
    
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
        for (int i = 0; i < numberOfNeurons; i++)
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
