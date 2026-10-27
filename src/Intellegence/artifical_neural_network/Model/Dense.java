package Intellegence.artifical_neural_network.Model;

import Intellegence.artifical_neural_network.LayerInterface.Activation;
import Intellegence.artifical_neural_network.LayerInterface.Tensor;

public class Dense extends Tensor
{
    Dense(int numberOfNeurons, int numberofWeightsPerNeuron, Activation activation)
    {
        super(numberOfNeurons, numberofWeightsPerNeuron, activation);
    }
}