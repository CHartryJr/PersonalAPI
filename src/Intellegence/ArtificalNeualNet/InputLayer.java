package Intellegence.ArtificalNeualNet;
public class InputLayer extends Layer
{
    private double [] inputRep; 
    protected InputLayer()
    {
        super();
        inputRep = new double[]{0,0,0,0,0};
    }
    /**
     * The inoput is a double repesntaion of the inputs 
     * @param inputRep
     */

    public InputLayer(double [] inputRep)
    {
        super();
        this.inputRep = inputRep;
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
        this.neurons.forEach(neuron -> neuron.init(inputRep.length, next.neurons.size()));
        this.neurons.forEach(neuron -> neuron.setWeightsIn(inputRep));
    }

}
