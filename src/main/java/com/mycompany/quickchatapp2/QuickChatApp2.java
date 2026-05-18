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

        // Stop if registration failed
        if (!registrationResult.startsWith("Registration successful!")) {
            System.out.println("Please restart the application and try again.");
            scanner.close();
            return;
        }

       // ---- STEP 3: LOGIN ----
        System.out.println("\n--- LOGIN ---");

        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        // Show login result
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

        // Stop if login failed - users can only message if logged in
        if (!login.loginUser(loginUsername, loginPassword)) {
            System.out.println("Login failed. Please restart the application.");
            scanner.close();
            return;
        }

        // ---- STEP 4: MESSAGING (NEW IN PART 2) ----

        // Reset message counters for a fresh session
        Message.resetAll();

        // Ask how many messages the user wants to send
        System.out.print("\nHow many messages would you like to send? ");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Exiting.");
            scanner.close();
            return;
        }

        boolean running = true;

        // ---- MAIN MENU LOOP ----
        while (running) {

            System.out.println("\n===========================================");
            System.out.println("Welcome to QuickChat.\u2122");
            System.out.println("===========================================");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {

                // ---- OPTION 1: SEND MESSAGES ----
                case "1":
                    int sent = 0;

                    while (sent < numMessages) {
                        System.out.println("\n--- Message " + (sent + 1) + " of " + numMessages + " ---");

                        // Get and validate recipient number
                        String recipient = "";
                        while (true) {
                            System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                            recipient = scanner.nextLine().trim();

                            // Use a temp Message just to call checkRecipient
                            Message tempMsg = new Message(recipient, "temp");
                            String recipientCheck = tempMsg.checkRecipient(recipient);
                            System.out.println(recipientCheck);
                            Message.resetAll(); // undo the temp message

                            if (recipientCheck.equals("Cell phone number successfully captured.")) {
                                break; // valid number - move on
                            }
                            System.out.println("Please try again.");
                        }

                        // Get and validate message text
                        String messageText = "";
                        while (true) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageText = scanner.nextLine().trim();

                            // Use a temp Message just to call checkMessageLength
                            Message tempMsg2 = new Message(recipient, messageText);
                            String lengthCheck = tempMsg2.checkMessageLength(messageText);
                            System.out.println(lengthCheck);
                            Message.resetAll(); // undo the temp message

                            if (lengthCheck.equals("Message ready to send.")) {
                                break; // valid message - move on
                            }
                            System.out.println("Please re-enter your message.");
                        }

                        // Create the real message now that both fields are valid
                        Message msg = new Message(recipient, messageText);

                        // Show full message details
                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID:   " + msg.getMessageID());
                        System.out.println("Message Hash: " + msg.getMessageHash());
                        System.out.println("Recipient:    " + msg.getRecipient());
                        System.out.println("Message:      " + msg.getMessageText());

                        // Ask what to do with the message
                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose an option: ");

                        int sendChoice = 0;
                        try {
                            sendChoice = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid option. Discarding message.");
                            sendChoice = 2;
                        }

                        // Show result of the user's choice
                        System.out.println(msg.sentMessage(sendChoice));
                        sent++; // count this message regardless of what was chosen
                    }

                    // Show total messages sent after all messages are done
                    System.out.println("\nTotal messages sent: " + Message.getTotalMessagesSent());
                    break;

                // ---- OPTION 2: SHOW RECENT MESSAGES (COMING SOON) ----
                case "2":
                    System.out.println("Coming Soon.");
                    break;

                // ---- OPTION 3: QUIT ----
                case "3":
                    running = false;
                    System.out.println("\n===========================================");
                    System.out.println("        Thank you for using QuickChat     ");
                    System.out.println("===========================================");
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        scanner.close();
    }
}
