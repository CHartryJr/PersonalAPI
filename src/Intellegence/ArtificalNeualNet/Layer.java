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

    public void printLayer()
    {
        System.out.println(this);
    }
    
    @Override
    public String toString() 
    {
        String message =String.format("Current Activation: %s, Number of Neurons{%d}\n_________________________________________________",this.func,this.neurons.size());
        return message;
    }

    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected Activation func = Activation.HAILSTONE;
    protected Layer prev = null;
    protected Layer next = null;
    abstract void init();
   
}