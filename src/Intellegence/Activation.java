package Intellegence;

public enum Activation 
{
    Sigmod
    {
        @Override
        double apply(double value) 
        {
            return 1/(1+Math.exp(-value));
        }

        @Override
        double derive(double value) 
        {
            return (1+Math.exp(-value)) * Math.exp(-value)
        }
    },

    ReLu
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

    Step
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
            //throw new ArithmeticException("Step Function's derivative is 0 for all {x|x ε 𝐑}");
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
