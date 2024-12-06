package intellegence.neuralnet;
import java.io.*;
import java.util.Random;
import intellegence.Encephalon;

/**
 *  @implNote This is a mock of a Artifical Neural Network.
 *  @apiNote Implements Encephalon Comparable and Serializable.
 *  @author Carl Hartry Jr.
 */
public class NeuralNet implements Encephalon<double[],double[]>, Comparable<NeuralNet>, Serializable
{
    private InputLayer firstLayer;
    private OutputLayer lastLayer;
    private Random rand;
    private double fitness,MSE,learningRate;
    private final int MAX_NUMBER_NEURONS;
    private int numberOfInputs, numberOfHiddenLayers, numberOfOutPuts, size;
    
    /**
     * 
     * @param numberOfInputs
     * @param numberOfHiddenLayers
     * @param numberOfOutPuts
     * @param maxNumOfNuerons
     * @Author Carl Hartry Jr.
     */
    public NeuralNet( int numberOfInputs,int numberOfHiddenLayers,int numberOfOutPuts,int maxNumOfNuerons )
    {
        if(maxNumOfNuerons < 1)
            throw new IndexOutOfBoundsException("Invalid Number of Nuerons");
        MAX_NUMBER_NEURONS = maxNumOfNuerons;
        size = 0;
        fitness = -1.0d;
        MSE =  0.0d;
        learningRate =  1E-3;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfOutPuts = numberOfOutPuts;
        this.numberOfInputs = numberOfInputs;
        firstLayer  = null;
        init();
    }

    /**
     * 
     * @param numberOfInputs
     * @param numberOfHiddenLayers
     * @param numberOfOutPuts
     * @apiNote maxNumNeuron = 5
     */
    public NeuralNet( int numberOfInputs, int numberOfHiddenLayers, int numberOfOutPuts )
    {
        this( numberOfInputs,numberOfHiddenLayers,numberOfOutPuts,5 );
    }

    /**
     *  Defualt contructor input is one neuron  and output one neuron
     */
    public NeuralNet()
    {
        this( 1,0,1,1 );
    }

    /**
     * @return the size
     */
    public int getSize() 
    {
        return size;
    }

    /**
     * @apiNote This function is used to make a slight change to each weight and bias in the network
     */
    public void  mutate()
    {
        Layer cuLayer = firstLayer;
        rand = new Random(System.currentTimeMillis());

        while(cuLayer != null)
        {
            for(Neuron n : cuLayer.neurons)
            {
                for(double d: n.getWeights())
                {
                    d = transform(d);
                }
                n.setBias(transform(n.getBias()));
            }
            cuLayer = cuLayer.next;
        }
    }

    public HiddenLayer removeHiddenLayer(int index)
    {
        if(size == 2 | index < 0 | index >= size | index + 1 == size - 1)
            throw new IndexOutOfBoundsException();
        Layer currentLayer  = firstLayer.next;
        for(int i = 0; i < index; ++i)
        {
            currentLayer = currentLayer.next;
        } 
        currentLayer.prev.next = currentLayer.next;
        currentLayer.next.prev = currentLayer.prev;
        currentLayer.next.init();
        currentLayer.next = null;
        currentLayer.prev = null;
        --size;
        return (HiddenLayer)currentLayer;
    }

    public Layer removeHiddenLayer()
    {
        return removeHiddenLayer(0);
    }

    public void addHiddenLayer(int index, HiddenLayer newLayer)
    {
        if(index < 0 | index >= size | index == size - 1)
            throw new IndexOutOfBoundsException();
            Layer currentLayer  = firstLayer;
            for(int i = 0; i < index; ++i)
            {
                currentLayer = currentLayer.next;
            } 
            newLayer.next = currentLayer.next;
            newLayer.prev = currentLayer;
            currentLayer.next = newLayer;
            newLayer.next.prev = newLayer;
            newLayer.init();
            newLayer.next.init();
            ++size;   
    }

    public void addHiddenLayer(HiddenLayer newLayer)
    {
        addHiddenLayer(0,newLayer);
    }

    public HiddenLayer getHiddenLayer(int index)
    {
        if(index < 0 | index >= size | index == size - 1)
            throw new IndexOutOfBoundsException();
        Layer currentLayer = firstLayer.next;
        for(int i =0; i < index;++i)
        {
            currentLayer = currentLayer.next;
        }
        return (HiddenLayer)currentLayer;
    }

    public HiddenLayer replace(int index, Layer newLayer)
    {
        if(size == 2 | index < 0 | index >= size | index + 1 == size - 1)
            throw new IndexOutOfBoundsException();
        Layer currentLayer  = firstLayer.next;
        for(int i = 0; i < index; ++i)
        {
            currentLayer = currentLayer.next;
        } 
        newLayer.prev = currentLayer.prev;
        newLayer.prev.next = newLayer;
        newLayer.next = currentLayer.next;
        newLayer.next.prev = newLayer;
        currentLayer.next = null;
        currentLayer.prev = null;
        newLayer.next.init();
        newLayer.init();
        return (HiddenLayer)currentLayer;
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
        observe(flatten(parsept));
    }

