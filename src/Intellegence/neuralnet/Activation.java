package Intellegence.neuralnet;

public enum Activation 
{
    SIGMOID
    {
        @Override
        double apply(double value) 
        {
            if (!Double.isFinite(value)) return 0; // Handle NaN or Infinity
            value = Math.max(-500, Math.min(500, value)); // Clamp input to prevent overflow
            return 1 / (1 + Math.exp(-value));  // Sigmoid function
        }

        @Override
        double derive(double value) 
        {
            double sigmoidValue = 1 / (1 + Math.exp(-value)); // Sigmoid output
            return sigmoidValue * (1 - sigmoidValue);  // Corrected derivative
        }
    },

    HYPER_TANGENT
    {
        @Override
        double apply(double value) 
        {
            if (!Double.isFinite(value)) return 0; // Handle NaN or Infinity
            value = Math.max(-500, Math.min(500, value)); // Clamp input to prevent overflow
            return Math.tanh(value); // Hyperbolic tangent
        }

        @Override
        double derive(double value) 
        {
            double tanhValue = Math.tanh(value); // Compute tanh value
            return 1 - tanhValue * tanhValue; // Corrected derivative of tanh
        }
    },

    HAILSTONE
    {
        @Override
        double apply(double value) 
        {
            if (!Double.isFinite(value)) return 1; // Handle NaN or Infinity
            value = Math.floor(value);
            if (value <= 1) 
                return 1.0;
            return value % 2 == 0 ? value / 2 : 3 * value + 1;
        }

        @Override
        double derive(double value) 
        {
            value = Math.floor(value);
            if (value <= 1)
                return 0;
            return value % 2 == 0 ? 0.5 : 3; // Derivative for hailstone function
        }
    },

    RECTIFIED_LINEAR_UNIT
    {
        @Override
        double apply(double value) 
        {
            return value > 0 ? value : 0.0d; // ReLU function
        }

        @Override
        double derive(double value) 
        {
            return value > 0 ? 1.0d : 0.0d; // Derivative of ReLU
        }
    },

    STEP
    {
        @Override
        double apply(double value) 
        {
            return value > 0 ? 1.0d : 0.0d; // Step function
        }

        @Override
        double derive(double value) 
        {
            return 0; // Derivative of step function is always 0
        }
    };

    /**
     * Function used to apply activation functions.
     * @param value Input value.
     * @return Activated value.
     */
    abstract double apply(double value);

    /**
     * Function used to apply derivative functions.
     * @param value Input value.
     * @return Derivative of the activated value.
     */
    abstract double derive(double value);
}
