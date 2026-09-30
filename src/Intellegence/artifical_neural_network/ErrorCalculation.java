package Intellegence.artifical_neural_network;

public enum ErrorCalculation 
{
    SQUARED_ERROR
    {

        @Override
        double[] calculateError(double[] predicted, double[] actual) 
        {
            double[] error = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                error[i] = Math.pow(predicted[i] - actual[i], 2);
            }
            return error;
        }

        @Override
        double[] gradient(double[] predicted, double[] actual) 
        {
            double[] gradient = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                gradient[i] = 2 * (predicted[i] - actual[i]);
            }
            return gradient;
        }

    },

    MEAN_SQUARED_ERROR
    {

        @Override
        double[] calculateError(double[] predicted, double[] actual) 
        {
            double[] error = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                error[i] = Math.pow(predicted[i] - actual[i], 2);
            }
            return error;
        }

        @Override
        double[] gradient(double[] predicted, double[] actual) 
        {
            double[] gradient = new double[predicted.length];
            for(int i = 0; i < predicted.length; i++)
            {
                gradient[i] = -2 * (predicted[i] - actual[i]) / predicted.length;
            }
            return gradient;
        }

    };

    abstract double[] calculateError(double[] predicted, double[] actual);
    abstract double[] gradient(double[] predicted, double[] actual);
}
