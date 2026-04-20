package com.multithreading.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    // Constructor
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Setup I/O Streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            String message;
            // Keep listening until client disconnects
            while ((message = input.readLine()) != null) {
                System.out.println("Received: " + message);
                
                // Echo message back or send to others
                output.println("Server: " + message);
            }

        } catch (IOException e) {
            System.out.println("Client Disconnected!");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}




