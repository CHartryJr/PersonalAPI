package Intellegence.ArtificalNeualNet;
public class InputLayer extends Layer
{
    private int numberOfInputNeurons;

    /**
     * The inoput is a double repesntaion of the inputs 
     * @param inputRep
     */
      
    public InputLayer(int numberOfInputNeurons)
    {
        super(numberOfInputNeurons);
        this.numberOfInputNeurons = numberOfInputNeurons;
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(numberOfInputNeurons, next.neurons.size()));
    }
    
}
