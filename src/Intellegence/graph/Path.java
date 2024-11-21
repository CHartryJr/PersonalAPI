package intellegence.graph;

import java.util.ArrayList;

public class Path 
{
    private ArrayList<Path> nodes;
    private double value, heuristic;
    private String id;
    
    /**
     * @param nodes
     */
    public Path()
     {
        this.nodes = new ArrayList<Path>();
        this.value = 0.0d;
        this.heuristic = 0.0d;
        this.id = "";
    }
    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }
    /**
     * @return the heuristic
     */
    public double getHeuristic() {
        return heuristic;
    }
    /**
     * @param heuristic the heuristic to set
     */
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
}