    /**
     * @return the fitness
     */
    public double getFitness() 
    {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(double fitness)
     {
        this.fitness = fitness;
    }

    /**
     * @return the learningRate
     */
    public double getLearningRate() 
    {
        return learningRate;
    }

    /**
     * @param learningRate the learningRate to set
     */
    public void setLearningRate(double learningRate) 
    {
        this.learningRate = learningRate;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(int precision) 
    {
        lastLayer.setPrecision(precision);
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

    public void train(int regiment, double [][] expected )
    { 
        train(regiment, flatten(expected));
    }

    public void train(int regiment, double [] expected )
    {
        switch (regiment) 
        {
            case 0:
                gradientDecent(expected);
                break;
        
            default:
                gradientDecent(expected);
                break;
        }

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
    public int compareTo(NeuralNet o) 
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

    public boolean saveStream(String loc)
    {
         try
        {   
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(loc +".annObj");
            ObjectOutputStream out = new ObjectOutputStream(file);
            // Method for serialization of object
            out.writeObject(this);
            out.close();
            file.close();
            return true;

        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
    }
    public boolean loadStream(String loc)
    {
         try
        {   
            //Saving of object in a file
            FileInputStream file = new FileInputStream(loc+".annObj");
            ObjectInputStream out = new ObjectInputStream(file);
            // Method for serialization of object
            NeuralNet temp = (NeuralNet)out.readObject();
            this.firstLayer = temp.firstLayer;
            this.lastLayer = temp.lastLayer;
            this.fitness = temp.fitness;
            this.MSE = temp.MSE;
            this.learningRate = temp.learningRate;
            this.numberOfInputs = temp.numberOfInputs;
            this.numberOfHiddenLayers = temp.numberOfHiddenLayers;
            this.numberOfOutPuts = temp.numberOfOutPuts;
            this.size = temp.size;
            out.close();
            file.close();
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    private void gradientDecent(double [] expected)
    {
        if(expected.length != lastLayer.neurons.size())
            throw new IndexOutOfBoundsException("expected size dose not match output size");
        
        setMSE(expected);
        backPropogate(expected);
        Layer currentLayer = lastLayer;
        while(currentLayer != null)
        {
            for (Neuron x : currentLayer.neurons) 
            {
                for (int j =0; j < x.getWeights().length; ++j) 
                {
                    x.getWeights()[j] =  x.getWeights()[j] - (learningRate* x.getDelta() * currentLayer.prev.neurons.get(j).getOutput()); //delta(L) * prev out associsted to weight
                }
                x.setBias(x.getBias() - ( x.getDelta()*.1) );
            }
            currentLayer = currentLayer.next;
        }
    }

    private void backPropogate( double [] expected )
    {
        Layer currentLayer = lastLayer;

        while(currentLayer != null )
        {
            for (int j = 0; j < currentLayer.neurons.size(); ++j) 
            {
                Neuron currentNeuron = currentLayer.neurons.get(j);
                double net = currentNeuron.getNet(), currentActDerivative = currentLayer.activation.derive(net) ,  out = currentNeuron.getOutput();

                if(currentLayer == lastLayer)
                {
                    currentLayer.neurons.get(j).setDelta((2 * (out - expected[j]) * currentActDerivative));
                }
                else
                {
                    double delatSumation = 0.0d;
                    for( Neuron k : currentLayer.next.neurons)
                    {
                        delatSumation += (k.getDelta() *  currentActDerivative * k.getWeights()[j]);
                    }
                    currentLayer.neurons.get(j).setDelta(delatSumation);
                }     
            }
            currentLayer = currentLayer.prev;
        }
    }

    private void init()
    {
       rand = new Random(System.currentTimeMillis());
        firstLayer = new InputLayer(numberOfInputs);
        Layer currentLayer = firstLayer;
        size = 2;
        for(int i = 0 ;i < numberOfHiddenLayers; ++i)
        {
            currentLayer.next = new HiddenLayer(rand.nextInt(MAX_NUMBER_NEURONS) + 1);
            currentLayer.init();
            currentLayer.next.prev = currentLayer;
            currentLayer = currentLayer.next;
            ++size;
        }
        lastLayer = new OutputLayer(numberOfOutPuts);
        currentLayer.next = lastLayer;
        currentLayer.init();
        lastLayer.prev = currentLayer; 
        lastLayer.init();
    }

    private  double transform(double d )
    {
        double threshold = rand.nextDouble();
        if( threshold < .2 )
            d = -d;
        else if ( threshold < .4 )
            d  = rand.nextDouble(2.0d) - 1.0d; 
        else if ( threshold < .6 )
            d /= (1 / (rand.nextInt(100)+1));
        else if ( threshold < .8 )
            d *= (1 / (rand.nextInt(100)+1));
        return d;
    }

    private void setMSE( double [] expected )
    {
        MSE = 0.0d;
        for(int i = 0; i <lastLayer.neurons.size(); ++i)
        {
            MSE += Math.pow(expected[i] - lastLayer.neurons.get(i).getOutput(),2);        
        } 
        MSE =  MSE  * 1/lastLayer.neurons.size();
    }   

    private double[] flatten( double[][] parsept ) 
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