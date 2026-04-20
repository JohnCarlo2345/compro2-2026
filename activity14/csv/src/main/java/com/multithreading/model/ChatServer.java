package com.multithreading.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.multithreading.service.ClientHandler;

public class ChatServer {
    public static void main(String[] args) {
        try {
            // Create Server Socket
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server Started... Waiting for Clients...");

            while (true) {
                // Accept client connection
                Socket socket = serverSocket.accept();
                System.out.println("New Client Connected!");

                // CREATE A NEW THREAD FOR EACH CLIENT
                ClientHandler handler = new ClientHandler(socket);
                Thread thread = new Thread(handler);
                thread.start(); // This runs in background
            }

        } catch (IOException e) {
        }
    }
}


