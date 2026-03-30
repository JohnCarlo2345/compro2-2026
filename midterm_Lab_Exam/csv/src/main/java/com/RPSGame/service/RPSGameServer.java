package com.RPSGame.service;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RPSGameServer {
    public static void main(String[] args) {
        // Kinda hardcoded port but teacher said it's fine for this project
        final int PORT = 1234;
        ServerSocket gameServer = null;
        Socket player1 = null;
        Socket player2 = null;

        System.out.println("=== Rock-Paper-Scissors Server Starting Up ===");
        System.out.println("Waiting for TWO players to join...\n");

        try {
            // Set up the server socket - took me a while to remember try-catch here
            gameServer = new ServerSocket(PORT);
            
            // First player connects
            player1 = gameServer.accept();
            System.out.println("Player 1 joined from: " + player1.getInetAddress());
            sendMessageToPlayer(player1, "Hey you're PLAYER 1! Wait for Player 2 to join...");

            // Second player connects
            player2 = gameServer.accept();
            System.out.println("Player 2 joined from: " + player2.getInetAddress());
            sendMessageToPlayer(player2, "Hey you're PLAYER 2! Let's start - choose ROCK, PAPER, or SCISSORS (all caps plz)");
            sendMessageToPlayer(player1, "Player 2 is here! Choose ROCK, PAPER, or SCISSORS (all caps plz)");

            // Get both players' choices
            String p1Choice = getPlayerChoice(player1);
            String p2Choice = getPlayerChoice(player2);

            // Print choices to server console (for testing - I forgot to add this first then had to edit)
            System.out.println("\nPlayer 1 chose: " + p1Choice);
            System.out.println("Player 2 chose: " + p2Choice);

            // Figure out who wins - made this logic step-by-step so I don't mess up
            String result = decideWinner(p1Choice, p2Choice);
            
            // Send results to both players
            sendMessageToPlayer(player1, "\nYour choice: " + p1Choice + " | Opponent's choice: " + p2Choice + "\n" + result);
            sendMessageToPlayer(player2, "\nYour choice: " + p2Choice + " | Opponent's choice: " + p1Choice + "\n" + result);

            // Clean up connections - important so we don't leave stuff hanging
            player1.close();
            player2.close();
            gameServer.close();
            System.out.println("\nGame over! Server shutting down.");

        } catch (IOException e) {
            // Not super detailed error handling but better than nothing
            System.out.println("Oops something broke with the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Little helper method to send messages - saves typing the same code twice
    private static void sendMessageToPlayer(Socket player, String message) throws IOException {
        PrintWriter out = new PrintWriter(player.getOutputStream(), true);
        out.println(message);
    }

    // Another helper to get what the player typed
    private static String getPlayerChoice(Socket player) throws IOException {
        Scanner in = new Scanner(player.getInputStream());
        return in.nextLine().trim(); // Trim just in case someone adds spaces by accident
    }

    // The actual win logic - wrote this like how I think about the game irl
    private static String decideWinner(String p1, String p2) {
        // First check if it's a tie
        if (p1.equalsIgnoreCase(p2)) { // Used ignore case just in case someone forgets caps
            return "IT'S A TIE!";
        }

        // Now check all winning cases for Player 1
        if ((p1.equals("ROCK") && p2.equals("SCISSORS")) ||
            (p1.equals("PAPER") && p2.equals("ROCK")) ||
            (p1.equals("SCISSORS") && p2.equals("PAPER"))) {
            return "PLAYER 1 WINS!";
        } 
        // If none of the above, Player 2 must have won
        else {
            return "PLAYER 2 WINS!";
        }
    }
}

