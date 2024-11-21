package intellegence.neuralnet;


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
        this.neurons.forEach(neuron -> neuron.init(neurons.size()));
    }
    
    /**
    * @param inputRep the inputRep to set
    */
   protected void setInputRep(double[] inputRep) 
   {
        if( inputRep.length != neurons.get(0).getWeights().length)
            neurons.forEach(neuron -> neuron.setWeightsIn(neuron.randomizedWeightsList(inputRep.length)));
        currerntInput = inputRep;
   }

   protected double [] getCurrentInput()
   {
        return currerntInput;
   }

    @Override
    void activate() 
    {
        for(Neuron x : neurons)
        {
            double net = 0.0d;
            for(int i = 0; i <  currerntInput.length; ++i)
            {
                net += x.getWeights()[i] * currerntInput[i];
            }
            x.setNet(net);
            x.setOutput(activation.apply(net + x.getBias()));
        }
    }

}
