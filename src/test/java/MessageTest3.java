/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

    package com.mycompany.quickchatapp2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MessagePart3Test - PROG5121 Part 3
 * Unit tests for arrays, search, delete and report features.
 * Uses exact test data from the task brief.
 * @author Student
 */
public class MessageTest3 {

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() {
        // Reset all arrays before every test
        Message.resetAll();

        // Test Data Message 1 - Sent
        // Recipient: +27834557896
        // Message: "Did you get the cake?"
        message1 = new Message("+27834557896", "Did you get the cake?");
        message1.sentMessage(1); // Sent

        // Test Data Message 2 - Stored
        // Recipient: +27838884567
        // Message: "Where are you? You are late! I have asked you to be on time."
        message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message2.sentMessage(3); // Stored

        // Test Data Message 3 - Disregard
        // Recipient: +27834484567
        // Message: "Yohoooo, I am at your gate."
        message3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        message3.sentMessage(2); // Disregard

        // Test Data Message 4 - Sent
        // Recipient: 0838884567
        // Message: "It is dinner time!"
        message4 = new Message("0838884567", "It is dinner time!");
        message4.sentMessage(1); // Sent

        // Test Data Message 5 - Stored
        // Recipient: +27838884567
        // Message: "Ok, I am leaving without you."
        message5 = new Message("+27838884567", "Ok, I am leaving without you.");
        message5.sentMessage(3); // Stored
    }

    // =============================================
    // Sent Messages array correctly populated
    // =============================================

    @Test
    public void testSentMessagesArrayPopulated() {
        // Sent array should contain message 1 and message 4
        assertEquals(2, Message.getSentMessages().size());

        String text1 = Message.getSentMessages().get(0).getMessageText();
        String text2 = Message.getSentMessages().get(1).getMessageText();

        assertEquals("Did you get the cake?", text1);
        assertEquals("It is dinner time!", text2);
    }

    // =============================================
    // Display the longest message
    // =============================================

    @Test
    public void testLongestMessage() {
        // Message 2 is the longest message overall
        String longest = Message.getLongestMessage();
        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            longest
        );
    }

    // =============================================
    // Search for Message ID
    // =============================================

    @Test
    public void testSearchByMessageID() {
        // Search using message 4's ID should return its message text
        String id = message4.getMessageID();
        String result = Message.searchByMessageID(id);
        assertEquals("It is dinner time!", result);
    }

    // =============================================
    // Search all messages for a particular recipient
    // =============================================

    @Test
    public void testSearchByRecipient() {
        // Searching +27838884567 should return both message 2 and message 5
        String result = Message.searchByRecipient("+27838884567");

        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    // =============================================
    // Delete a message using a message hash
    // =============================================

    @Test
    public void testDeleteByMessageHash() {
        // Delete message 2 using its hash
        String hash = message2.getMessageHash();
        String result = Message.deleteByMessageHash(hash);

        assertEquals(
            "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
            result
        );
    }

    // =============================================
    // Display Report
    // =============================================

    @Test
    public void testGenerateReport() {
        String report = Message.generateReport();

        // Report must contain hash, recipient and message for sent messages
        assertTrue(report.contains(message1.getMessageHash()));
        assertTrue(report.contains(message1.getRecipient()));
        assertTrue(report.contains(message1.getMessageText()));

        assertTrue(report.contains(message4.getMessageHash()));
        assertTrue(report.contains(message4.getRecipient()));
        assertTrue(report.contains(message4.getMessageText()));
    }

    // =============================================
    // Disregarded Messages array correctly populated
    // =============================================

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        // Disregarded array should contain message 3
        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals("Yohoooo, I am at your gate.", Message.getDisregardedMessages().get(0).getMessageText());
    }

    // =============================================
    // Stored Messages array correctly populated
    // =============================================

    @Test
    public void testStoredMessagesArrayPopulated() {
        // Stored array should contain message 2 and message 5
        assertEquals(2, Message.getStoredMessages().size());
    }

    // =============================================
    // Message Hashes and IDs arrays populated
    // =============================================

    @Test
    public void testMessageHashesAndIDsPopulated() {
        // 4 messages were sent or stored (message 1, 2, 4, 5) - message 3 was disregarded
        assertEquals(4, Message.getMessageHashes().size());
        assertEquals(4, Message.getMessageIDs().size());
    }

    // =============================================
    // Print sender and recipient of stored messages
    // =============================================

    @Test
    public void testPrintStoredSenderAndRecipient() {
        String result = Message.printStoredSenderAndRecipient();
        assertTrue(result.contains("+27838884567"));
    }
}

