import java.*; 
import java.io.*;
import java.util.Random;

// This program creates a thread for each calculation of an element in a matrix multilication
public class BoundedBuffer
{
  // Buffer variables
  static final int BUFFER_SIZE = 5;
  static int[] buffer = new int[BUFFER_SIZE];
  
  // Random value variables
  static Random random = new Random(); // random for choosing an element in a matrix
  static final int MAX_VALUE = 100; // maximum value of an element in the buffer
  
  /* Semaphores */
  static class Semaphore{ 
    public int value; 
  
    public Semaphore(int v){
        value = v;
    }

    public void accquire(){
      while(value <= 0)
        ;
      value--;
    }

    public void release(){
      value++;
    }
  } // end of Semaphore class
    
  static Semaphore mutex = new Semaphore(1); // A binary semaphore to ensure that the buffer is only modified by one thread at a time, 0 means locked, 1 means available
  static Semaphore empty = new Semaphore(BUFFER_SIZE); // A counting semaphore to check if there's empty slot in the buffer, ranging from [0, BUFFER_SIZE], 0 means the buffer is full, other values correspond to the number of available empty slots
  static Semaphore full = new Semaphore(0); // A counting semaphore to check if there's data in the buffer, ranging from [0, BUFFER_SIZE], 0 means the buffer is empty, other values correspond to the number of avaiable data

  // Threads variable
  static Thread[] threads = new Thread[2];
  public static final int THREAD_SLEEP_TIME = 1; // in milliseconds, to minimize chances of deadlocks
  
  // This class creates a thread, which acts as a Producer, indefinitely adds random data into the buffer
  static class Producer implements Runnable{
    @Override
    public void run(){
      while(true){
        try { 
          Thread.sleep(THREAD_SLEEP_TIME); // sleep for THREAD_SLEEP_TIME ms
          empty.accquire(); // check and wait for an available slot in the buffer
          mutex.accquire(); // acquire the lock

          // critical section
          int value = random.nextInt(MAX_VALUE);
          buffer[empty.value] = value;
          System.out.println("Added the value " + value + " to slot " + empty.value);
          //printBuffer();
        } 
        catch (InterruptedException ie) {}
        finally{
          mutex.release(); // release the lock  
          full.release(); // since a new value has now been added to the buffer, increase the full semaphore
        }
      } // end of while loop
    } // end of run()
  } // end of Producer class

  // This class creates a thread, which acts as a Consumer, indefinitely removes stored data from the buffer
  static class Consumer implements Runnable{
    @Override
    public void run(){
      while(true){
        try{
          Thread.sleep(THREAD_SLEEP_TIME); // sleep for THREAD_SLEEP_TIME ms
          full.accquire(); // check and wait for an available data from the buffer
          mutex.accquire(); // acquire the lock

          // critical section
          int index = BUFFER_SIZE - full.value - 1;
          int value = buffer[index]; // get the value
          buffer[index] = Integer.MIN_VALUE; // set the value to null
          System.out.println("Removed the value " + value + " from slot " + index);
          //printBuffer();
        }
        catch (InterruptedException ie) {}
        finally{
          mutex.release(); // release the lock  
          empty.release(); // since a data has now been removed from the buffer, increase the empty semaphore
        }
      } // end of while loop
    } // end of run()
  } // end of Consumer class

  // This function prints all the data in the buffer
  public static void printBuffer(){
    System.out.println("Printing the buffer");

    for(int data : buffer){
      System.out.print(data + " ");
    }

    System.out.println();
  }

  public static void main(String args[]) { 
    threads[0] = new Thread(new Producer()); // producer thread
    threads[1] = new Thread(new Consumer()); // consumer thread

    for(Thread thread : threads){
      thread.start();
    }
  } // end of main function
} // end of BoundedBuffer class
