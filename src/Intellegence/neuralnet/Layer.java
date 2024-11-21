package intellegence.neuralnet;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Layer class to immplement the layer sturcture in Neural Networks
 * @author Carl Hartry jr.
 */
abstract class Layer implements Serializable
{
    protected Layer prev = null;
    protected Layer next = null;
    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected Activation activation = Activation.RECTIFIED_LINEAR_UNIT;
    abstract void init();
    abstract void  activate();

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
        this.activation  = func;
    }

    public void printLayerInfo()
    {
      String message = String.format(this+"\n Prevous Layer Size: %d and Next Layer Size: %d ",
      prev == null ? 0 : prev.neurons.size()
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
            message.append(String.format("> NEURON %d <\n\n WEIGHTS: [ ", i + 1));

            for (double w : temp.getWeights()) 
            {
                message.append(w).append("  ");
            }

            message.append("]\n\n Bias :[ ");
            message.append(neurons.get(i).getBias()).append(" ]");
        
            message.append("\n\n Output  : [ ");
            message.append(neurons.get(i).getOutput()).append(" ]");
            message.append("\n\n");
        }
        return message.toString();
    }
   
}