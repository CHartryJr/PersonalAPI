package Intellegence.artifical_neural_network.ModelInterface;

public interface Model 
{
    double[] predict(double[] input);
    double[] train(double[] input, double[] expectedOutput);
}
