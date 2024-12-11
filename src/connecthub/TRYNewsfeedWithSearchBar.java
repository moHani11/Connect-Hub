package connecthub;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TRYNewsfeedWithSearchBar extends JFrame {
    private User user;
    private UserAccountManagement userAccountManagement;

    // Constructor for News Feed
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

        // Top Panel for Search
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchBar = new JTextField();
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchBar, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // Search Action
        searchButton.addActionListener(e -> {
            String query = searchBar.getText().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Perform search functionality
    private void performSearch(String query) {
        Search search = new Search(userAccountManagement);
        List<User> results = search.searchUsersByUsername(query);

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
            JButton viewProfileButton = new JButton("View Profile");
            JButton addFriendButton = new JButton("Add Friend");
            JButton blockUserButton = new JButton("Block");
            JButton removeFriendButton = new JButton("Remove Friend");

            // Add actions to buttons
            viewProfileButton.addActionListener(e -> viewProfile(result));
            addFriendButton.addActionListener(e -> addFriend(result));
            blockUserButton.addActionListener(e -> blockUser(result));
            removeFriendButton.addActionListener(e -> removeFriend(result));

            userPanel.add(viewProfileButton);
            userPanel.add(addFriendButton);
            userPanel.add(blockUserButton);
            userPanel.add(removeFriendButton);

            resultsFrame.add(userPanel);
        }

        resultsFrame.setLocationRelativeTo(this);
        resultsFrame.setVisible(true);
    }

    // View Profile Action
    private void viewProfile(User user) {
        JOptionPane.showMessageDialog(this, "Viewing profile of " + user.getUsername());
    }

    // Add Friend Action
    private void addFriend(User otherUser) {
        // Check if the user is already friends
        if (user.getFriends().contains(otherUser.getEmail())) {
            JOptionPane.showMessageDialog(this, "You are already friends with " + otherUser.getUsername());
        } else {
            // Add friend request
            userAccountManagement.sendFriendRequest(user.getEmail(), otherUser.getEmail());
            JOptionPane.showMessageDialog(this, "Friend request sent to " + otherUser.getUsername());
        }

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
