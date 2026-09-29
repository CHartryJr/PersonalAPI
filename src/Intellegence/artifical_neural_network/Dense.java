package Intellegence.artifical_neural_network;

public class Dense extends Tensor
{
    private Tensor nextLayer,previousLayer;

    public Dense(int numberOfNeurons, int numberofWeightsPerNeuron, Activation activation)
    {
        super(numberOfNeurons, numberofWeightsPerNeuron, activation);
    }

    public void insertLayer(Tensor nextLayer, Tensor previousLayer)
    {
        this.nextLayer = nextLayer;
        this.previousLayer = previousLayer;
    }

    public void appendLayer(Tensor nextLayer)
    {
        this.nextLayer = nextLayer;
    }

    public Tensor getNextLayer() {
        return nextLayer;
    }

    public void setNextLayer(Tensor nextLayer) {
        this.nextLayer = nextLayer;
    }

    public Tensor getPreviousLayer() {
        return previousLayer;
    }

    public void setPreviousLayer(Tensor previousLayer) {
        this.previousLayer = previousLayer;
    }

    public Tensor removeLayer()
    {
        this.getNextLayer().set = this.previousLayer;
        this.previousLayer.nextLayer = this.nextLayer;
        this.nextLayer = null;
        this.previousLayer = null;
        return this;
    }

}
