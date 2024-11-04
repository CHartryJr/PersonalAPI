package intellegence.enviroments.Practtical;
import java.io.*;


public class NumberRecognition
{
    // Example matrix dimensions (change as needed)
    private static final int MATRIX_SIZE = 5;
    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    // Constructor initializes the matrix with sample values (-2, -1, 0, 1, 2)
    public NumberRecognition() {
        // Example matrix, can be modified to represent different numbers
        int[][] exampleMatrix = {
            {0, -1, 2, -1, 0},
            {-1, 2, 2, 2, -1},
            {2, 2, 0, 2, 2},
            {-1, 2, 2, 2, -1},
            {0, -1, 2, -1, 0}
        };
        this.matrix = exampleMatrix;
    }

    // Function to return the matrix
    public int[][] getMatrix() {
        return matrix;
    }

    // Function to print the matrix to the console
    public void displayMatrix() {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    // Example function to identify the number represented by the matrix
    public int identifyNumber() {
        // Custom logic to analyze matrix values and determine number
        // This is a placeholder and should be adapted for specific patterns
        int sum = 0;
        for (int[] row : matrix) {
            for (int val : row) {
                sum += val;
            }
        }
        // Placeholder logic: sum determines number (e.g., sum=10 -> number is 5)
        return sum / 10; // Adjust as needed
    }

    // Save matrix to a file with ".maps" extension
    public void saveMatrixToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".rec"))) {
            for (int[] row : matrix) {
                for (int val : row) {
                    writer.write(val + " ");
                }
                writer.newLine();
            }
            System.out.println("Matrix saved to " + filename + ".rec");
        } catch (IOException e) {
            System.out.println("Error saving matrix: " + e.getMessage());
        }
    }

    // Load matrix from a file with ".maps" extension
    public void loadMatrixFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename + ".rec"))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < MATRIX_SIZE) {
                String[] values = line.trim().split(" ");
                for (int col = 0; col < values.length && col < MATRIX_SIZE; col++) {
                    matrix[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            System.out.println("Matrix loaded from " + filename + ".rec");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading matrix: " + e.getMessage());
        }
    }

}