package Intellegence;

/**
 * This function will be used to input data into various modles.
 */
public interface Encephalon <T,R>
{  
    void observe(T parsept);
    R predict();
} 
