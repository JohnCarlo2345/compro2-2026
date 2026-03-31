package com.RPSGame.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RPSGameClient {
    public static void main(String[] args) {
        // I'll let the user type the server IP because my laptop and friend's use different ones
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter server IP address (type 'localhost' if playing on same computer): ");
        String serverIP = userInput.nextLine();
        final int PORT = 1234;

        try {
            // Connect to the server
            Socket clientSocket = new Socket(serverIP, PORT);
            System.out.println("Connected to game server!\n");

            // Set up to read messages from server and send our choice
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read messages the server sends us
            String serverMessage;
            while ((serverMessage = inFromServer.readLine()) != null) {
                System.out.println("SERVER: " + serverMessage);
                
                // If the server tells us to choose, send our pick
                if (serverMessage.contains("choose")) { // Simple check - works for our messages
                    System.out.print("Your choice: ");
                    String myChoice = userInput.nextLine();
                    outToServer.println(myChoice);
                }
            }

            // Close up when done
            clientSocket.close();
            userInput.close();

        } catch (UnknownHostException e) {
            System.out.println("Can't find that server - did you type the IP right?");
        } catch (IOException e) {
            System.out.println("Problem connecting to server: " + e.getMessage());
        }
    }
}
