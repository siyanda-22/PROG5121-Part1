/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchatapp2;

import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Message class - PROG5121 Part 2 and Part 3
 * Handles creating, sending, storing, searching and reporting on messages.
 *
 
 *
 * @author Student
 */
public class Message {

    // ---- Static arrays shared across all messages (PROG5121 Part 3 requirement) ----
    private static ArrayList<Message> sentMessages        = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages      = new ArrayList<>();
    private static ArrayList<String>  messageHashes       = new ArrayList<>();
    private static ArrayList<String>  messageIDs          = new ArrayList<>();

    private static int totalMessagesSent = 0;
    private static int messageCounter    = 0;

    // File used to store messages (Part 3 requirement)
    private static final String STORAGE_FILE = "stored_messages.json";

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
     * @param recipient the cell number to check
     * @return success or failure message
     */
    public static String checkRecipient(String recipient) {
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
    public static String checkMessageLength(String message) {
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
     * @return the message hash in uppercase
     */
    public String createMessageHash() {
        String idPart = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");

        String hash = idPart + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // =============================================
    // Method 5: sentMessage
    // =============================================

    /**
     * Allows the user to send, disregard, or store the message.
     * Populates the Sent, Disregarded, Stored, Hash and ID arrays
     * as required by PROG5121 Part 3.
     * @param choice 1 = Send, 2 = Disregard, 3 = Store
     * @return the appropriate status message
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            // Send - add to sent list and the hash/id arrays
            totalMessagesSent++;
            sentMessages.add(this);
            messageHashes.add(this.messageHash);
            messageIDs.add(this.messageID);
            return "Message successfully sent.";

        } else if (choice == 2) {
            // Disregard - add to disregarded list only
            disregardedMessages.add(this);
            return "Press 0 to delete the message.";

        } else if (choice == 3) {
            // Store - add to stored list, hash/id arrays, and save to JSON file
            storedMessages.add(this);
            messageHashes.add(this.messageHash);
            messageIDs.add(this.messageID);
            storeMessage();
            return "Message successfully stored.";

        } else {
            return "Invalid option selected.";
        }
    }

    // =============================================
    // Method 6: storeMessage - saves storedMessages to a JSON file
    // =============================================

    
    public void storeMessage() {
        JSONArray jsonArray = new JSONArray();

        for (Message msg : storedMessages) {
            JSONObject obj = new JSONObject();
            obj.put("messageID", msg.messageID);
            obj.put("messageHash", msg.messageHash);
            obj.put("recipient", msg.recipient);
            obj.put("message", msg.messageText);
            jsonArray.put(obj);
        }

        try (FileWriter file = new FileWriter(STORAGE_FILE)) {
            file.write(jsonArray.toString(2));
        } catch (IOException e) {
            System.out.println("Error saving messages to file: " + e.getMessage());
        }
    }

    // =============================================
    // loadStoredMessages - reads storedMessages back from the JSON file
    // =============================================

    
    public static void loadStoredMessages() {
        try (FileReader reader = new FileReader(STORAGE_FILE)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(tokener);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Message msg = new Message(obj.getString("recipient"), obj.getString("message"));
                // Overwrite the auto-generated ID and hash with the saved ones
                msg.messageID   = obj.getString("messageID");
                msg.messageHash = obj.getString("messageHash");

                storedMessages.add(msg);
                messageHashes.add(msg.messageHash);
                messageIDs.add(msg.messageID);
            }
        } catch (IOException e) {
            System.out.println("No stored messages file found yet.");
        }
    }

    // =============================================
    // Part 3 - a) Display sender and recipient of all stored messages
    // =============================================

    /**
     * Returns the sender and recipient of every stored message.
     * @return formatted string of sender/recipient pairs
     */
    public static String printStoredSenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message msg : storedMessages) {
            sb.append("Sender: You | Recipient: ").append(msg.recipient).append("\n");
        }
        return sb.toString();
    }

    // =============================================
    // Part 3 - b) Display the longest message
    // =============================================

    /**
     * Finds and returns the longest message out of all sent,
     * disregarded and stored messages.
     * @return the longest message text
     */
    public static String getLongestMessage() {
        String longest = "";

        ArrayList<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(disregardedMessages);
        allMessages.addAll(storedMessages);

        for (Message msg : allMessages) {
            if (msg.messageText.length() > longest.length()) {
                longest = msg.messageText;
            }
        }

        if (longest.isEmpty()) {
            return "No messages found.";
        }
        return longest;
    }

    // =============================================
    // Part 3 - c) Search for a message by Message ID
    // =============================================

    /**
     * Searches all messages for a given message ID and returns
     * the recipient and message text.
     * @param id the message ID to search for
     * @return recipient and message, or not found message
     */
    public static String searchByMessageID(String id) {
        ArrayList<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(disregardedMessages);
        allMessages.addAll(storedMessages);

        for (Message msg : allMessages) {
            if (msg.messageID.equals(id)) {
                return msg.messageText;
            }
        }
        return "No message found with that ID.";
    }

    // =============================================
    // Part 3 - d) Search for all messages for a particular recipient
    // =============================================

    /**
     * Searches sent and stored messages for a given recipient and
     * returns all matching messages.
     * @param recipient the recipient cell number to search for
     * @return all messages for that recipient
     */
    public static String searchByRecipient(String recipient) {
        ArrayList<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages);

        StringBuilder sb = new StringBuilder();
        for (Message msg : allMessages) {
            if (msg.recipient.equals(recipient)) {
                sb.append(msg.messageText).append("\n");
            }
        }

        if (sb.length() == 0) {
            return "No messages found for that recipient.";
        }
        return sb.toString().trim();
    }

