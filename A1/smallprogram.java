import java.*; import java.io.*;

public class smallprogram
{
  public static void main(String[] args)
  {
    String opinion = " likes Linux! "; 
    char first=' ', last=' '; // user's initials 
    int loop = 20; // number of times to repeat
    
    try{
      System.out.print("Your first initial? "); 
      first = (char)System.in.read();
      System.in.read();
      System.out.print("Your last initial? "); 
      last = (char)System.in.read();
      System.in.read();
    }
    catch (IOException e){}

    System.out.println("Repeating "+loop+" times.");
    for (int i=0; i<loop; i++){
      System.out.print(first+"."+last+"."+opinion);
    }
    System.out.println();
  } // end of main()
} // end of smallprogram