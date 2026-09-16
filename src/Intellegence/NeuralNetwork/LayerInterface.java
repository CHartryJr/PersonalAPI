package Intellegence.NeuralNetwork;

public interface LayerInterface 
{
    Activation act = Activation.RECTIFIED_LINEAR_UNIT;
    abstract double [][] forward(double[][] input);
    abstract double [][] backward(double[][] returnOut);
}
