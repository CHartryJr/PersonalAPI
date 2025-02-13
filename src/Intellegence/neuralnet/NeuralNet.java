package Intellegence.neuralnet;
import java.io.*;
import java.util.Random;
import Intellegence.Encephalon;

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
    private double fitness,MSE,learningRate,mutationRate,mutationScale;
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
        fitness = 0;
        MSE =  0.0d;
        learningRate =  1E-3;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfOutPuts = numberOfOutPuts;
        this.numberOfInputs = numberOfInputs;
        firstLayer  = null;
        mutationRate = .01d;
        mutationScale = .01d;
        rand = new Random(System.currentTimeMillis());
        init();
    }

    /**
     * 
     * @param numberOfInputs
     * @param numberOfHiddenLayers
     * @param numberOfOutPuts
     * 
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
     * Sets the mutation rate, ensuring it stays within the valid range [0.1, 0.8].
     * @param mutationRate The mutation rate to be set.
     */
    public void setMutationRate(double mutationRate) 
    {
        this.mutationRate = mutationRate > .8d ? .8d :
                            mutationRate < .1d ? .1d : mutationRate;
    }
    /**
     * Sets the mutation scale, ensuring it stays within the valid range [0.1, 10].
     * @param mutationScale The mutation scale to be set.
     */
    public void setMutationScale(double mutationScale) 
    {
        this.mutationScale = mutationScale > 10d ? 10d :
                             mutationScale < .1d ? .1d : mutationScale;
    }
    /**
     * Retrieves the current input values from the first layer of the neural network.
     * @return An array of double values representing the current input.
     */
    public double[] getCurrentInput()
    {
        return firstLayer.getCurrentInput();
    }
    
    /**
     * Retrieves the number of layers in the neural network.
     * @return The size of the network.
     */
    public int getSize() 
    {
        return size;
    }

    /**
     * Applies mutation to the network by modifying weights and biases randomly.
     */
    public void  mutate()
    {
        Layer cuLayer = firstLayer;
        //rand = new Random(System.currentTimeMillis());

        while(cuLayer != null)
        {
            for(Neuron n : cuLayer.neurons)
            {
                double [] newWeights = new double[n.getWeights().length];
                for(int j = 0; j < n.getWeights().length;++j)
                {
                    newWeights[j] = transform(n.getWeights()[j]);
                }
                n.setWeightsIn(newWeights);
                n.setBias(transform(n.getBias()));
            }
            cuLayer = cuLayer.next;
        }
    }
    /**
     * Removes a hidden layer at a specific index.
     * @param index The index of the hidden layer to be removed.
     * @return The removed HiddenLayer.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
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
    /**
     * Removes the first hidden layer.
     * @return The removed HiddenLayer.
     */
    public Layer removeHiddenLayer()
    {
        return removeHiddenLayer(0);
    }
    /**
     * Adds a hidden layer at a specific index.
     * @param index The index where the new hidden layer should be inserted.
     * @param newLayer The new HiddenLayer to be added.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
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
    /**
     * Adds a hidden layer at the first available position.
     * @param newLayer The new HiddenLayer to be added.
     */
    public void addHiddenLayer(HiddenLayer newLayer)
    {
        addHiddenLayer(0,newLayer);
    }
    /**
     * Retrieves a hidden layer at a specific index.
     * @param index The index of the hidden layer to retrieve.
     * @return The requested HiddenLayer.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
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
    /**
     * Replaces a hidden layer at a given index with a new layer.
     * @param index The index of the layer to be replaced.
     * @param newLayer The new layer to insert.
     * @return The replaced HiddenLayer.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
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
    /**
     * Sets the input of the neural network.
     * @param parsept An array representing the input values.
     * @throws RuntimeException If the input is null.
     */
    @Override
    public void observe(double[]parsept) 
    {
        if( parsept == null)
            throw new RuntimeException("no input to observe");
        firstLayer.setInputRep(parsept);
    }

    /**
     * Observes a two-dimensional input and flattens it before feeding it into the network.
     * @param parsept A 2D array representing input values.
     */
    public void observe(double[][]parsept) 
    {
        observe(flatten(parsept));
    }

    /**
     * Retrieves the current fitness value of the network.
     * @return The fitness score.
     */
    public double getFitness() 
    {
        return fitness;
    }

    /**
     * Sets the fitness score of the neural network.
     * @param fitness The fitness score to set.
     */
    public void setFitness(double fitness)
     {
        this.fitness = fitness;
    }

    /**
     * Retrieves the current learning rate of the network.
     * @return The learning rate.
     */
    public double getLearningRate() 
    {
        return learningRate;
    }

    /**
     * Sets the learning rate of the neural network.
     * @param learningRate The learning rate to set.
     */
    public void setLearningRate(double learningRate) 
    {
        this.learningRate = learningRate;
    }
    /**
     * Sets the precision for the output layer calculations.
     * @param precision The precision value.
     */
    public void setPrecision(int precision) 
    {
        lastLayer.setPrecision(precision);
    }
    /**
     * Propagates input forward through the network layers.
     */
    public void forward()
    {
        Layer currentLayer  = firstLayer;
        while(currentLayer != null)
        {
            currentLayer.activate();
            currentLayer = currentLayer.next;
        }
    }
    /**
     * Trains the neural network with a given regiment and expected outputs.
     * @param regiment The training regiment type.
     * @param expected The expected outputs in a 2D array format.
     */
    public void train(int regiment, double [][] expected )
    { 
        train(regiment, flatten(expected));
    }
    /**
     * Trains the neural network with a given regiment and expected outputs.
     * @param regiment The training regiment type.
     * @param expected The expected outputs in a 1D array format.
     */
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
    /**
     * Sets the activation function for all layers in the network.
     * @param e The activation function to apply.
     */
    public void  setActivation(Activation e)
    {
        Layer currentLayer = firstLayer;
        while (currentLayer != null)
        {
            currentLayer.setActivation(e);
            currentLayer = currentLayer.next;
        }
    }
    /**
     * Sets the activation function for a specific layer index.
     * @param e The activation function to apply.
     * @param index The index of the layer.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
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
     * Runs the network prediction using current input values.
     * @return The predicted output values as an array.
     */
    public double[] predict()
    {   
        return lastLayer.getCurrentOutput();
    } 

    /**
    * Retrieves the Mean Squared Error (MSE) of the network.
    * @return The MSE value.
    */
    public double getError() 
    {
        return MSE;
    }
    /**
     * Provides a string representation of the network structure.
     * @return A formatted string representing the network.
     */
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
    /**
     * Compares the fitness and error values between this and another NeuralNet instance.
     * @param o The other NeuralNet instance to compare.
     * @return Comparison result as an integer.
     */
    @Override
    public int compareTo(NeuralNet o) 
    {
        if(o == null)
            return 1;

        int flag = Double.compare(this.fitness, o.fitness);
        
        if ( flag == 0)
        {
            flag = Double.compare(this.MSE, o.MSE);
        }
        return flag;
    }
    /**
     * Saves the neural network state to a file.
     * @param loc The file location to save the object.
     * @return True if successful, false otherwise.
     */
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
    /**
     * Loads a neural network state from a file.
     * @param loc The file location to load the object from.
     * @return True if successful, false otherwise.
     */
    public boolean loadStream(String loc)
    {
         try
        {   
            
            FileInputStream file = new FileInputStream(loc+".annObj");
            ObjectInputStream out = new ObjectInputStream(file);
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
            System.out.println(e + "Net failed to,load");
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

    /**
     * Fuction to update weights using deriavtive and layers to expected output connections.
     * @param expected
     */
    private void backPropogate( double [] expected )
    {
        if(expected.length != lastLayer.neurons.size())
            throw new IndexOutOfBoundsException("expected size dose not match output size");

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
        int hiddenNeuronCount,reconNeurons = (numberOfInputs + numberOfOutPuts)/2;
        rand = new Random(System.currentTimeMillis());
        firstLayer = new InputLayer(numberOfInputs);
        Layer currentLayer = firstLayer;
        size = 2;
        for(int i = 0 ;i < numberOfHiddenLayers; ++i)
        {
            hiddenNeuronCount = MAX_NUMBER_NEURONS - reconNeurons < 0 ? reconNeurons : (reconNeurons + rand.nextInt(MAX_NUMBER_NEURONS - reconNeurons));
            currentLayer.next = new HiddenLayer(hiddenNeuronCount);
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

    private double transform(double value) 
    {
        double threshold = rand.nextDouble();

    if (threshold < mutationRate * 0.2) 
    {
        value = -value; // Flip sign
    } 
    else if (threshold < mutationRate * 0.4) 
    {
        value += rand.nextGaussian() * mutationScale * 0.1; // Small additive mutation
    } 
    else if (threshold < mutationRate * 0.6) 
    {
        value *= 1 + (rand.nextGaussian() * mutationScale * 0.1); // Small multiplicative mutation
    } 
    else if (threshold < mutationRate * 0.8) 
    {
        value *= rand.nextBoolean() ? (1.0 + mutationScale * 0.1) : (1.0 - mutationScale * 0.1);
    } 
    else if (threshold < mutationRate) 
    {
        value += (rand.nextDouble() * 2 - 1) * mutationScale * 0.5; // Random shift
    }

    // Prevent extreme values
    double maxValue = mutationScale * 10;
    double minValue = -maxValue;
    value = Math.max(minValue, Math.min(maxValue, value));

    return value;
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