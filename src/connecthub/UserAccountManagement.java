/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */ 
package connecthub;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserAccountManagement {
    public Map<String, User> userDatabase = new HashMap<>(); // Mock file-based database
    private ConnectHubEngine cEngine;
    private static final String DATABASE_FILE = "userDatabase.json"; // Path to JSON file
    private final Gson gson = new Gson();
    
    public UserAccountManagement(ConnectHubEngine c){
//        this.userDatabase = userDatabase;
//        loadDatabase(); // 7amo ----------------------------------------
        this.cEngine = c;
        c.loadData(this);
        c.populateUserDatabase(this);
    }
    public void sendFriendRequest(String senderEmail, String receiverEmail) {
    User sender = userDatabase.get(senderEmail);
    User receiver = userDatabase.get(receiverEmail);

    if (sender != null && receiver != null && !senderEmail.equals(receiverEmail)) {
        // Add sender to receiver's friend request list
        receiver.getFriendRequests().add(senderEmail);
//        saveDatabase();  // Save after updating the request
    }
}

   public void acceptFriendRequest(String currentUserEmail, String friendEmail) {
    User currentUser = userDatabase.get(currentUserEmail);
    User friendUser = userDatabase.get(friendEmail);

    // Add the friend to both users' friend list
    if (currentUser != null && friendUser != null) {
        currentUser.getFriends().add(friendEmail);
        friendUser.getFriends().add(currentUserEmail);

        // Remove the friend from the requests list
        currentUser.getFriendRequests().remove(friendEmail);
        friendUser.getFriendRequests().remove(currentUserEmail);

        // Save the updated data back to the file
//        saveDatabase();
    }
}

   public void declineFriendRequest(String currentUserEmail, String friendEmail) {
    User currentUser = userDatabase.get(currentUserEmail);
    User friendUser = userDatabase.get(friendEmail);

    // Remove the request from both users' friend requests
    if (currentUser != null && friendUser != null) {
        currentUser.getFriendRequests().remove(friendEmail);
        friendUser.getFriendRequests().remove(currentUserEmail);

        // Save the updated data back to the file
//        saveDatabase();
    }
}

     // Remove a friend from the friend list
    public void removeFriend(String email, String friendEmail) {
        User user = userDatabase.get(email);
        
        if (user != null) {
            user.removeFriend(friendEmail);
            User friend = userDatabase.get(friendEmail);
            if (friend != null) {
                friend.removeFriend(email);
            }
        }
    }
    // Method to block a user
    public void blockUser(String currentUserEmail, String targetUserEmail) {
        User currentUser = userDatabase.get(currentUserEmail);
        User targetUser = userDatabase.get(targetUserEmail);
        
        if (currentUser != null && targetUser != null) {
            // Add target user to the current user's blocked list
            currentUser.getBlockedUsers().add(targetUserEmail);
            // Remove target user from the current user's friends list
            currentUser.getFriends().remove(targetUserEmail);
//            saveDatabase(); // Save after modifying
        }
    }
       // Method to unblock a user
    public void unblockUser(String currentUserEmail, String targetUserEmail) {
        User currentUser = userDatabase.get(currentUserEmail);
        User targetUser = userDatabase.get(targetUserEmail);
        
        if (currentUser != null && targetUser != null) {
            // Remove target user from the blocked list
            currentUser.getBlockedUsers().remove(targetUserEmail);
//            saveDatabase(); // Save after modifying
        }
    }
    public Set<String> getFriendRequests(String email) {
        User user = userDatabase.get(email);
        return user != null ? user.getFriendRequests() : new HashSet<>();
    }
    

    public String getEmailByUsername(String username) {
    for (User user : userDatabase.values()) {
        if (user.getUsername().equals(username)) {
            return user.getEmail();
        }
    }
    return null; // Return null if no matching user is found
}
     // Get a user's list of friends
    public Set<String> getFriends(String email) {
        User user = userDatabase.get(email);
        return user != null ? user.getFriends() : new HashSet<>();
    }

    // Get a user's blocked users
    public Set<String> getBlockedUsers(String email) {
        User user = userDatabase.get(email);
        return user != null ? user.getBlockedUsers() : new HashSet<>();
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userDatabase.get(email);
    }
    // Get a user's username by their email
public String getUsernameByEmail(String email) {
    User user = userDatabase.get(email);
    if (user == null) {
        System.out.println("No user found for email: " + email);
    }
    return user != null ? user.getUsername() : null;
}
public void printUserDatabase() {
    for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
        System.out.println("Email: " + entry.getKey() + ", Username: " + entry.getValue().getUsername());
    }
}


