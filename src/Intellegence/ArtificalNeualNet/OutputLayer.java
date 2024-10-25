package Intellegence.ArtificalNeualNet;

public class OutputLayer extends Layer
{
    private double [] currentOutput;

     public OutputLayer( int numberOfOutputs)
    {
        super(numberOfOutputs);
        currentOutput = new double[numberOfOutputs];
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(prev.neurons.size(), this.neurons.size()));
    }
    

    protected double[] getCurrentOutput()
    {
        return currentOutput;
    }

    @Override
    void Activate() 
    {
        if( prev == null)
            throw new UnsupportedOperationException("There are no linked layers to tghe output layer look over network");

        for(int i = 0; i <  prev.neurons.size(); ++i)
        {
            Neuron currentNeuron = prev.neurons.get(i);
            for(Neuron x : neurons)
            {
                double input =  prev.activation.apply(currentNeuron.getOutput() + currentNeuron.getBias());
                x.setOutput( x.getOutput() + (x.getWeightsIn()[i] * input));
            }
            prev.neurons.get(i).setOutput(0.0d);
        }

        for(int i = 0; i < neurons.size(); ++i)
        {
            currentOutput[i] = neurons.get(i).getOutput();
            neurons.get(i).setOutput(0.0d);
        }

    }

}
