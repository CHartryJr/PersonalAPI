package Intellegence.ArtificalNeualNet;
public class InputLayer extends Layer
{
    int numberOfInputNeurons;
  
    /**
     * The inoput is a double repesntaion of the inputs 
     * @param inputRep
     */
      
    public InputLayer(int numberOfInputNeurons)
    {
        super(numberOfInputNeurons);
        this.numberOfInputNeurons = numberOfInputNeurons;
    }

    /**
     * @param inputRep the inputRep to set
     */
    public void setInputRep(double[] inputRep) 
    {
        this.neurons.forEach(neuron -> neuron.setWeightsIn(inputRep));
    }
    
    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(numberOfInputNeurons, next.neurons.size()));
    }

}
