/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchatapp2;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class QuickChatApp2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===========================================");
        System.out.println("        Welcome to QuickChat App          ");
        System.out.println("===========================================");

        // ---- STEP 1: GET NAME ----
        System.out.print("\nEnter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        // Create Login object with the user's name
        Login login = new Login(firstName, lastName);

        // ---- STEP 2: REGISTRATION ----
        System.out.println("\n--- REGISTRATION ---");

        System.out.print("Enter a username (must contain _ and be max 5 characters): ");
        String username = scanner.nextLine();

        if (login.checkUserName(username)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.");
        }

        System.out.print("Enter a password (min 8 chars, must have capital, number, special character): ");
        String password = scanner.nextLine();

        if (login.checkPasswordComplexity(password)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.");
        }

        System.out.print("Enter your cell phone number with international code (e.g. +27838968976): ");
        String cellPhone = scanner.nextLine();

        if (login.checkCellPhoneNumber(cellPhone)) {
            System.out.println("Cell number successfully captured.");
        } else {
            System.out.println("Cell number is incorrectly formatted or does not contain an international code; "
                    + "please correct the number and try again.");
        }

        // Attempt registration and show result
        String registrationResult = login.registerUser(username, password, cellPhone);
        System.out.println("\nRegistration Status: " + registrationResult);

        // ---- STEP 3: LOGIN ----
        System.out.println("\n--- LOGIN ---");

        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        // Show login result
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

        System.out.println("\n===========================================");
        System.out.println("        Thank you for using QuickChat     ");
        System.out.println("===========================================");

        scanner.close();
    }
}
