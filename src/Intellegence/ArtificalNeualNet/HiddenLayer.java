package Intellegence.ArtificalNeualNet;

public class HiddenLayer extends Layer
{
    public HiddenLayer(int numberOfNeurons)
    {
        super(numberOfNeurons);
       
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size(), next.neurons.size())); 
    }
}
