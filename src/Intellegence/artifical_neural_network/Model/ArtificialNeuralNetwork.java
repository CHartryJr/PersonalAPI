package Intellegence.artifical_neural_network.Model;
import Intellegence.artifical_neural_network.ModelInterface.Model;
import Intellegence.neuralnet.Activation;

public class ArtificialNeuralNetwork extends Model
{
    private Dense[] layers;
    Activation activation = Activation.SIGMOID;
    double learningRate = 0.01,mutationRate = 0.01, mutationScale = 0.1;
    private int numberOfLayers = 0,numberOfNeurons = 0;
   
    
    ArtificialNeuralNetwork(){}
    
    
    public ArtificialNeuralNetwork(Dense[] layers)
    {
        this.layers = layers;
    }
    
    @Override
    public double[] predict(double[] input) 
    {
        double[] output = input;
        for (Dense layer : layers) 
        {
            output = layer.forward(output);
        }
        return output;
    }

    @Override
    public void train(double[] expectedOutput) 
    {
        double[] output = layers[layers.length - 1].getOutput();
        double[] gradient = new double[output.length];
        
        gradient = ErrorCalculation.SQUARED_ERROR.gradient(output, expectedOutput);
        
        
        for (int i = layers.length - 1; i >= 0; --i) 
        {
            gradient = layers[i].backward(gradient);
        }

    }
}