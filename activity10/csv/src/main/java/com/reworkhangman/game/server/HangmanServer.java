package com.reworkhangman.game.server;

import model.GameState;
import model.User;
import service.AuthService;
import service.GameService;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HangmanServer {
    private static final int PORT = 12345;
    private AuthService authService;
    private GameService gameService;

    public HangmanServer() {
        authService = new AuthService();
        gameService = new GameService();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Hangman Server running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle each client connection in a separate thread
    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private User currentUser;
        private GameState currentGame;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Step 1-4: Authentication
                out.println("SEND_CREDENTIALS");
                String username = in.readLine();
                String password = in.readLine();
                currentUser = authService.validateUser(username, password);

                if (currentUser == null) {
                    out.println("AUTH_FAILED");
                    return;
                }
                out.println("AUTH_SUCCESS");
                out.println("WELCOME: Your current score is " + currentUser.getScore());

                // Step 5: Initialize game
                String targetWord = gameService.selectRandomWord();
                currentGame = new GameState(targetWord);
                out.println("GAME_READY");
                out.println("MASKED_WORD: " + currentGame.getMaskedWord());
                out.println("ATTEMPTS_LEFT: " + currentGame.getRemainingAttempts());

                // Step 6: Game loop
                while (!currentGame.isGameOver()) {
                    out.println("SEND_GUESS");
                    String guessInput = in.readLine();
                    if (guessInput == null || guessInput.length() != 1 || !Character.isLetter(guessInput.charAt(0))) {
                        out.println("INVALID_INPUT: Enter a single letter");
                        continue;
                    }
                    char guess = guessInput.charAt(0);

                    if (currentGame.hasGuessedLetter(guess)) {
                        out.println("ALREADY_GUESSED");
                        continue;
                    }

                    boolean isCorrect = currentGame.checkGuess(guess);
                    out.println(isCorrect ? "GUESS_CORRECT" : "GUESS_WRONG");
                    out.println("MASKED_WORD: " + currentGame.getMaskedWord());
                    out.println("ATTEMPTS_LEFT: " + currentGame.getRemainingAttempts());
                }

                // Game end handling
                if (currentGame.isGameWon()) {
                    gameService.updateScore(currentUser, currentGame);
                    out.println("GAME_WON: You won! New score: " + currentUser.getScore());
                } else {
                    out.println("GAME_LOST: The word was " + currentGame.getTargetWord());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new HangmanServer().start();
        
    }
}


