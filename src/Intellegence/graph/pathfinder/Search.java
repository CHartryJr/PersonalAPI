package intellegence.graph.pathfinder;

import java.util.HashMap;
import intellegence.Encephalon;

enum Search implements Encephalon<Path ,double[]>
{
    DRISKAL,BREATHFIRST,DEPTHFIRST,ASTAR;
    
    HashMap<String,Double> frontier = new HashMap<>();

    @Override
    public double[] predict() 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'predict'");
    }

    @Override
    public void observe(Path parsept) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'observe'");
    }
}