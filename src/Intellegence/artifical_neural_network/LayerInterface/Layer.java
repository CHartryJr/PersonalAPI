package Intellegence.artifical_neural_network.LayerInterface;

public interface Layer 
{
    double [] forward(double [] input);
    double [] backward(double [] gradient);
}
