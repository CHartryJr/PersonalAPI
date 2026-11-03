package Intellegence.artifical_neural_network.Model;

import Intellegence.artifical_neural_network.LayerInterface.Tensor;
import Intellegence.artifical_neural_network.Utilities.Activation;

public class Dense extends Tensor
{
    Dense(int numberOfNeurons, int numberofWeightsPerNeuron, Activation activation)
    {
        super(numberOfNeurons, numberofWeightsPerNeuron, activation);
    }
}