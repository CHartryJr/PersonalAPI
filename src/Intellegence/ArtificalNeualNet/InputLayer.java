package Intellegence.ArtificalNeualNet;

public class InputLayer extends Layer
{
    private  double [] currerntInput;

    /**
     * The inoput is a double repesntaion of the inputs 
     * @param inputRep
     */
      
    public InputLayer(int numberOfInputNeurons)
    {
        super(numberOfInputNeurons);
        this.currerntInput = new double[]{0};
    }

    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(neurons.size(), next.neurons.size()));
    }
    
    /**
    * @param inputRep the inputRep to set
    */
   protected void setInputRep(double[] inputRep) 
   {
        if( currerntInput.length != neurons.get(0).getWeightsIn().length)
            neurons.forEach(neuron -> neuron.setWeightsIn(neuron.randomizedWeightsList(inputRep.length)));

        currerntInput = inputRep;
   }

    @Override
    void Activate() 
    {
        for(int i = 0; i <  currerntInput.length; ++i)
        {
           for(Neuron x : neurons)
           {
                x.setOutput( x.getOutput() + (x.getWeightsIn()[i] * currerntInput[i]));
           }
        }
    }
}
