package com.rework.model;

import java.util.Scanner;
import org.json.JSONObject;
import java.io.FileWriter;

public class NestedMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double[] subjectGrades = new double[3];
        String[] subjects = {"COMPRO2", "DSA", "OOP"};
        int mainChoice, subChoice;
        final String JSON_FILE = "gradesData.json";
        loadGrades(subjectGrades, subjects, JSON_FILE);

        do {
            System.out.println("MAIN MENU:");
            System.out.println("[1] Enter Grades");
            System.out.println("[2] Display Grades");
            System.out.println("[3] Exit");
            System.out.print("Enter choice");
            mainChoice = scanner.nextInt();
        }