    // =============================================
    // Part 3 - e) Delete a message using the message hash
    // =============================================

    /**
     * Deletes a message from sent, disregarded or stored arrays
     * using its message hash.
     * @param hash the message hash to delete
     * @return success or not found message
     */
    public static String deleteByMessageHash(String hash) {
        for (Message msg : sentMessages) {
            if (msg.messageHash.equals(hash)) {
                sentMessages.remove(msg);
                return "Message: \"" + msg.messageText + "\" successfully deleted.";
            }
        }
        for (Message msg : disregardedMessages) {
            if (msg.messageHash.equals(hash)) {
                disregardedMessages.remove(msg);
                return "Message: \"" + msg.messageText + "\" successfully deleted.";
            }
        }
        for (Message msg : storedMessages) {
            if (msg.messageHash.equals(hash)) {
                storedMessages.remove(msg);
                return "Message: \"" + msg.messageText + "\" successfully deleted.";
            }
        }
        return "No message found with that hash.";
    }

    // =============================================
    // Part 3 - f) Display a report of all sent messages
    // =============================================

    /**
     * Returns a full report listing the message hash, recipient,
     * and message text of every sent message.
     * @return formatted report string
     */
    public static String generateReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to report.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n========== Sent Messages Report ==========\n");

        for (Message msg : sentMessages) {
            sb.append("Message Hash: ").append(msg.messageHash).append("\n");
            sb.append("Recipient:    ").append(msg.recipient).append("\n");
            sb.append("Message:      ").append(msg.messageText).append("\n");
            sb.append("--------------------------------------------\n");
        }

        return sb.toString();
    }

    // =============================================
    // Method 7: printMessages
    // =============================================

    /**
     * Returns all sent messages as a formatted string.
     * @return formatted string of all sent messages
     */
    public String printMessages() {
        return generateReport();
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

    public static ArrayList<Message> getSentMessages()        { return sentMessages; }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages()      { return storedMessages; }
    public static ArrayList<String>  getMessageHashes()       { return messageHashes; }
    public static ArrayList<String>  getMessageIDs()          { return messageIDs; }

    // =============================================
    // resetAll - used between unit tests
    // =============================================

    /**
     * Resets all static arrays and counters.
     * Called in unit tests before each test to start fresh.
     */
    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessagesSent = 0;
        messageCounter = 0;
    }
}
