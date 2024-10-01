package Intellegence.ArtificalNeualNet;
import java.util.Random;
/**
 * Neuron class to 
 */
class Neuron
{
    private double weightsIn[];
    private double weightsOut[];
    private double bias;

    protected Neuron()
    {
        weightsIn =null;
        weightsOut = null;
        bias = 0.0d;
    }
    /**
     * @apiNote Used to create batch of new In/out weights based off of the number of neurons in the prevous and outer later.
     * @param layerIn
     * @param layerOut
     */
    protected  void init(int layerIn, int layerOut)
    {
        Random rand = new Random(System.currentTimeMillis());
        bias = rand.nextDouble();
        this.weightsIn =  new double [layerIn];
        this.weightsOut = new double [layerOut];
        int count = 0;
        while(count < layerIn )
        {
            weightsIn[count] = rand.nextDouble();
            ++count;
        }
        count = 0;
        while(count < layerOut)
        {
            weightsIn[count] = rand.nextDouble();
            ++count;
        }
    }

    /**
     * @return the weightsIn
     */
    protected double[] getWeightsIn() 
    {
        return weightsIn;
    }
    /**
     * @param weightsIn the weightsIn to set
     */
    protected void setWeightsIn(double[] weightsIn) 
    {
        this.weightsIn = weightsIn;
    }
    /**
     * @return the weightsOut
     */
    protected double[] getWeightsOut() 
    {
        return weightsOut;
    }
    /**
     * @param weightsOut the weightsOut to set
     */
    protected void setWeightsOut(double[] weightsOut)
    {
        this.weightsOut = weightsOut;
    }

    /**
     * @return the bias
     */
    protected double getBias()
    {
        return bias;
    }

    /**
     * @param bias the bias to set
     */
    public void setBias(double bias) 
    {
        this.bias = bias;
    }    
}
