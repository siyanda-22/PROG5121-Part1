/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchatapp2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Message class - PROG5121 Part 2
 * Handles creating, sending, storing and displaying messages.
 * @author Student
 */
public class Message {

    // ---- Static fields shared across all messages ----
    private static ArrayList<String[]> sentMessagesList = new ArrayList<>();
    private static int totalMessagesSent = 0;
    private static int messageCounter = 0;

    // ---- Instance fields for each individual message ----
    private String messageID;
    private String messageHash;
    private String recipient;
    private String messageText;
    private int messageNumber;

    /**
     * Constructor - creates a new message.
     * Auto-generates the message ID, message number and message hash.
     * @param recipient the recipient cell phone number
     * @param messageText the message content
     */
    public Message(String recipient, String messageText) {
        messageCounter++;
        this.messageNumber = messageCounter;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // =============================================
    // Private helper - generateMessageID
    // =============================================

    /**
     * Generates a random 10-digit message ID.
     * Reference: Oracle Java SE 8 Random class
     * https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
     * @return 10-digit ID as a String
     */
    private String generateMessageID() {
        Random random = new Random();
        long id = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    // =============================================
    // Method 1: checkMessageID
    // =============================================

    /**
     * Checks that the message ID is not more than 10 characters.
     * @param messageID the ID to check
     * @return true if 10 characters or less, false otherwise
     */
    public boolean checkMessageID(String messageID) {
        if (messageID == null) {
            return false;
        }
        return messageID.length() <= 10;
    }

    // =============================================
    // Method 2: checkRecipient
    // =============================================

    /**
     * Checks the recipient cell number has an international code
     * and is the correct length.
     * Reference: Oracle Java SE 8 Pattern class
     * https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
     * @param recipient the cell number to check
     * @return success or failure message
     */
    public String checkRecipient(String recipient) {
        if (recipient != null && recipient.matches("^\\+[0-9]{1,3}[0-9]{7,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // =============================================
    // Method 3: checkMessageLength
    // =============================================

    /**
     * Checks the message does not exceed 250 characters.
     * @param message the message text to check
     * @return "Message ready to send." OR error showing how many chars over
     */
    public String checkMessageLength(String message) {
        if (message == null || message.length() <= 250) {
            return "Message ready to send.";
        }
        int over = message.length() - 250;
        return "Message exceeds 250 characters by " + over + ": please reduce the size.";
    }

    // =============================================
    // Method 4: createMessageHash
    // =============================================

    /**
     * Creates and returns the Message Hash.
     * Format: First 2 digits of ID : message number : FIRSTWORD+LASTWORD (all caps)
     * Example: 00:1:HITONIGHT
     * Reference: Oracle Java SE 8 String class
     * https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
     * @return the message hash in uppercase
     */
    public String createMessageHash() {
        // Get first 2 characters of the message ID
        String idPart = messageID.substring(0, 2);

        // Split the message into words
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");

        // Build hash and return in uppercase
        String hash = idPart + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // =============================================
    // Method 5: sentMessage
    // =============================================

    /**
     * Allows the user to send, disregard, or store the message.
     * @param choice 1 = Send, 2 = Disregard, 3 = Store
     * @return the appropriate status message
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            // Send - add to sent list and increase total count
            totalMessagesSent++;
            sentMessagesList.add(new String[]{
                messageID,
                messageHash,
                recipient,
                messageText
            });
            return "Message successfully sent.";

        } else if (choice == 2) {
            // Disregard - do not send or store
            return "Press 0 to delete the message.";

        } else if (choice == 3) {
            // Store for later
            storeMessage();
            return "Message successfully stored.";

        } else {
            return "Invalid option selected.";
        }
    }

    // =============================================
    // Method 6: storeMessage
    // =============================================

    /**
     * Stores the message for sending later.
     * Saved into the sentMessagesList with a STORED tag.
     */
    public void storeMessage() {
        sentMessagesList.add(new String[]{
            messageID,
            messageHash,
            recipient,
            "[STORED] " + messageText
        });
    }

    // =============================================
    // Method 7: printMessages
    // =============================================

    /**
     * Returns all messages sent during the session as a formatted string.
     * @return formatted string of all sent messages
     */
    public String printMessages() {
        if (sentMessagesList.isEmpty()) {
            return "No messages sent yet.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n========== Sent Messages ==========\n");

        for (String[] msg : sentMessagesList) {
            sb.append("Message ID:   ").append(msg[0]).append("\n");
            sb.append("Message Hash: ").append(msg[1]).append("\n");
            sb.append("Recipient:    ").append(msg[2]).append("\n");
            sb.append("Message:      ").append(msg[3]).append("\n");
            sb.append("------------------------------------\n");
        }

        return sb.toString();
    }

    // =============================================
    // Method 8: returnTotalMessages
    // =============================================

    /**
     * Returns the total number of messages sent.
     * @return total messages sent as int
     */
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // =============================================
    // Static getter - used in QuickChatApp2
    // =============================================

    /**
     * Static method to get total messages sent without needing an instance.
     * @return total messages sent
     */
    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    // =============================================
    // Getters - used in tests and main app
    // =============================================

    public String getMessageID()     { return messageID;     }
    public String getMessageHash()   { return messageHash;   }
    public String getRecipient()     { return recipient;     }
    public String getMessageText()   { return messageText;   }
    public int    getMessageNumber() { return messageNumber; }

    // =============================================
    // resetAll - used between unit tests
    // =============================================

    /**
     * Resets all static fields.
     * Called in unit tests before each test to start fresh.
     */
    public static void resetAll() {
        sentMessagesList.clear();
        totalMessagesSent = 0;
        messageCounter = 0;
    }
}
