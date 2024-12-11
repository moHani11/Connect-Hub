package connecthub;

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
    // Ensure the receiver's friend requests map exists
    friendRequests.computeIfAbsent(receiverEmail, k -> new HashSet<>());
    
    // Add the sender's email to the receiver's friend requests
    friendRequests.get(receiverEmail).add(senderEmail);
    
    // Save the updated friend requests to file
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
        // Debug: Check the content of friendRequests
        System.out.println("Loaded friend requests: " + friendRequests);
    } catch (FileNotFoundException e) {
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
