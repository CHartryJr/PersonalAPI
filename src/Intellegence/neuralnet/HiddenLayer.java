package intellegence.neuralnet;

public class HiddenLayer extends Layer
{
    public HiddenLayer(int numberOfNeurons)
    {
        super(numberOfNeurons);
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size())); 
    }

    @Override
    void activate() 
    {
        if( prev == null || next == null)
            throw new UnsupportedOperationException("There are no linked layers look over network");

        for(Neuron x : neurons)
        {   
            double net = 0.0d; 
            for(int i = 0; i <  prev.neurons.size(); ++i)
            {
                double input = prev.neurons.get(i).getOutput() ;
                net += (x.getWeights()[i] * input);
            }
            x.setNet(net);
            x.setOutput(activation.apply(net + x.getBias()));
        }
    }
}
