package Intellegence.ArtificalNeualNet;
import Intellegence.Encephalon;
import java.util.Random;

/**
 *  This a low scale mock of a neual network that show my understanding of simple ANN.
 * @author Carl Hartry Jr.
 */
public class NeualNet implements Encephalon<double[]>,Comparable<NeualNet>
{
    private InputLayer firstLayer;
    private OutputLayer lastLayer;
    private double fitness,MSE,learningRate;
    private final int MAX_NUMBER_NEURONS;
    private int numberOfInputs,numberOfHiddenLayers,numberOfOutPuts,size;
    

    public NeualNet(int numberOfInputs, int numberOfHiddenLayers, int numberOfOutPuts)
    {
        MAX_NUMBER_NEURONS = 5;
        size = 0;
        fitness = -1.0d;
        MSE = 0.0d;
        learningRate = .001d;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfOutPuts = numberOfOutPuts;
        this.numberOfInputs = numberOfInputs;
        firstLayer  = null;
        init();
    }

    public NeualNet(int numberOfInputs,int numberOfHiddenLayers,int numberOfOutPuts,int maxNumOfNuerons)
    {
        if(maxNumOfNuerons < 1)
            throw new IndexOutOfBoundsException("Invalid Number of Nuerons");
        MAX_NUMBER_NEURONS = maxNumOfNuerons;
        size = 0;
        fitness = -1.0d;
        MSE =0.0d;
        learningRate = .001d;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfOutPuts = numberOfOutPuts;
        this.numberOfInputs = numberOfInputs;
        firstLayer  = null;
        init();
    }

    /**
     * @return the size
     */
    public int getSize() 
    {
        return size;
    }

    @Override
    public void observe(double[]parsept) 
    {
        firstLayer.setInputRep(parsept);
    }
/**
 * @apiNote This function is used to take in a input and prepare the network
 * @param parsept
 * @implNote Warnning!!! Changing shape of the input will result in a Changing input weights
 */
    public void observe(double[][]parsept) 
    {
        double [] convertedParsept = flatten(parsept);
        firstLayer.setInputRep(convertedParsept);
    }

    public void forward()
    {
        Layer currentLayer  = firstLayer;
        while(currentLayer != null)
        {
            currentLayer.activate();
            currentLayer = currentLayer.next;
        }
    }

    public void backPropogate( double [] expecting )
    {
        if(expecting.length != lastLayer.neurons.size())
            throw new IndexOutOfBoundsException("expected size dose not match output size");
        setMSE(expecting);
        gradientDecent(expecting);
    }

    public void  setActivation(Activation e)
    {
        Layer currentLayer = firstLayer;
        while (currentLayer != null)
        {
            currentLayer.setActivation(e);
            currentLayer = currentLayer.next;
        }
    }

    public void  setActivation(Activation e, int index)
    {
        if( index < 0  | index >= size)
            throw new IndexOutOfBoundsException();

        Layer currentLayer = firstLayer;

        for(int i = 0; i< index; ++i)
        {
            currentLayer.setActivation(e);
            currentLayer = currentLayer.next;
        }
    }

    /**
     * @implNote Must call Observe before using this method call.
     * @return
     */
    public double[] predict()
    {   
        return lastLayer.getCurrentOutput();
    } 

    /**
     * @return the mSE
     */
    public double getError() 
    {
        return MSE;
    }
    
    @Override
    public String toString()
    {
        Layer currentLayer = firstLayer;
        String message = "";
        while(currentLayer != null)
        {
            message += currentLayer.toString();
            currentLayer = currentLayer.next;
        }
        return message;
    }

    @Override
    public int compareTo(NeualNet o) 
    {
        int flag = 0;
        if( this.fitness < o.fitness )
        {
            flag = -1;
        }
         else if ( this.fitness > o.fitness )
        {
            flag = 1;
        }
         else if ( this.fitness == o.fitness )
        {
            if( this.MSE < o.MSE)
            {
                flag = 1;
            }
             else if ( this.MSE > o.MSE )
            {
                flag = -1;
            }
            else if ( this.MSE == o.MSE )
            {
                flag = 0; 
            }
        }
        return flag;
    }

    private void gradientDecent(double [] expected)
    {
        Layer currentLayer = lastLayer;
        double deriveMSE = getMSEDerive(expected);

        while(currentLayer != null )
        {
            for (int i =0; i < currentLayer.neurons.size(); ++i) 
            {
                Neuron currentNeuron   = currentLayer.neurons.get(i);
                double out = currentNeuron.getOutput();
                double deltaOver = deriveMSE  * currentLayer.activation.derive(out);

                for(int j = 0; j < currentNeuron.getWeights().length; ++j)
                {
                    if(currentLayer == lastLayer)
                    {
                        double delta = -2 * (out - expected[i]) * currentLayer.activation.derive(out);
                        currentNeuron.getWeights()[j] = currentNeuron.getWeights()[j] - learningRate * ( delta * currentLayer.prev.neurons.get(j).getOutput());
                        currentNeuron.setBias(currentNeuron.getBias() - (delta));
                    }
                     else if(currentLayer == firstLayer)
                    {
                        currentNeuron.getWeights()[j] = currentNeuron.getWeights()[j] - learningRate * ( deltaOver * firstLayer.getCurrentInput()[j]);
                        currentNeuron.setBias(currentNeuron.getBias() - (deltaOver));
                    }
                    else
                    {
                        currentNeuron.getWeights()[j] = currentNeuron.getWeights()[j] - learningRate * ( deltaOver * currentLayer.prev.neurons.get(j).getOutput());
                        currentNeuron.setBias(currentNeuron.getBias() - (deltaOver));
                    }
                } 
            }
            currentLayer = currentLayer.prev;
        }
    }

    private void init()
    {
        Random rand = new Random(System.currentTimeMillis());
        firstLayer = new InputLayer(numberOfInputs);
        Layer currentLayer = new HiddenLayer(rand.nextInt(MAX_NUMBER_NEURONS) + 1);
        firstLayer.next = currentLayer;
        currentLayer.prev = firstLayer;
        firstLayer.init();
        size = 3;
        int i = 1;
        while(i < numberOfHiddenLayers)
        {
            currentLayer.next = new HiddenLayer(rand.nextInt(MAX_NUMBER_NEURONS) + 1);
            currentLayer.init();
            currentLayer.next.prev = currentLayer;
            currentLayer = currentLayer.next;
            ++i;
            ++size;
        }
        lastLayer = new OutputLayer(numberOfOutPuts);
        currentLayer.next = lastLayer;
        currentLayer.init();
        lastLayer.prev = currentLayer; 
        lastLayer.init();
    }

    private void setMSE( double [] expected )
    {
        MSE = 0.0d;
        for(int i = 0; i <lastLayer.neurons.size(); ++i)
        {
            MSE += Math.pow((expected[i] - lastLayer.neurons.get(i).getOutput()),2);        
        } 
        MSE /= lastLayer.neurons.size();
    }   

    private double getMSEDerive( double [] expected )
    {
        double derivatative = 0.0d;
        for(int i = 0; i < lastLayer.neurons.size(); ++i)
        {
            derivatative += (expected[i] - lastLayer.neurons.get(i).getOutput());        
        } 
        derivatative *= -2/lastLayer.neurons.size();
        return derivatative;
    }   

    private double[] flatten(double[][] parsept) 
    {
        int rows = parsept.length;
        int cols = parsept[0].length;
        double[] flatArray = new double[rows * cols];
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                flatArray[i * cols + j] = parsept[i][j];
            }
        }
        return flatArray;
    }
} 