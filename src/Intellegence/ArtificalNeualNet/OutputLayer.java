package Intellegence.ArtificalNeualNet;

public class OutputLayer extends Layer
{
     public OutputLayer( int numberOfOutputs)
    {
        super();
        while(numberOfOutputs > neurons.size())
        {
            neurons.add(new Neuron());
        }
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size(), this.neurons.size()));
    }
    
}
