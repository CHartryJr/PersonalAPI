package Intellegence.ArtificalNeualNet;

public class OutputLayer extends Layer
{
    int precision;

     public OutputLayer(int numberOfOutputs)
    {
        super(numberOfOutputs);
        precision = 6;
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
            x.setNet(net);
            x.setOutput(truncate(activation.apply(net + x.getBias())));
        }
    }

    private double truncate(double value) 
    {
        if (precision < 0) 
            throw new IllegalArgumentException("Decimal places must be non-negative");

        double scaleFactor = Math.pow(10, precision);
        return Math.floor(value * scaleFactor) / scaleFactor;
    }

    public int getPrecision() 
    {
      return precision;
    }

    public void setPrecision(int precision)
    {
        this.precision = precision;
    }

}
