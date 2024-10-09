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
    private double fitness;
    private final int MAX_NUMBER_NEURONS;
    private int numberOfInputs,numberOfHiddenLayers,numberOfOutPuts,size;

    public NeualNet(int numberOfInputs, int numberOfHiddenLayers, int numberOfOutPuts)
    {
        MAX_NUMBER_NEURONS = 10;
        size = 0;
        fitness = -1.0d;
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

    public String ShowCurrentInput()
    {
        
       return firstLayer.getInputRep();
    }

    @Override
    public void observe(double[]parsept) 
    {
        if (parsept.length != numberOfInputs)
            throw new IndexOutOfBoundsException("Parsept does Not match number of inputs");
        firstLayer.setInputRep(parsept);
    }

    public void observe(double[][]parsept) 
    {
        if (parsept.length * parsept[0].length != numberOfInputs)
            throw new IndexOutOfBoundsException("Parsept does Not match number of inputs");
        double [] convertedParsept = flatten(parsept);
        firstLayer.setInputRep(convertedParsept);
    }

    public void forward()
    {
        Layer currentlayer = firstLayer;
        while(currentlayer != null)
        {
            
            
        }
    }

    public double[] predict()
    {
        return lastLayer.neurons.get(0).getWeightsOut();
    } 

    @Override
    public int compareTo(NeualNet o) 
    {
        int flag = 0;
        if(this.fitness < o.fitness)
        {
            flag = -1;
        }
         else if (this.fitness > o.fitness)
        {
            flag = 1;
        }
         else if (this.fitness == o.fitness)
        {
            flag = 0;
        }
        return flag;
    }

    private void init()
    {
        Random rand = new Random(System.currentTimeMillis());
        firstLayer = new InputLayer(numberOfInputs);
        firstLayer.next = new HiddenLayer(rand.nextInt(MAX_NUMBER_NEURONS) + 1);
        Layer currentLayer = firstLayer.next;
        firstLayer.init();
        currentLayer.prev  = firstLayer;
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
        lastLayer.prev = currentLayer; 
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