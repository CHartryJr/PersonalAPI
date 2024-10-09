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

    /**
     * @param inputRep the inputRep to set
     */
    public void setInputRep(double[] inputRep) 
    {
        int i  = 0;
        double [] filler;
        while( i < inputRep.length)
        {
            filler = new double [inputRep.length];
            int j = 0;
            while(j < filler.length)
            {
                filler[j] = inputRep[i];
                ++j;
            }
            neurons.get(i).setWeightsIn(filler);
            ++i;
        }
    }
    
    @Override
    void init() 
    {
        this.neurons.forEach(neuron -> neuron.init(numberOfInputNeurons, next.neurons.size()));
    }
    
    public String getInputRep()
    {

        String message = "Inputs : ";
        System.out.println();
        for(Neuron x: this.neurons)
        {
            message += Double.toString(x.getWeightsIn()[0])+ ' ';
        }
        return message;
    }

    
}
