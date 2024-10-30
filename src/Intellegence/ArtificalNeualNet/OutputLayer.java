package Intellegence.ArtificalNeualNet;

public class OutputLayer extends Layer
{
    

     public OutputLayer(int numberOfOutputs)
    {
        super(numberOfOutputs);
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size()));
    }
    

    protected double[] getCurrentOutput()
    {
        double[] currentOutput = new double[neurons.size()];
        for(int i = 0; i < neurons.size(); ++i)
        {
            currentOutput[i] = neurons.get(i).getOutput();
        }
        return currentOutput;
    }

    @Override
    void activate() 
    {
        if( prev == null)
            throw new UnsupportedOperationException("There are no linked layers to tghe output layer look over network");

        for(Neuron x : neurons)
        {   
            double net = 0.0d; 
            for(int i = 0; i <  prev.neurons.size(); ++i)
            {
                double input = prev.neurons.get(i).getOutput();
                net += (x.getWeights()[i] * input);
            }
            x.setOutput(activation.apply(net + x.getBias()));
        }
    }

}
