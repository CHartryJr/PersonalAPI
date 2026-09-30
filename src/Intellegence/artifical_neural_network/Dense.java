package Intellegence.artifical_neural_network;

public class Dense extends Tensor
{
   

    public Dense(int numberOfNeurons, int numberofWeightsPerNeuron, Activation activation)
    {
        super(numberOfNeurons, numberofWeightsPerNeuron, activation);
    }

    @Override
    void setPreviousLayer(Tensor previousLayer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPreviousLayer'");
    }

    @Override
    void setNextLayer(Tensor nextLayer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNextLayer'");
    }


}
