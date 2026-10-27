package Intellegence.predictiiveInterface;

public interface Predictor 
{
    double[] predict(double[] input);
    void train(double[] expectedOutput);
}
