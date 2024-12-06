/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */ 
package connecthup;

/**
 *
 * @author mohamed
 */ 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserAccountManagement {
    Map<String, User> userDatabase = new HashMap<>(); // Mock file-based database

  

    
    public static class User {    //nested class 
        private String userId;
        private String email;
        private String username;
        private String password; // hashed password
        private String dateOfBirth;
        private boolean isOnline;

        public User() {
        }

        public User(String userId, String email, String username, String password, String dateOfBirth) {
            this.userId = userId;
            this.email = email;
            this.username = username;
            this.password = password;
            this.dateOfBirth = dateOfBirth;
            this.isOnline = false;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public String getPassword() {
            return password;
        }

        public boolean isOnline() {
            return isOnline;    // Getter for online status
        }

        public void setOnline(boolean online) {
            isOnline = online;    // Setter for online status
        }
    }
    

    // Method to sign up a new user
  public void signup(String userId, String email, String username, String password, String dateOfBirth) throws NoSuchAlgorithmException {
        // Check for duplicate email, username, or userId
        for (User user : userDatabase.values()) {
            if (user.email.equals(email)) {
                throw new IllegalArgumentException("Email is already taken.");
            }
            if (user.username.equals(username)) {
                throw new IllegalArgumentException("Username is already taken.");
            }
            if (user.userId.equals(userId)) {
                throw new IllegalArgumentException("User ID is already taken.");
            }
        }
if (userId.equals(username)) {
        throw new IllegalArgumentException("User ID cannot be the same as the Username.");
    }
if (userId.equals(email)) {
        throw new IllegalArgumentException("User ID cannot be the same as the Email.");
    }
        // Validate email format
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate date format
        if (!isValidDate(dateOfBirth)) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        String hashedPassword = hashPassword(password);  // Hash the password
        User user = new User(userId, email, username, hashedPassword, dateOfBirth);
        userDatabase.put(email, user);  // Store the user in the database
    }
    // Method to log in a user
public boolean login(String email, String password) throws NoSuchAlgorithmException {
        User user = userDatabase.get(email);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
            user.setOnline(true);
            return true;
        }
        return false;
    }



    // Method to log out a user
    public void logout(String email) {
        User user = userDatabase.get(email);
        if (user != null) {
            user.setOnline(false);
        }
    }

    // Validate email format
   private boolean validateEmail(String email) {
    // Regex to check the email format
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*" +  // Local part before "@"
                        "@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*" +             // Domain part before "."
                        "\\.[a-zA-Z]{2,}$";                                // TLD after "."
    
    // Check if the email matches the regex pattern
    return email.matches(emailRegex);
}
   public boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Ensures that the date must be valid (no 2024-13-40)
        try {
            Date parsedDate = sdf.parse(date);
            return true; // If parse is successful, it's a valid date
        } catch (ParseException e) {
            return false; // If parse fails, it's an invalid date
        }
    }
    // Hash the password
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.trim().getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
