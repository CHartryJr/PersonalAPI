package Intellegence.artifical_neural_network.Utilities;

public enum ErrorCalculation 
{
    MEAN_ABSOLUTE_ERROR
    {

        @Override
        public double[] calculateError(double[] predicted, double[] actual) 
        {
            double[] error = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                error[i] = Math.abs(predicted[i] - actual[i]);
            }
            return error;
        }

        @Override
        public double[] gradient(double[] predicted, double[] actual) 
        {
            double[] gradient = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                gradient[i] = (predicted[i] > actual[i]) ? 1 : -1;
            }
            return gradient;
        }

    },

    MEAN_SQUARED_ERROR
    {
        @Override
        public double[] calculateError(double[] predicted, double[] actual) 
        {
            double[] error = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                error[i] = Math.pow(predicted[i] - actual[i], 2)/predicted.length;
            }
            return error;
        }

        @Override
        public double[] gradient(double[] predicted, double[] actual) 
        {
            double[] gradient = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                gradient[i] = 2 * (predicted[i] - actual[i]) / predicted.length;
            }
            return gradient;
        }
    };

    public abstract double[] calculateError(double[] predicted, double[] actual);
    public abstract double[] gradient(double[] predicted, double[] actual);
}
