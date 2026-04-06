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
        try {
            try ( // Let user type IP address
                Scanner userInput = new Scanner(System.in)) {
                System.out.print("Enter server IP address (type 'localhost' if playing on same computer): ");
                String serverIP = userInput.nextLine();
                final int PORT = 1234;
                try ( // Connect to server
                    Socket clientSocket = new Socket(serverIP, PORT)) {
                    System.out.println("Connected to game server!\n");
                    // Setup streams
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
                    String serverMessage;
                    // Read messages the server sends us
                    while ((serverMessage = inFromServer.readLine()) != null) {
                        System.out.println("SERVER: " + serverMessage);
                        // If server tells us to choose, send our pick
                        if (serverMessage.contains("choose")) {
                            System.out.print("Your choice: ");
                            String myChoice = userInput.nextLine();
                            outToServer.println(myChoice);
                        }
                        // If game over
                        if (serverMessage.startsWith("GAME OVER")) {
                            break;
                        }
                    }
                    // Close up when done
                    clientSocket.close();
                    userInput.close();
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Can't find that server - did you type the IP right?");
        } catch (IOException e) {
            System.out.println("Problem connecting to server: " + e.getMessage());
        }
    }
}




