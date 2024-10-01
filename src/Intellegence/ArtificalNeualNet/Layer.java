package Intellegence.ArtificalNeualNet;
import java.util.ArrayList;
/**
 * Layer class to immplement the layer sturcture in Neural Networks
 * @author Carl Hartry jr.
 */
abstract class Layer
{
    protected ArrayList<Neuron> neurons = new ArrayList<Neuron>();
    protected Activation func = Activation.ReLu;
    protected Layer prev = null;
    protected Layer next = null;
    abstract void init();
    public void  setActivation(Activation func)
    {
        this.func  = func;
    }
}