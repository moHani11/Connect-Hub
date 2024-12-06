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
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class UserAccountManagement {
    private Map<String, User> userDatabase = new HashMap<>(); // Mock file-based database
    private static final String DATABASE_FILE = "userDatabase.json"; // Path to JSON file
    private final Gson gson = new Gson();

    public static class User {
        private String userId;
        public String email;
        public String username;
        private String password; // hashed password
        private String dateOfBirth;
        private boolean isOnline;
        private Set<String> friends = new HashSet<>();
        private Set<String> friendRequests = new HashSet<>();
        private Set<String> blockedUsers = new HashSet<>();

        public User(String userId, String email, String username, String password, String dateOfBirth) {
            this.userId = userId;
            this.email = email;
            this.username = username;
            this.password = password;
            this.dateOfBirth = dateOfBirth;
            this.isOnline = false;
        }

        public String getPassword() {
            return password;
        }

        public String getUserId() {
            return userId;
        }
        public String getEmail() {
            return email;
        }
        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }

        public Set<String> getFriends() {
            return friends;
        }

        public void setFriends(Set<String> friends) {
            this.friends = friends;
        }
        
        public String getUsername() {
            return username;
        }
public Set<String> getBlockedUsers() {
            return blockedUsers;
        }
        
    }

    public UserAccountManagement() {
        loadDatabase(); // Load the data when the object is created
    }
 

    public String getEmailByUsername(String username) {
    for (User user : userDatabase.values()) {
        if (user.getUsername().equals(username)) {
            return user.getEmail();
        }
    }
    return null; // Return null if no matching user is found
}
    // Get a user's username by their email
public String getUsernameByEmail(String email) {
    User user = userDatabase.get(email);
    if (user == null) {
        System.out.println("No user found for email: " + email);
    }
    return user != null ? user.username : null;
}
public void printUserDatabase() {
    for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
        System.out.println("Email: " + entry.getKey() + ", Username: " + entry.getValue().username);
    }
}

// Get all usernames for friend suggestions
public Map<String, String> getAllUsernames() {
    Map<String, String> usernames = new HashMap<>();
    for (User user : userDatabase.values()) {
        usernames.put(user.email, user.username);
    }
    return usernames;
}

    // Method to get all user emails
    public Set<String> getAllUserEmails() {
        return userDatabase.keySet(); // Returns a Set of all the emails (keys of the userDatabase map)
    }
    // Method to get the friends of a specific user
   public Set<String> getFriends(String email) {
        User user = userDatabase.get(email);
        return user != null ? user.getFriends() : new HashSet<>();
    }
    public Set<String> getBlockedUsers(String email) {
        User user = userDatabase.get(email);
        return user != null ? user.getBlockedUsers() : new HashSet<>();
    }


    public void addUser(User user) {
        userDatabase.put(user.getEmail(), user);
    }
    private String generateUniqueUserId() {
        String userId;
        do {
            // Generate a random user ID (e.g., 6-digit alphanumeric)
            userId = "UID" + (int)(Math.random() * 1_000_000);
        } while (isUserIdTaken(userId)); // Ensure the ID is not already taken
        return userId;
    }

    private boolean isUserIdTaken(String userId) {
        for (User user : userDatabase.values()) {
            if (user.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public void signup(String email, String username, String password, String dateOfBirth) throws NoSuchAlgorithmException {
        email = email.toLowerCase(); // Ensure email is stored in lowercase

        // Validate for duplicate email or username
        for (User user : userDatabase.values()) {
            if (user.email.equals(email)) {
                throw new IllegalArgumentException("Email is already taken.");
            }
            if (user.username.equals(username)) {
                throw new IllegalArgumentException("Username is already taken.");
            }
        }

        // Validate email format
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate date format
        if (!isValidDate(dateOfBirth)) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        // Generate a unique user ID
        String userId = generateUniqueUserId();
        System.out.println("Generated User ID: " + userId); // Debug: Print the generated user ID

        // Hash the password
        String hashedPassword = hashPassword(password);

        // Create a new user and add to database
        User user = new User(userId, email, username, hashedPassword, dateOfBirth);
        userDatabase.put(email, user); // Store the user in the database
        saveDatabase();
    }

    public boolean login(String email, String password) throws NoSuchAlgorithmException {
    User user = userDatabase.get(email);
    
    // Check if the user exists and the password is correct
    if (user != null && user.getPassword().equals(hashPassword(password))) {
        user.setOnline(true); // Mark the user as online
        
        // Print the username of the logged-in user
        System.out.println("User logged in successfully! Username: " + user.username);

        saveDatabase(); // Save the updated user data after login
        return true;
    }
    
    // If user is not found or password doesn't match, print an error
    System.out.println("Login failed for email: " + email);
    return false;
}


    public void logout(String email) {
        User user = userDatabase.get(email);
        if (user != null) {
            user.setOnline(false);
            saveDatabase(); // Save the updated user data after logout
        }
    }

    private void saveDatabase() {
        try (Writer writer = new FileWriter(DATABASE_FILE)) {
            gson.toJson(userDatabase, writer); // Write the user data to the JSON file
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save database to JSON file.");
        }
    }

    private void loadDatabase() {
        try (Reader reader = new FileReader(DATABASE_FILE)) {
            userDatabase = gson.fromJson(reader, new TypeToken<Map<String, User>>() {}.getType());
            if (userDatabase == null) userDatabase = new HashMap<>();
        } catch (FileNotFoundException e) {
            userDatabase = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*" +  // Local part before "@"
                            "@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*" +             // Domain part before "."
                            "\\.[a-zA-Z]{2,}$";                                // TLD after "."
        return email.matches(emailRegex);
    }

    public boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

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
