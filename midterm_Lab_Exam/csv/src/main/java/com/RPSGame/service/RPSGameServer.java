package com.RPSGame.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class RPSGameServer {
    // 1. ABSTRACTION LAYER (INSIDE SERVER CLASS)
    public abstract static class GameMove {
        private final String moveName;


        public GameMove(String name) {
            this.moveName = name;
        }


        public String getMoveName() {
            return moveName;
        }


        public abstract int compare(GameMove other);
    }


    static class Rock extends GameMove {
        public Rock() {
            super("Rock");
        }


        @Override
        public int compare(GameMove other) {
            if (other instanceof Scissors) return 1;
            if (other instanceof Paper) return -1;
            return 0;
        }
    }


    static class Paper extends GameMove {
        public Paper() {
            super("Paper");
        }


        @Override
        public int compare(GameMove other) {
            if (other instanceof Rock) return 1;
            if (other instanceof Scissors) return -1;
            return 0;
        }
    }


    static class Scissors extends GameMove {
        public Scissors() {
            super("Scissors");
        }


        @Override
        public int compare(GameMove other) {
            if (other instanceof Paper) return 1;
            if (other instanceof Rock) return -1;
            return 0;
        }
    }
    // 2. ENCAPSULATION
    static class Player {
        private final String name;
        private int score;
        private GameMove currentMove;


        public Player(String name) {
            this.name = name;
            this.score = 0;
        }


        public String getName() { return name; }
        public int getScore() { return score; }
        public GameMove getCurrentMove() { return currentMove; }


        public void setCurrentMove(GameMove move) { this.currentMove = move; }
        public void incrementScore() { this.score++; }
    }


    static class GameSession {
        private final Player p1;
        private final Player p2;
        private int roundsPlayed;


        public GameSession(Player p1, Player p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.roundsPlayed = 0;
        }


        public String playRound() {
            roundsPlayed++;
            GameMove m1 = p1.getCurrentMove();
            GameMove m2 = p2.getCurrentMove();


            int result = m1.compare(m2);
            String outcome;


            switch (result) {
                case 1 -> {
                    p1.incrementScore();
                    outcome = p1.getName() + " WINS this round!";
                }
                case -1 -> {
                    p2.incrementScore();
                    outcome = p2.getName() + " WINS this round!";
                }
                default -> outcome = "It's a TIE!";
            }


            return outcome + " | SCORE: " + p1.getName() + " " + p1.getScore() + " - " + p2.getScore() + " " + p2.getName();
        }


        public boolean isGameOver() {
            return roundsPlayed >= 10;
        }


        public String getFinalResult() {
            if (p1.getScore() > p2.getScore()) return "GAME OVER! " + p1.getName() + " WINS THE GAME!";
            else if (p2.getScore() > p1.getScore()) return "GAME OVER! " + p2.getName() + " WINS THE GAME!";
            else return "GAME OVER! IT'S A DRAW!";
        }
    }
    // SERVER
    public static void main(String[] args) {
        final int PORT = 1234;


        ServerSocket gameServer = null;
        Socket player1 = null;
        Socket player2 = null;


        System.out.println("---- Rock-Paper-Scissors Server Starting Up ----");
        System.out.println("Waiting for TWO players to join...\n");


        try {
            gameServer = new ServerSocket(PORT);


            // First player connects
            player1 = gameServer.accept();
            System.out.println("Player 1 joined from: " + player1.getInetAddress());
            sendMessageToPlayer(player1, "Hey you're PLAYER 1! Wait for Player 2 to join...");


            // Second player connects
            player2 = gameServer.accept();
            System.out.println("Player 2 joined from: " + player2.getInetAddress());
            sendMessageToPlayer(player2, "Hey you're PLAYER 2!");
            sendMessageToPlayer(player1, "Player 2 is here!");


            // Create Players
            Player p1 = new Player("Player 1");
            Player p2 = new Player("Player 2");
            GameSession game = new GameSession(p1, p2);


            while (!game.isGameOver()) {
                // Ask to choose every round
                sendMessageToPlayer(player1, "Choose your move (0=Rock, 1=Paper, 2=Scissors)");
                sendMessageToPlayer(player2, "Choose your move (0=Rock, 1=Paper, 2=Scissors)");


                // Get choices
                String move1 = getPlayerChoice(player1);
                String move2 = getPlayerChoice(player2);


                // Set moves
                p1.setCurrentMove(createMove(move1));
                p2.setCurrentMove(createMove(move2));


                // Play round and show result
                String result = game.playRound();
                System.out.println(result);
                sendMessageToPlayer(player1, result);
                sendMessageToPlayer(player2, result);
            }
            // Final Result
            String finalRes = game.getFinalResult();
            sendMessageToPlayer(player1, finalRes);
            sendMessageToPlayer(player2, finalRes);


        } catch (IOException e) {
            System.out.println("Oops something broke with the server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up
            try {
                if (player1 != null) player1.close();
                if (player2 != null) player2.close();
                if (gameServer != null) gameServer.close();
                System.out.println("\nGame over! Server shutting down.");
            } catch (IOException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    // Helper method to send messages
    private static void sendMessageToPlayer(Socket player, String message) throws IOException {
        PrintWriter out = new PrintWriter(player.getOutputStream(), true);
        out.println(message);
    }
    // Helper method to get what the player typed
    private static String getPlayerChoice(Socket player) throws IOException {
        try (Scanner in = new Scanner(player.getInputStream())) {
            return in.nextLine().trim();
        }
    }
    // Helper method to create GameMove object
    public static GameMove createMove(String type) {
        return switch (type) {
            case "1" -> new Rock();
            case "2" -> new Paper();
            case "3" -> new Scissors();
            default -> new Rock();
        };
    }
}





