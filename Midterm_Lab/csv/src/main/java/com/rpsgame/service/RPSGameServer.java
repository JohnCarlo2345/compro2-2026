package com.rpsgame.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class RPSGameServer {
    public static void main(String[] args) {
        final int PORT = 1234;
       
        System.out.println("---- SERVER STARTING ----");
        System.out.println("Waiting for players...\n");


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
           
            Socket player1 = serverSocket.accept();
            System.out.println("Player 1 Connected");


            Socket player2 = serverSocket.accept();
            System.out.println("Player 2 Connected\n");


            String name1 = login(player1, "Player 1");
            String name2 = login(player2, "Player 2");


            if (name1 == null || name2 == null) {
                System.out.println("Login Failed");
                return;
            }


            Player p1 = new Player(name1);
            Player p2 = new Player(name2);
            GameSession game = new GameSession(p1, p2);


            while (!game.isGameOver()) {
               
                sendMsg(player1, "Choose: 0=Rock, 1=Paper, 2=Scissors");
                sendMsg(player2, "Choose: 0=Rock, 1=Paper, 2=Scissors");


                int move1 = Integer.parseInt(getInput(player1));
                int move2 = Integer.parseInt(getInput(player2));


                p1.setCurrentMove(createMove(move1));
                p2.setCurrentMove(createMove(move2));


                String result = game.playRound();
                System.out.println(result);
                sendMsg(player1, result);
                sendMsg(player2, result);
            }


            String finalResult = game.getFinalResult();
            sendMsg(player1, finalResult);
            sendMsg(player2, finalResult);
           
            String leaderboard = "\n===== FINAL SCORE =====\n"
                                + p1.getName() + ": " + p1.getScore() + "\n"
                                + p2.getName() + ": " + p2.getScore() + "\n"
                                + "========================\n";
                               
            System.out.println(leaderboard);
            sendMsg(player1, leaderboard);
            sendMsg(player2, leaderboard);


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static GameMove createMove(int choice) {
        switch(choice) {
            case 0: return new Rock();
            case 1: return new Paper();
            case 2: return new Scissors();
            default: return new Rock();
        }
    }


    private static String login(Socket socket, String playerName) throws IOException {
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


        String user = in.nextLine();
        String pass = in.nextLine();


        System.out.println(playerName + " - User: " + user);


        if (!user.isEmpty() && !pass.isEmpty()) {
            out.println("LOGIN SUCCESS! Welcome " + user + "!");
            return user;
        } else {
            out.println("INVALID");
            return null;
        }
    }


    private static void sendMsg(Socket s, String msg) throws IOException {
        new PrintWriter(s.getOutputStream(), true).println(msg);
    }


    private static String getInput(Socket s) throws IOException {
        return new Scanner(s.getInputStream()).nextLine();
    }
}





