/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchatapp2;

/**
 *
 * @author Student
 */
public class Login {

    // Stored user details after registration
    private String username;
    private String password;
    private final String firstName;
    private final String lastName;

    // Constructor
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks if the username contains an underscore
     * and is no more than 5 characters long.
     * @param username
     * @return 
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks if the password meets complexity requirements:
     * - At least 8 characters
     * - Contains a capital letter
     * - Contains a number
     * - Contains a special character
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return hasUpperCase && hasDigit && hasSpecial;
    }

    /**
     * Checks if the cell phone number has an international
     * country code and is the correct length.
     *
     * Reference: Regex pattern adapted from
     * https://stackoverflow.com/questions/7416637
     * @param cellPhone
     * @return 
     */
    public boolean checkCellPhoneNumber(String cellPhone) {
        if (cellPhone == null) {
            return false;
        }
        return cellPhone.matches("^\\+[0-9]{1,3}[0-9]{7,10}$");
    }

    /**
     * Registers the user if all validations pass.
     * Returns the appropriate message for each outcome.
     * @param username
     * @param password
     * @param cellPhone
     * @return 
     */
    public String registerUser(String username, String password, String cellPhone) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell number is incorrectly formatted or does not contain an international code; "
                    + "please correct the number and try again.";
        }

        // Save details if everything passed
        this.username = username;
        this.password = password;

        return "Registration successful! Username successfully captured. "
                + "Password successfully captured. Cell number successfully captured.";
    }

    /**
     * Checks if the entered username and password
     * match what was saved during registration.
     * @param username
     * @param password
     * @return 
     */
    public boolean loginUser(String username, String password) {
        if (this.username == null || this.password == null) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Returns the login status message based on
     * whether the login was successful or not.
     * @param username
     * @param password
     * @return 
     */
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName;  }
    public String getUsername()  { return username;  }
}
