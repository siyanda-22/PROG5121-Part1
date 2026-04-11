/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import org.junit.jupiter.api.BeforeEach;
import com.mycompany.quickchatapp2.Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PROG5121 Part 1
 * Unit Tests for Login class
 * Uses exact test data from the assignment brief
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        // This runs before every single test
        // Creates a fresh Login object and registers a valid user
        login = new Login("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // =============================================
    // checkUserName - assertTrue / assertFalse
    // =============================================

    @Test
    public void testUsernameCorrectlyFormatted() {
        // kyl_1 has an underscore and is exactly 5 characters - should return true
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        // kyle!!!!!!! has no underscore and is too long - should return false
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    // =============================================
    // checkPasswordComplexity - assertTrue / assertFalse
    // =============================================

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        // Ch&&sec@ke99! has uppercase, number, special char, and is 8+ chars - true
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        // password has no uppercase, no number, no special character - false
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // =============================================
    // checkCellPhoneNumber - assertTrue / assertFalse
    // =============================================

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        // +27838968976 starts with international code - should return true
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        // 08966553 has no international code and is too short - should return false
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // =============================================
    // registerUser - assertEquals (message checks)
    // =============================================

    @Test
    public void testRegisterUserBadUsername() {
        Login newLogin = new Login("Test", "User");
        String result = newLogin.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(
            "Username is not correctly formatted; please ensure that your username "
            + "contains an underscore and is no more than five characters in length.",
            result
        );
    }

    @Test
    public void testRegisterUserBadPassword() {
        Login newLogin = new Login("Test", "User");
        String result = newLogin.registerUser("kyl_1", "password", "+27838968976");
        assertEquals(
            "Password is not correctly formatted; please ensure that the password "
            + "contains at least eight characters, a capital letter, a number, and a special character.",
            result
        );
    }

    @Test
    public void testRegisterUserBadCellPhone() {
        Login newLogin = new Login("Test", "User");
        String result = newLogin.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals(
            "Cell number is incorrectly formatted or does not contain an international code; "
            + "please correct the number and try again.",
            result
        );
    }

    // =============================================
    // loginUser - assertTrue / assertFalse
    // =============================================

    @Test
    public void testLoginSuccessful() {
        // Correct username and password - should return true
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        // Wrong password - should return false
        assertFalse(login.loginUser("kyl_1", "wrongpassword"));
    }

    // =============================================
    // returnLoginStatus - assertEquals (message checks)
    // =============================================

    @Test
    public void testReturnLoginStatusSuccess() {
        String result = login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", result);
    }

    @Test
    public void testReturnLoginStatusFailed() {
        String result = login.returnLoginStatus("kyl_1", "wrongpassword");
        assertEquals("Username or password incorrect, please try again.", result);
    }
}