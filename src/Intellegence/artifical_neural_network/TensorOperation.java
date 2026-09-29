package Intellegence.artifical_neural_network;

enum TensorOperation 
{
    ADDITION
    {
        @Override
        double[] apply(double[] matrixA, double[] matrixB) 
        {   
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'apply'");
        }
    } ,  

    SUBTRACTION
    {
        @Override
        double[] apply(double[] matrixA, double[] matrixB) 
        {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'apply'");
        }
    } ,   
    
    DOT_PRODUCT
    {
        @Override
        double[] apply(double[] matrixA, double[] matrixB) 
        {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'apply'");
        }
    } , 
    
    TRANSPOSE
    {
        @Override
        double[] apply(double[] matrixA, double[] matrixB) 
        {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'apply'");
        }
    } ,

    INVERSE
    {
        @Override
        double[] apply(double[] matrixA, double[] matrixB) 
        {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'apply'");
        }
    }; 

    abstract double[] apply(double[] matrixA, double[] matrixB);

}
