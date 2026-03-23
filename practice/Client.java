import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    public static void main(String[] args) {
        String server = "localhost"; //same as 127.0.0.1
        int  port = 8000;

        try(Socket socket = new Socket(server, port); 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);
        ){
            
            System.out.println("Connected to the server. Welcome");

            while(true){
                //type your message
                System.out.println("Your turn (Client): ");
                String message = sc.nextLine();

                //send your message
                out.println(message);

                //wait for server response
                System.out.println("Waiting for server response...");

                //read server's reply
                String reply = in.readLine();

                //determine if server's quitting
                if(reply == null || reply.equalsIgnoreCase("/quit")){
                    System.out.println("Server disconnected...");
                    break;
                }


                //display server's reply
                System.out.println("Server: " + reply);
            }

            
        }catch(IOException e){
            System.out.println("Can't connect right now...");
        }

    }
}
