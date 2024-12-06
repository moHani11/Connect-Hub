package connecthub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FriendsListWithRequest extends JFrame {

    private static Map<String, User> users = new HashMap<>();
    private UserAccountManagement userAccountManagement;
    private String currentUserEmail; // The logged-in user's email
    
    private JTabbedPane tabbedPane;
    private JPanel friendListPanel;
    private JPanel friendSuggestionsPanel;
    private JPanel friendRequestsPanel;
    
    private Map<String, String> requestStatus = new HashMap<>(); // To track friend request status

    // Constructor accepting the userAccountManagement object
    public FriendsListWithRequest(UserAccountManagement userAccountManagement, String currentUserEmail) {
        this.userAccountManagement = userAccountManagement;
        this.currentUserEmail = currentUserEmail;

        loadUserData();

        // Create the main frame
        setTitle("Friends List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        friendListPanel = new JPanel();
        friendSuggestionsPanel = new JPanel();
        friendRequestsPanel = new JPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Friend List", friendListPanel);
        tabbedPane.addTab("Friend Suggestions", friendSuggestionsPanel);
        tabbedPane.addTab("Friend Requests", friendRequestsPanel);

        // Add the tabbed pane to the frame
        add(tabbedPane);

        // Set layout for each panel
        friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));
        friendSuggestionsPanel.setLayout(new BoxLayout(friendSuggestionsPanel, BoxLayout.Y_AXIS));
        friendRequestsPanel.setLayout(new BoxLayout(friendRequestsPanel, BoxLayout.Y_AXIS));

        // Load the data into each tab
        loadFriendList();
        loadFriendSuggestions();
        loadFriendRequests();

        setLocationRelativeTo(null);
        setVisible(true);
    }


    // Load user data from JSON file
    private static void loadUserData() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("userdatabase.json")) {
            // Deserialize the JSON into a Map of email -> User
            users = gson.fromJson(reader, new TypeToken<Map<String, User>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
private void loadFriendRequests() {
    Set<String> requests = userAccountManagement.getFriendRequests(currentUserEmail);
    friendRequestsPanel.removeAll();

    for (String email : requests) {
        User user = users.get(email);
        String username = user.getUsername();
        String status = user.isOnline() ? "Online" : "Offline";

        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLabel = new JLabel(username + " (" + status + ")");
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");

        acceptButton.addActionListener(e -> {
            // Accept the friend request
            userAccountManagement.acceptFriendRequest(currentUserEmail, email);

            // Immediately update UI: Add to Friend List, Remove from Friend Requests
            loadFriendList();  // Refresh Friend List Tab
            loadFriendRequests();  // Refresh Friend Requests Tab
            loadFriendSuggestions();  // Refresh Friend Suggestions Tab

            // Inform the user
            JOptionPane.showMessageDialog(this, "Friend request accepted!");
        });

        declineButton.addActionListener(e -> {
            // Decline the friend request
            userAccountManagement.declineFriendRequest(currentUserEmail, email);

            // Refresh the Friend Requests and Friend Suggestions
            loadFriendRequests();
            loadFriendSuggestions();

            // Inform the user
            JOptionPane.showMessageDialog(this, "Friend request declined.");
        });

        requestPanel.add(nameLabel);
        requestPanel.add(acceptButton);
        requestPanel.add(declineButton);
        friendRequestsPanel.add(requestPanel);
    }

    // Revalidate and repaint the Friend Requests panel to immediately reflect changes
    friendRequestsPanel.revalidate();
    friendRequestsPanel.repaint();
}

private void loadFriendList() {
    Set<String> friends = userAccountManagement.getFriends(currentUserEmail); // Get friends of logged-in user
    friendListPanel.removeAll();

    for (String email : friends) {
        User user = users.get(email);
        String username = user.getUsername();
        String status = user.isOnline() ? "Online" : "Offline";

        JPanel friendPanel = new JPanel();
        friendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLabel = new JLabel(username + " (" + status + ")");
        JButton removeButton = new JButton("Remove");

        removeButton.addActionListener(e -> {
            // Handle removing friend logic
            userAccountManagement.removeFriend(currentUserEmail, email);

            // Refresh friend list and suggestion list
            loadFriendList();
            loadFriendSuggestions();

            // Show a confirmation dialog to notify the user
            JOptionPane.showMessageDialog(this, username + " has been successfully removed from your friend list.", 
                                          "Friend Removed", JOptionPane.INFORMATION_MESSAGE);
        });

        friendPanel.add(nameLabel);
        friendPanel.add(removeButton);
        friendListPanel.add(friendPanel);
    }

    // Revalidate and repaint the Friend List panel to immediately reflect changes
    friendListPanel.revalidate();
    friendListPanel.repaint();
}


private void loadFriendSuggestions() {
    Set<String> allUsers = users.keySet();
    friendSuggestionsPanel.removeAll();

    for (String email : allUsers) {
        if (!email.equals(currentUserEmail) && !userAccountManagement.getFriends(currentUserEmail).contains(email)) {
            User user = users.get(email);
            String username = user.getUsername();
            String status = user.isOnline() ? "Online" : "Offline";

            JPanel suggestionPanel = new JPanel();
            suggestionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            JLabel nameLabel = new JLabel(username + " (" + status + ")");
            JButton addButton = new JButton("Add Friend");

            // If the current user has already sent a request, show "Pending"
            if (requestStatus.containsKey(username) && requestStatus.get(username).equals("Pending")) {
                addButton.setText("Pending");
                addButton.setEnabled(false);  // Disable the button once it's pending
            }

            addButton.addActionListener(e -> {
                // Send friend request logic
                userAccountManagement.sendFriendRequest(currentUserEmail, email);
                requestStatus.put(username, "Pending");  // Update the request status
                addButton.setText("Pending");  // Change button text to Pending
                addButton.setEnabled(false);  // Disable the button after sending the request

                // Immediately update suggestion list and move to requests
                loadFriendSuggestions(); // Reload suggestion list to reflect the change
                loadFriendRequests();    // Reload the friend requests tab to reflect the new request
            });

            suggestionPanel.add(nameLabel);
            suggestionPanel.add(addButton);
            friendSuggestionsPanel.add(suggestionPanel);
        }
    }

    // Revalidate and repaint the Friend Suggestions panel to immediately reflect changes
    friendSuggestionsPanel.revalidate();
    friendSuggestionsPanel.repaint();
}
}