package Intellegence.ArtificalNeualNet;

public class HiddenLayer extends Layer
{
    public HiddenLayer(int numberOfNeurons)
    {
        super();
        if(numberOfNeurons < 1)
            throw new IndexOutOfBoundsException("Invalid number of Neurons");
        while(numberOfNeurons > neurons.size())
        {
            neurons.add(new Neuron());
        }
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size(), next.neurons.size())); 
    }
}
