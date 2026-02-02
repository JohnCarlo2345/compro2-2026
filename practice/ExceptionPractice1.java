package practice;
import java.util.Scanner;

public class ExceptionPractice1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter valid number: ");

        try {
             int number = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid number.");
        }


        System.out.print("Input another number: ");
        


        System.out.println("Progrsm shutting down. Good bye!");
    }
}

public static int inputNumber() {
    int number;
    Scanner sc = new Scanner(System.in);

    while(true) {
        try {
            number = sc.nextInt();
    }
}