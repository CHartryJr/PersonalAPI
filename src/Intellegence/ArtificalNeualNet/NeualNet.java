package Intellegence.ArtificalNeualNet;
import Intellegence.Encephalon;
import java.util.Random;
/**
 *  This a low scale mock of a neual network that show my understanding of simple ANN.
 */
public class NeualNet implements Encephalon<double[]>,Comparable<NeualNet>
{
    private final int GENERATOR = 20;
    private int numberOfInputs,numberOfLayers,numberOfOutPuts,size;
    private double fitness;
    private InputLayer firstLayer  = null;
   
    public NeualNet(int numberOfInputs,int numberOfLayers,int numberOfOutPuts)
    {
        size = 0;
        fitness = -1.0d;
        this.numberOfLayers = numberOfLayers;
        this.numberOfOutPuts = numberOfOutPuts;
        this.numberOfInputs = numberOfInputs;
    }
    
    /**
     * @return the size
     */
    public int getSize() 
    {
        return size;
    }

    public void init()
    {
        Random rand = new Random(System.currentTimeMillis());
        firstLayer = new InputLayer(numberOfInputs);
        firstLayer.next = new HiddenLayer(rand.nextInt(GENERATOR) + 1);
        Layer currentLayer = firstLayer.next;
        firstLayer.init();
        currentLayer.prev  = firstLayer;
        size = 3;
        int i = 0;
        while(i < numberOfLayers)
        {
            currentLayer.next = new HiddenLayer(rand.nextInt(GENERATOR) + 1);
            currentLayer.init();
            currentLayer.next.prev = currentLayer;
            currentLayer = currentLayer.next;
            ++i;
            ++size;
        }
        currentLayer.next = new OutputLayer(numberOfOutPuts);
    }

    @Override
    public void observe(double[]parsept) 
    {
        if (parsept.length != numberOfInputs)
            throw new IndexOutOfBoundsException("Parsept does Not match number of inputs");
        firstLayer.setInputRep(parsept);
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
    
} 
