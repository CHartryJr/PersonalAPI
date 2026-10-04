package Intellegence.artifical_neural_network;

public interface Layer 
{
    double [] forward(Tensor Layer);
    double [] backward(double[] outputGradient);    
}
