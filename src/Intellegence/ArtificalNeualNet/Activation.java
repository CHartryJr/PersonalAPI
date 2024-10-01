package Intellegence.ArtificalNeualNet;

public enum Activation 
{
    SIGMOID
    {
        @Override
        double apply(double value) 
        {
            return 1/(1+Math.exp(-value));
        }

        @Override
        double derive(double value) 
        {
            return (1+Math.exp(-value)) * Math.exp(-value);
        }
    },

    HYPER_TANGENT
    {
        @Override
        double apply(double value) 
        {
            return (1-Math.exp(-value))/(1+Math.exp(-value));
        }

        @Override
        double derive(double value) 
        {
            return (2*(Math.exp(value)))/ Math.pow(Math.exp(value)+1,2);
        }
    },

    HAILSTONE
    {

        @Override
        double apply(double value) 
        {
            value = Math.floor(value);
            if(value == 1) 
                return 1.0;
            return value % 2 == 0 ?  value/2 : 3*value+1;
        }

        @Override
        double derive(double value) 
        {
            value = Math.floor(value);
            if(value == 1 )
                return 0;
            return value % 2 == 0 ? .5 : 3;
        }
    },
    
    RECTIFIED_LINEAR_UNIT
    {
        @Override
        double apply(double value) 
        {
            return  value > 0 ? value : 0.0d;
        }

        @Override
        double derive(double value) 
        {
          return value > 0 ? 1.0d : 0.0d;
        }
    },

    STEP
    {
        @Override
        double apply(double value) 
        {
            return value > 0 ? 1.0d : 0.0d;
        }

        @Override
        double derive(double value) 
        {
            return 0;
        }
    };

    /**
     * Function used to apply activation funtions.
     * @Aurthor CHartryJr
     * @param value
     * @return
     */
    abstract double apply(double value);
    /**
     * Function used to apply dervivatives funtions.
     * @Aurthor CHartryJr
     * @param value
     * @return
     */
    abstract double derive(double value);
}
