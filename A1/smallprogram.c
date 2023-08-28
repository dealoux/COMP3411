#include <stdio.h>
#define MAX 40 // maximum name length

//function prototype
void display(char n[], int times);

// main function (does not need prototype) 
int main (void){ 
    char name[MAX]; // user's name 
    int loop=0; // number of loops

    printf("What is your name? "); 
    gets(name); // get user's name 
    printf("How many times shall I print it? "); 
    scanf("%d",&loop); // read no. of times
    display(name, loop); 

    return 0; 
} // end of main()

//function display (n)ame so many (t)imes 
void display (char n[], int t){ 
    int i=0; for (i=0; i<t; i++) printf ("%s ",n);
    printf ("\n");
}// end of display()