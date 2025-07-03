package com.example.False.Alarm.websocket;

import java.util.Scanner;

public class InputHandler {

    private static final Scanner scanner = new Scanner(System.in);

    public static String getUserId() {
        System.out.print("Enter user ID: ");
        return scanner.nextLine();
    }

    public static String getUsername() {
        System.out.print("Enter username: ");
        return scanner.nextLine();
    }

    public static String getMessage() {
        System.out.print("Enter message: ");
        return scanner.nextLine();
    }

    public static void printAlert(String alert) {
        System.out.println("ALERT: " + alert);
    }

    public static void printStatus(String status) {
        System.out.println(status);
    }
}
