package heatreegames.controllers;

import intellegence.Encephalon;
import java.util.Random;


public class MLAdapter extends Adapter
{
    private Encephalon<double[],double[]>  MLM ;
    private Random rand; 

    public MLAdapter(Encephalon<double[],double[]>  MLM)
    {
       super();
       this.MLM = MLM;
    }
    
    public MLAdapter()
    {
        super();
        MLM = null;
    }

    @Override
     public int getInput()
    {
        rand = new Random();
        return MLM == null? rand.nextInt(4) + 37 : (int)MLM.predict()[0];    
    }
    //37 ,39,38,40 up down left righht
    public  void swapEncephalon(Encephalon<double[],double[]> MLM )
    {
        this.MLM = MLM;
    }

}
