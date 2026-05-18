
import com.mycompany.quickchatapp2.Message;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageTest - PROG5121 Part 2
 * Unit tests for the Message class.
 * Uses exact test data from the task brief.
 * @author Student
 */
public class MessageTest {

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        // This runs before every single test
        // Reset counters so tests do not affect each other
        Message.resetAll();

        // Test Data Message 1 - from the brief
        // Recipient: +27718693002 (valid - has international code)
        // Message: "Hi Mike, can you join us for dinner tonight?"
        // Action: Send
        message1 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");

        // Test Data Message 2 - from the brief
        // Recipient: 08575975889 (invalid - no international code)
        // Message: "Hi Keegan, did you receive the payment?"
        // Action: Discard
        message2 = new Message("08575975889", "Hi Keegan, did you receive the payment?");
    }

    // ---- checkMessageLength SUCCESS ----
    @Test
    public void testMessageLengthSuccess() {
        // Message under 250 characters should return "Message ready to send."
        String result = message1.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }

    // ---- checkMessageLength FAILURE ----
    @Test
    public void testMessageLengthFailure() {
        // 260 characters is 10 over the 250 limit
        String longMessage = new String(new char[260]).replace('\0', 'A');
        String result = message1.checkMessageLength(longMessage);
        assertEquals(
            "Message exceeds 250 characters by 10: please reduce the size.",
            result
        );
    }

    // ---- checkRecipient SUCCESS ----
    @Test
    public void testRecipientCorrectlyFormatted() {
        // +27718693002 has international code so it should pass
        String result = message1.checkRecipient("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    // ---- checkRecipient FAILURE ----
    @Test
    public void testRecipientIncorrectlyFormatted() {
        // 08575975889 has no international code so it should fail
        String result = message2.checkRecipient("08575975889");
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. "
            + "Please correct the number and try again.",
            result
        );
    }

    // ---- createMessageHash correct format ----
    @Test
    public void testMessageHashCorrect() {
        // Message 1: "Hi Mike, can you join us for dinner tonight?"
        // First word = HI, Last word = TONIGHT
        // Hash must end with HITONIGHT and be all uppercase
        String hash = message1.getMessageHash();
        assertNotNull(hash);
        assertTrue(hash.equals(hash.toUpperCase()));
        assertTrue(hash.contains(":"));
        assertTrue(hash.endsWith("HITONIGHT"));
    }

    // ---- createMessageHash loop test ----
    @Test
    public void testMessageHashesInLoop() {
        // Test hashes for multiple messages using a loop - required by brief
        Message.resetAll();

        String[] recipients = {
            "+27718693002",
            "+27838968976"
        };
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };
        String[] expectedEndings = {
            "HITONIGHT",
            "HIPAYMENT"
        };

        for (int i = 0; i < messages.length; i++) {
            Message msg = new Message(recipients[i], messages[i]);
            String hash = msg.getMessageHash();
            assertNotNull(hash);
            assertTrue(hash.equals(hash.toUpperCase()));
            assertTrue(hash.endsWith(expectedEndings[i]));
        }
    }

    // ---- checkMessageID created ----
    @Test
    public void testMessageIDCreated() {
        // Message ID must exist and not be empty
        String id = message1.getMessageID();
        assertNotNull(id);
        assertFalse(id.isEmpty());
        System.out.println("Message ID generated: " + id);
    }

    // ---- checkMessageID not more than 10 characters ----
    @Test
    public void testMessageIDNotMoreThan10Characters() {
        // Message ID must be 10 characters or less
        String id = message1.getMessageID();
        assertTrue(message1.checkMessageID(id));
    }

    // ---- sentMessage SEND ----
    @Test
    public void testSendMessage() {
        // User selects Send - should return "Message successfully sent."
        String result = message1.sentMessage(1);
        assertEquals("Message successfully sent.", result);
    }

    // ---- sentMessage DISREGARD ----
    @Test
    public void testDisregardMessage() {
        // User selects Disregard - should return delete prompt
        String result = message1.sentMessage(2);
        assertEquals("Press 0 to delete the message.", result);
    }

    // ---- sentMessage STORE ----
    @Test
    public void testStoreMessage() {
        // User selects Store - should return stored message
        String result = message1.sentMessage(3);
        assertEquals("Message successfully stored.", result);
    }

    // ---- returnTotalMessages ----
    @Test
    public void testReturnTotalMessages() {
        // Message 1 is sent - counts toward total
        message1.sentMessage(1);
        // Message 2 is discarded - does NOT count toward total
        message2.sentMessage(2);
        // Only 1 message was actually sent
        assertEquals(1, message1.returnTotalMessages());
    }
}
