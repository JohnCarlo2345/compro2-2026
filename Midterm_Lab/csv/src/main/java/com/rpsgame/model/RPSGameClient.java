package com.rpsgame.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class RPSGameClient {
    public static void main(String[] args) {
        try {
            Scanner userInput = new Scanner(System.in);
           
            System.out.print("Enter server IP: ");
            String serverIP = userInput.nextLine();
            final int PORT = 1234;
            Socket clientSocket = new Socket(serverIP, PORT);
            System.out.println("Connected!\n");


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            System.out.print("Enter Username: ");
            String user = userInput.nextLine();
            System.out.print("Enter Password: ");
            String pass = userInput.nextLine();


            out.println(user);
            out.println(pass);


            String response = in.readLine();
            System.out.println("SERVER: " + response);


            if (response.startsWith("INVALID")) {
                System.out.println("Login Failed!");
                clientSocket.close();
                return;
            }


            String msg;
            int round = 1;
            while ((msg = in.readLine()) != null) {
                System.out.println("SERVER: " + msg);


                if (msg.contains("Choose")) {
                    System.out.print("Enter Choice: ");
                    String choice = userInput.nextLine();
                    out.println(choice);
                }


                if (msg.startsWith("GAME OVER")) {
                    break;
                }
            }


            clientSocket.close();
            userInput.close();


        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}