// Get all usernames for friend suggestions
public Map<String, String> getAllUsernames() {
    Map<String, String> usernames = new HashMap<>();
    for (User user : userDatabase.values()) {
        usernames.put(user.getEmail(), user.getUsername());
    }
    return usernames;
}

    // Method to get all user emails
    public Set<String> getAllUserEmails() {
        return userDatabase.keySet(); // Returns a Set of all the emails (keys of the userDatabase map)
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

    // Method to sign up a new user
    public void signup(String email, String username, String password, String dateOfBirth) throws NoSuchAlgorithmException {
               email = email.toLowerCase(); // Ensure email is stored in lowercase
        // Check for duplicate email, username, or userId
        for (User user : userDatabase.values()) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("Email is already taken.");
            }
            if (user.getUsername().equals(username)) {
                throw new IllegalArgumentException("Username is already taken.");
            }
        }
//if (userId.equals(username)) {
//        throw new IllegalArgumentException("User ID cannot be the same as the Username.");
//    }
//if (userId.equals(email)) {
//        throw new IllegalArgumentException("User ID cannot be the same as the Email.");
//    }
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
        
        String hashedPassword = hashPassword(password);  // Hash the password
        User user = new User(userId, email, username, hashedPassword, dateOfBirth);
        userDatabase.put(email, user);  // Store the user in the database
//        saveDatabase(); //7amoo --------------------------------
    }
    // Method to log in a user
    public User login(String email, String password) throws NoSuchAlgorithmException {
        User user = userDatabase.get(email);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
            user.setOnline(true);
            System.out.println("User logged in successfully! Username: " + user.getUsername());

//        saveDatabase(); // Save the updated user data after login
            return user;
        }
        System.out.println("Login failed for email: " + email);
        return null;
    }



    // Method to log out a user
    public void logout(String email) {
        User user = userDatabase.get(email);
        if (user != null) {
            user.setOnline(false);
//            saveDatabase(); // Save the updated user data after logout
        }
    }
private void saveDatabase() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
        out.writeObject(userDatabase); // Serialize the userDatabase map
        System.out.println("Database saved successfully to " + DATABASE_FILE);
    } catch (IOException e) {
        System.err.println("Error saving database to file: " + e.getMessage());
        e.printStackTrace(); // Print stack trace for detailed debugging
        throw new RuntimeException("Failed to save database to file.", e);
    }
}

// Load database from a binary file
@SuppressWarnings("unchecked")
private void loadDatabase() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
        userDatabase = (Map<String, User>) in.readObject(); // Deserialize the map

        // Handle null database
        if (userDatabase == null) {
            userDatabase = new HashMap<>();
            System.out.println("Database was empty. Initialized with an empty map.");
        } else {
            System.out.println("Database loaded successfully from " + DATABASE_FILE);
        }
    } catch (FileNotFoundException e) {
        // Handle the case where the file doesn't exist
        userDatabase = new HashMap<>();
        System.out.println("Database file not found. Initialized with an empty map.");
    } catch (IOException | ClassNotFoundException e) {
        System.err.println("Error loading database from file: " + e.getMessage());
        e.printStackTrace(); // Print stack trace for detailed debugging
        throw new RuntimeException("Failed to load database from file.", e);
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
