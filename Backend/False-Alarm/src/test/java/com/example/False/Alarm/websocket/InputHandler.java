package com.example.False.Alarm.websocket;

import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static String getUserInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
    
    public static String getUsername() {
        return getUserInput("Enter username:");
    }
    
    public static String getUserId() {
        return getUserInput("Enter user ID:");
    }
    
    public static String getMessage() {
        return getUserInput("Enter message:");
    }
    
    public static String getLocation() {
        return getUserInput("Enter location (latitude,longitude):");
    }
    
    public static void printAlert(String alert) {
        System.out.println("Alert: " + alert);
    }
    
    public static void printStatus(String status) {
        System.out.println("Status: " + status);
    }
}
