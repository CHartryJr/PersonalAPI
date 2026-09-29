package Intellegence.artifical_neural_network;

public interface Layer 
{
    double [] forward(double[] input);
    double [] backward(double[] outputGradient);    
}
