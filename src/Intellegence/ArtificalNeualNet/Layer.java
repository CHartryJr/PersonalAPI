package Intellegence.ArtificalNeualNet;
import java.util.ArrayList;
/**
 * Layer class to immplement the layer sturcture in Neural Networks
 * @author Carl Hartry jr.
 */
abstract class Layer
{
    public Layer(int numberOfNeurons)
    {
    if(numberOfNeurons < 1)
            throw new IndexOutOfBoundsException("Invalid number of Neurons");
        while(numberOfNeurons > neurons.size())
        {
            neurons.add(new Neuron());
        }
    }

    
    public void  setActivation(Activation func)
    {
        this.func  = func;
    }

    public void printLayerInfo()
    {
      String message = String.format(this+"\n Prevous Layer Size: %d and Next Layer Size: %d ",
      prev ==null ? 0 : prev.neurons.size()
      ,next == null ? 0 : next.neurons.size());
      System.out.println(message);
    }
    
    @Override
    public String toString() 
    {
        String test = String.valueOf(this.getClass());
        String [] tokken = test.split("\\.");
        String currentClass = tokken[tokken.length -1];
        String message = String.format("\nLayer: %s, Current Activation: %s, Number of Neurons: %d\n_________________________________________________",currentClass,this.func,this.neurons.size());
        return message;
    }

    /**
     * @apiNote uses tyhe 
     * @param index
     * @return  summation of neuron's output weight(index)
     * @implNote Z = Summation (Wi + Xi) + b, Z is what we are returning
     */
    protected double summation(int index)
    {
        double sum = 0;
        int i = 0;
        int outSize = neurons.get(0).getWeightsIn().length;
        while(i < neurons.size())
        {
            int j = 0;
            while(j < outSize)
            {
                sum += neurons.get(i).getWeightsIn()[j] * neurons.get(i).getWeightsOut()[j] + neurons.get(i).getBias();
                ++j;
            } 
            ++i;
        }

        return sum;
    }

    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected Activation func = Activation.STEP;
    protected Layer prev = null;
    protected Layer next = null;
    abstract void init();
   
}