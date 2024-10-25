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

    @Override
    void Activate() 
    {
        if( prev == null || next == null)
            throw new UnsupportedOperationException("There are no linked layers look over network");

        for(int i = 0; i <  prev.neurons.size(); ++i)
        {
            Neuron  currentNeuron = prev.neurons.get(i);
            for(Neuron x : neurons)
            {
                double input =  prev.activation.apply(currentNeuron.getOutput() + currentNeuron.getBias());
                x.setOutput( x.getOutput() + (x.getWeightsIn()[i] * input));
            }
            prev.neurons.get(i).setOutput(0.0d);
        }
    }
}
