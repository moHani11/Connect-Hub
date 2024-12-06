package connecthup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mohamed
 */

public class FriendManagement {
    private Map<String, Set<String>> friendRequests = new HashMap<>();
    private UserAccountManagement userAccountManagement;
      private static final String FRIEND_REQUESTS_FILE = "friendRequests.json";
    private Gson gson = new Gson();

    public FriendManagement() {
        loadFriendRequests();
    }
    public FriendManagement(UserAccountManagement userAccountManagement) {
        this.userAccountManagement = userAccountManagement;
    }

    public void sendFriendRequest(String senderEmail, String receiverEmail) {
        if (senderEmail.equals(receiverEmail)) {
            throw new IllegalArgumentException("You cannot send a friend request to yourself.");
        }
        if (userAccountManagement.getBlockedUsers(receiverEmail).contains(senderEmail)) {
            throw new IllegalArgumentException("You are blocked by this user.");
        }
          friendRequests.computeIfAbsent(receiverEmail, k -> new HashSet<>()).add(senderEmail);
        saveFriendRequests();
    }

   public void acceptFriendRequest(String senderEmail, String receiverEmail) {
        Set<String> requests = friendRequests.get(receiverEmail);
        if (requests != null) {
            requests.remove(senderEmail);
            saveFriendRequests(); // Update the data
        }
    }

    public void declineFriendRequest(String senderEmail, String receiverEmail) {
        Set<String> requests = friendRequests.get(receiverEmail);
        if (requests != null) {
            requests.remove(senderEmail);
            saveFriendRequests(); // Update the data
        }
    }
     private void saveFriendRequests() {
        try (Writer writer = new FileWriter(FRIEND_REQUESTS_FILE)) {
            gson.toJson(friendRequests, writer); // Serialize to JSON and save
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFriendRequests() {
        try (Reader reader = new FileReader(FRIEND_REQUESTS_FILE)) {
            friendRequests = gson.fromJson(reader, new TypeToken<Map<String, Set<String>>>(){}.getType());
            if (friendRequests == null) {
                friendRequests = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            // If file is not found, no data, so we initialize an empty map
            friendRequests = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeFriend(String userEmail, String friendEmail) {
        userAccountManagement.getFriends(userEmail).remove(friendEmail);
        userAccountManagement.getFriends(friendEmail).remove(userEmail);
    }

    public void blockUser(String userEmail, String blockEmail) {
        userAccountManagement.getBlockedUsers(userEmail).add(blockEmail);
        removeFriend(userEmail, blockEmail);
    }

    public Set<String> getFriendRequests(String email) {
        return friendRequests.getOrDefault(email, new HashSet<>());
    }
}
