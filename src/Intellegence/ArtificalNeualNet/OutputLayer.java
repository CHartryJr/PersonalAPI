package Intellegence.ArtificalNeualNet;

public class OutputLayer extends Layer
{
     public OutputLayer( int numberOfOutputs)
    {
        super(numberOfOutputs);
       
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size(), this.neurons.size()));
    }
    
}
