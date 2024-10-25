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
    

    protected double[] predict()
    {
        double [] actValues = new double [neurons.size()];
        
        for(int i = 0; i < neurons.size(); ++i) //i = current neuron on both sides
        {
           actValues[i] = activation.apply(neurons.get(i).summation(currerntInput));
        }
        return actValues;
    }

}
