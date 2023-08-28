import java.*; 
import java.io.*;
import java.util.Random;

// This program creates a thread for each calculation of an element in a matrix multilication
public class MatrixMultiplication
{
  static final int MATRIX_DIMENSION = 3; // 2 for 2x2 matrix, 3 for 3x3 matrix, 4 for 4x4 matrix
  static final int MATRIX_MAX_VALUE = 10; // maximum value of a element inside a matrix
  static Random random = new Random(); // random for choosing an element in a matrix

  static int[][] source = initMatrix();; // source matrix
  static int[][] target = initMatrix();; // target matrix
  static int[][] result = new int[MATRIX_DIMENSION][MATRIX_DIMENSION]; // blank matrix for storing the result of the multiplication
  static Thread[] threads = new Thread[MATRIX_DIMENSION * MATRIX_DIMENSION]; // threads array

  // base Worker class
  static class Worker implements Runnable{
    int id;
    
    public void run(){
      try { Thread.sleep(500); } // sleep for 500 ms
      catch (InterruptedException ie) {}

      System.out.println("I am the " + id + "'th worker thread");
    }
  } // end of Worker class

  // This class is a thread specified in multiplying the target matrix to the source matrix, storing the result inside the result matrix
  static class Multiplier extends Worker{
    int row, col;

    // Constructor
    Multiplier (int id, int row, int col){
      this.id = id;
      this.row = row;
      this.col = col;
    }

    // This function multiplies the source and target matrixes correspond to element indexed at result[row][col] 
    void calculateElement(){
      for(int i=0; i<MATRIX_DIMENSION; i++){
        result[row][col] += source[row][i] * target[i][col];
      }
    }

    @Override
    public void run(){
      super.run();

      System.out.println("Calculating the element at (" + row + ", " + col + ")");
      calculateElement(); // call the 
    }
  }

  // This function creates and fills a new matrix base on the contants MATRIX_DIMENSION and MATRIX_MAX_VALUE, then returns the matrix
  public static int[][] initMatrix(){
    int[][] matrix = new int[MATRIX_DIMENSION][MATRIX_DIMENSION];

    // loop through the matrix and randomize each element
    for(int r = 0; r< matrix.length; r++){
      for(int c = 0; c< matrix[r].length; c++){
        matrix[r][c] = random.nextInt(MATRIX_MAX_VALUE);
      }
    }

    return matrix;
  }

  // This function loops through and prints each elements in the given matrix
  public static void printMatrix(int[][] matrix){
    for(int r = 0; r< matrix.length; r++){
      for(int c = 0; c< matrix[r].length; c++){
        System.out.print(matrix[r][c] +  " ");
      }
      System.out.print("\n");
    }
  }

  // This functions starts and waits for a thread specified with the given id
  static void startThread(int id){
    threads[id].start();

    try{
      threads[id].join();
    } catch(InterruptedException ie) {}
    System.out.println("Thread " + ++id + " is done\n"); 
  }

  // This function creates a test run for the matrix multiplication
  static void MultiplicationTest(){
    int id = 0;

    System.out.println("Testing the multiplication of " + MATRIX_DIMENSION + "D matrixes\n");
    
    System.out.println("Printing the source matrix");
    printMatrix(source);
    
    System.out.println("Printing the target matrix");
    printMatrix(target);

    // matrix multiplication threads
    // loops through the elements in the result matrix, creates and run the thread correspond to each element
    for(int r=0; r<MATRIX_DIMENSION; r++){
      for(int c=0; c<MATRIX_DIMENSION; c++){
        threads[id] = new Thread(new Multiplier(id+1, r, c));
        startThread(id++);
      }
    }
  }

  public static void main(String args[]) { 
    MultiplicationTest(); // call the test
    System.out.println("I am the main thread");

    // print out the result matrix for verification
    System.out.println("Printing the result matrix");
    printMatrix(result);
  } // end of main function
} // end of MatrixMultiplication class
