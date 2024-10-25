package Intellegence.ArtificalNeualNet;
import java.util.ArrayList;
/**
 * Layer class to immplement the layer sturcture in Neural Networks
 * @author Carl Hartry jr.
 */
abstract class Layer
{
    protected Layer prev = null;
    protected Layer next = null;
    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected double [] currerntInput;
    protected Activation activation = Activation.RECTIFIED_LINEAR_UNIT;

    abstract void init();

    public Layer(int numberOfNeurons)
    {
    if(numberOfNeurons < 1)
            throw new IndexOutOfBoundsException("Invalid number of Neurons");
        while(numberOfNeurons > neurons.size())
        {
            neurons.add(new Neuron());
        }
        currerntInput = new double[neurons.get(0).getWeightsIn().length];
    }

    public void  setActivation(Activation func)
    {
        this.activation  = func;
    }

   

    public void printLayerInfo()
    {
      String message = String.format(this+"\n Prevous Layer Size: %d and Next Layer Size: %d ",
      prev ==null ? 0 : prev.neurons.size()
      ,next == null ? 0 : next.neurons.size());
      System.out.println(message);
    }

    /**
     * @return the activation
     */
    public Activation getActivation() 
    {
        return activation;
    }

    /**
     * @apiNote set the activation function
     */
    public void SetActivation(Activation func) 
    {
        this.activation = func;
    }

    @Override
    public String toString() 
    {
        StringBuilder message = new StringBuilder();
        message.append(String.format("\n{**** %s ****}\nCurrent Activation: %s\nNumber of Neurons: %d\n\n", 
                                    this.getClass().getSimpleName().toUpperCase(), activation,neurons.size()));

        for (int i = 0; i < neurons.size(); ++i) 
        {
            Neuron temp = neurons.get(i);
            message.append(String.format("NEURON %d\n\nINPUT WEIGHTS: [ ", i + 1));

            for (double w : temp.getWeightsIn()) 
            {
                message.append(w).append("  ");
            }

            message.append("]\nOUTPUT WEIGHTS: [ ");

            for (double w : temp.getWeightsOut()) 
            {
                message.append(w).append("  ");
            }

            message.append("]\n\n");
        }
        return message.toString();
    }
    /**
    * @param inputRep the inputRep to set
    */
   protected void setInputRep(double[] inputRep) 
   {
       currerntInput = inputRep;
   }
// func(z) * outW = next layer neurons input
    protected double [] Activate()
    {
        if(next == null)
            throw new IndexOutOfBoundsException("Activation failure at "+  this.getClass().getSimpleName().toUpperCase());

        int outSize = next.neurons.get(0).getWeightsIn().length;
        double [] actValues = new double [outSize];
        
        for(int i = 0; i < outSize; ++i) //i = current neuron on both sides
        {
           actValues[i] = activation.apply(neurons.get(i).summation(currerntInput)); // * neurons.get(i).getWeightsOut()[i];// multipling by out weight shows neurons confidence 
        }
        return actValues;
    }
}