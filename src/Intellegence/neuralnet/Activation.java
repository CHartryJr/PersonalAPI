package Intellegence.neuralnet;

/**
 * Enum representing various activation functions used in neural networks.
 * Each activation function transforms an input value non-linearly and provides 
 * a derivative function for use in backpropagation during training.
 */
public enum Activation 
{
    /**
     * Sigmoid activation function.
     * Maps any real-valued number into the range (0,1), making it useful for probability-based outputs.
     * However, it suffers from vanishing gradients for extreme values.
     */
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
            double sigmoidValue = 1 / (1 + Math.exp(-value)); // Compute sigmoid output
            return sigmoidValue * (1 - sigmoidValue);  // Compute derivative of sigmoid
        }
    },

    /**
     * Hyperbolic tangent (tanh) activation function.
     * Similar to sigmoid but maps values to the range (-1,1), resulting in a zero-centered output,
     * which can help improve training efficiency compared to sigmoid.
     */
    HYPER_TANGENT
    {
        @Override
        double apply(double value) 
        {
            if (!Double.isFinite(value)) return 0; // Handle NaN or Infinity
            value = Math.max(-500, Math.min(500, value)); // Clamp input to prevent overflow
            return Math.tanh(value); // Hyperbolic tangent function
        }

        @Override
        double derive(double value) 
        {
            double tanhValue = Math.tanh(value); // Compute tanh value
            return 1 - tanhValue * tanhValue; // Compute derivative of tanh
        }
    },

    /**
     * Hailstone activation function (non-standard).
     * Applies the Collatz sequence transformation to integer inputs.
     * While not commonly used in neural networks, it demonstrates a unique transformation approach.
     */
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
            return value % 2 == 0 ? 0.5 : 3; // Derivative approximation for Hailstone
        }
    },

    /**
     * Rectified Linear Unit (ReLU) activation function.
     * Outputs the input value if it is positive; otherwise, returns zero.
     * ReLU helps mitigate vanishing gradient issues but can suffer from "dying ReLU" where neurons become inactive.
     */
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

    /**
     * Step activation function.
     * A binary threshold function that outputs 1 for positive inputs and 0 otherwise.
     * Useful for simple decision boundaries but unsuitable for gradient-based learning due to a zero derivative.
     */
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
            return 0; // Derivative of step function is always 0, making it unsuitable for gradient-based learning
        }
    };

    /**
     * Applies the activation function to an input value.
     * 
     * @param value Input value to be transformed.
     * @return The activated output after applying the function.
     */
    abstract double apply(double value);

    /**
     * Computes the derivative of the activation function.
     * 
     * @param value Input value for derivative computation.
     * @return The derivative of the activation function with respect to the input.
     */
    abstract double derive(double value);
}
