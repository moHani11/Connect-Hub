package connecthub;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TRYNewsfeedWithSearchBar extends JFrame {
    private User user;
    private UserAccountManagement userAccountManagement;
    private String currentUserEmail;
    // To track pending friend requests
    private Map<String, String> requestStatus = new HashMap<>(); // Email -> "Pending"

    public TRYNewsfeedWithSearchBar(User user, UserAccountManagement userAccountManagement) {
        this.user = user;
        this.userAccountManagement = userAccountManagement;
        initUI();
    }

    // Initialize the UI components
    private void initUI() {
        setTitle("News Feed");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchBar = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Search Action
        searchButton.addActionListener(e -> {
            String query = searchBar.getText().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            }
        });

        // Back Action
        backButton.addActionListener(e -> {
            // Close this frame and return to the News Feed
            this.setVisible(false);
            new NewsFeed(user).setVisible(true);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void performSearch(String query) {
        if (userAccountManagement == null) {
            JOptionPane.showMessageDialog(this, "User account management is not initialized.");
            return;
        }

        Search search = new Search(userAccountManagement);
        List<User> results = search.searchUsersByUsername(query);

        // Filter out the logged-in user from the results
        results.removeIf(result -> result.getEmail().equals(user.getEmail()));

        // If no results, show a message that user doesn't exist
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User not found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Display results in a new JFrame
        JFrame resultsFrame = new JFrame("Search Results");
        resultsFrame.setSize(400, 300);
        resultsFrame.setLayout(new GridLayout(results.size(), 1));

        for (User result : results) {
            JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            userPanel.add(new JLabel(result.getUsername()));

            // Add buttons only if the result is not the logged-in user
            if (!result.getEmail().equals(user.getEmail())) {
                JButton viewProfileButton = new JButton("View Profile");
                JButton addFriendButton = new JButton("Add Friend");
                JButton blockUserButton = new JButton("Block");
                JButton removeFriendButton = new JButton("Remove Friend");

                // Check if the current user has already sent a friend request
                if ((requestStatus.containsKey(result.getEmail()) && requestStatus.get(result.getEmail()).equals("Pending"))||
              userAccountManagement.getFriendRequests(result.getEmail()).contains(currentUserEmail)) {
                    addFriendButton.setText("Pending");
                    addFriendButton.setEnabled(false); // Disable the button once it's pending
                }

                // Add actions to buttons
                viewProfileButton.addActionListener(e -> viewProfile(result, resultsFrame));
                addFriendButton.addActionListener(e -> addFriend(result, addFriendButton));  // Modified to pass the button
                blockUserButton.addActionListener(e -> blockUser(result));
                removeFriendButton.addActionListener(e -> removeFriend(result));

                userPanel.add(viewProfileButton);
                userPanel.add(addFriendButton);
                userPanel.add(blockUserButton);
                userPanel.add(removeFriendButton);
            }

            resultsFrame.add(userPanel);
        }

        resultsFrame.setLocationRelativeTo(this);
        resultsFrame.setVisible(true);
    }

// View Profile Action
private void viewProfile(User selectedUser, JFrame resultsFrame) {
    if (selectedUser == null) {
        JOptionPane.showMessageDialog(this, "Invalid user profile.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Close the results frame before opening the profile
    if (resultsFrame != null) {
        resultsFrame.dispose();  // Close the search results window
    }

    // Close the current NewsFeed window (this frame)
    this.dispose(); // Close the current NewsFeed window

    // Check if it's the logged-in user (currUser) or another user
    boolean canEdit = user.getEmail().equals(selectedUser.getEmail()); // Can edit if it's the logged-in user

    // Open the ProfileGUI for the selected user
    ProfileManager profileManager = new ProfileManager(selectedUser);
    ProfileGUI profileGUI = new ProfileGUI(selectedUser, profileManager, canEdit);  // Can edit if logged-in user
    profileGUI.setVisible(true); // Open the profile window for the selected user
}

    private void addFriend(User otherUser, JButton addFriendButton) {
        // Check if the user is already friends
        if (user.getFriends().contains(otherUser.getEmail())) {
            JOptionPane.showMessageDialog(this, "You are already friends with " + otherUser.getUsername());
        } else {
            // Add friend request
            userAccountManagement.sendFriendRequest(user.getEmail(), otherUser.getEmail());

            // Mark the request as "Pending"
            requestStatus.put(otherUser.getEmail(), "Pending");

            // Update the button text and disable it to show the request is "Pending"
            addFriendButton.setText("Pending");
            addFriendButton.setEnabled(false);

            JOptionPane.showMessageDialog(this, "Friend request sent to " + otherUser.getUsername());
        }

        // Refresh the UI to reflect changes
        refreshUI();
    }

    // Remove Friend Action
    private void removeFriend(User otherUser) {
        // Check if the user is friends
        if (user.getFriends().contains(otherUser.getEmail())) {
            userAccountManagement.removeFriend(user.getEmail(), otherUser.getEmail());
            userAccountManagement.sendFriendRequest(user.getEmail(), otherUser.getEmail());  // Move to suggestions
            JOptionPane.showMessageDialog(this, otherUser.getUsername() + " has been removed from your friends.");
        } else {
            JOptionPane.showMessageDialog(this, "You are not friends with " + otherUser.getUsername());
        }

        refreshUI();
    }

    // Block User Action
    private void blockUser(User otherUser) {
        // Block the user
        userAccountManagement.blockUser(user.getEmail(), otherUser.getEmail());
        JOptionPane.showMessageDialog(this, otherUser.getUsername() + " has been blocked.");

        // Optionally, you can also remove the user from the friend list after blocking
        userAccountManagement.removeFriend(user.getEmail(), otherUser.getEmail());

        // Refresh the UI (reload friend list and blocked users)
        refreshUI();
    }

    // Refresh the Friend Suggestions, Friend List, and Blocked Users in TRYNewsfeedWithSearchBar
    private void refreshUI() {
        loadFriendSuggestions();
        loadFriendList();
        loadBlockedUsers();
    }

    private void loadFriendSuggestions() {
        // Update the friend suggestions based on the latest data
        // Implement this method to show updated suggestions
    }

    private void loadFriendList() {
        // Update the friend list based on the latest data
        // Implement this method to show updated friends
    }

    private void loadBlockedUsers() {
        // Update the blocked users based on the latest data
        // Implement this method to show blocked users
    }
}
