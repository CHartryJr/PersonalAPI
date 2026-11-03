package Intellegence.artifical_neural_network.Model;
import Intellegence.artifical_neural_network.ModelInterface.Model;
import Intellegence.artifical_neural_network.Utilities.ErrorCalculation;
import Intellegence.artifical_neural_network.Utilities.PredictionType;
import Intellegence.neuralnet.Activation;

public class ArtificialNeuralNetwork extends Model implements Cloneable
{
    private Dense[] layers;
    private Activation activation = Activation.SIGMOID;
    private ErrorCalculation errorCalculation = ErrorCalculation.MEAN_SQUARED_ERROR;
    private PredictionType predictionType = PredictionType.ADALINE;
    private double learningRate = 0.01,mutationRate = 0.01, mutationScale = 0.1;
    private int numberOfLayers = 0,numberOfNeurons = 0;
   
    
    ArtificialNeuralNetwork(){}

    
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
        
        gradient = ErrorCalculation.MEAN_SQUARED_ERROR.gradient(output, expectedOutput);
        
        for (int i = layers.length - 1; i >= 0; --i) 
        {
            gradient = layers[i].backward(gradient);
        }

    }

    @Override 
    public ArtificialNeuralNetwork clone() throws CloneNotSupportedException
    {
        ArtificialNeuralNetwork clonedModel = (ArtificialNeuralNetwork) super.clone();
        clonedModel.layers = new Dense[this.layers.length];
        for (int i = 0; i < this.layers.length; i++)
        {
            clonedModel.layers[i] = (Dense) this.layers[i].clone();
        }       
        return clonedModel;
    }

    @Override
    public int compareTo(Model arg0) 
    {
        return Double.compare(this.getFitness(), arg0.getFitness());
    }
}