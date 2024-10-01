package Intellegence.ArtificalNeualNet;
import Intellegence.Encephalon;

public class NeualNet implements Encephalon<Double[]>,Comparable<NeualNet>
{
    double fitness;
    public NeualNet()
    {
        fitness = 0.0d;
        
    }
    
    @Override
    public void observe(Double []parsept) 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'observe'");
    }

    @Override
    public int compareTo(NeualNet o) 
    {
        int check = 0;
        if(this.fitness < o.fitness)
        {
            check = -1;
        }
         else if (this.fitness > o.fitness)
        {
            check = 1;
        }
         else if (this.fitness == o.fitness)
        {
            check = 0;
        }
        return check;
    }
    
} 
