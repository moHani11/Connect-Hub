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
  String normalizedQuery = query.trim().toLowerCase();
    // Search for users
    Search search = new Search(userAccountManagement);
    List<User> userResults = search.searchUsersByUsername(query);

     // Search for groups (case-insensitive comparison)
    List<Group> groupResults = GroupManager.getAllGroups().stream()
        .filter(group -> group.getName().toLowerCase().contains(normalizedQuery))
        .toList();
    // Filter out the logged-in user from the userResults
    userResults.removeIf(result -> result.getEmail().equals(user.getEmail()));

    // Filter out blocked users
 userResults.removeIf(this::isBlocked);

    // If no results, show a message that users or groups don't exist
    if (userResults.isEmpty() && groupResults.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No results found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Create new results frame
    JFrame resultsFrame = new JFrame("Search Results");
    resultsFrame.setSize(400, 300);
    resultsFrame.setLayout(new GridLayout(userResults.size() + groupResults.size(), 1));

    // Display users
    for (User result : userResults) {
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel(result.getUsername()));

        // Add action buttons for the user
        JButton viewProfileButton = new JButton("View Profile");
        JButton addFriendButton = new JButton("Add Friend");
        JButton blockUserButton = new JButton("Block");
        JButton removeFriendButton = new JButton("Remove Friend");

        // Check the relationship status
        if (user.getFriendRequests().contains(result.getEmail())) {
            // If a friend request is already sent to this user
            addFriendButton.setVisible(false);
            removeFriendButton.setVisible(false);
        } else if (result.getFriendRequests().contains(user.getEmail())) {
            // If the current user is in this user's pending requests
            addFriendButton.setText("Pending");
            addFriendButton.setEnabled(false);
            removeFriendButton.setVisible(false);
        } else if (user.getFriends().contains(result.getEmail())) {
            // If already friends
            addFriendButton.setVisible(false);
            removeFriendButton.setEnabled(true);
        } else {
            // Default case: show Add Friend
            removeFriendButton.setVisible(false);
        }

        // Add actions to buttons
        viewProfileButton.addActionListener(e -> viewProfile(result, resultsFrame));
        addFriendButton.addActionListener(e -> addFriend(result, addFriendButton));
        blockUserButton.addActionListener(e -> blockUser(result));
        removeFriendButton.addActionListener(e -> removeFriend(result));

        // Add buttons to the panel
        userPanel.add(viewProfileButton);
        userPanel.add(addFriendButton);
        userPanel.add(blockUserButton);
        userPanel.add(removeFriendButton);

        resultsFrame.add(userPanel);
    }

    // Display groups (similar logic to users)
    for (Group result : groupResults) {
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        groupPanel.add(new JLabel(result.getName()));

        JButton joinGroupButton = new JButton("Join Group");
        JButton leaveGroupButton = new JButton("Leave Group");
        JButton viewGroupButton = new JButton("View Group");

        if (result.getMemberIds().contains(user.getEmail())) {
            joinGroupButton.setEnabled(false);
            leaveGroupButton.setEnabled(true);
        } else {
            joinGroupButton.setEnabled(true);
            leaveGroupButton.setEnabled(false);
        }

        joinGroupButton.addActionListener(e -> joinGroup(result, joinGroupButton));
        leaveGroupButton.addActionListener(e -> leaveGroup(result, leaveGroupButton));
        viewGroupButton.addActionListener(e -> viewGroup(result));

        groupPanel.add(joinGroupButton);
        groupPanel.add(leaveGroupButton);
        groupPanel.add(viewGroupButton);

        resultsFrame.add(groupPanel);
        
    }
 
    resultsFrame.setLocationRelativeTo(this);
    resultsFrame.setVisible(true);
   
}


// Method to check if a user is blocked
private boolean isBlocked(User result) {
    // Check if the current user has blocked this user
    return user.getBlockedUsers().contains(result.getEmail()) || result.getBlockedUsers().contains(user.getEmail());
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
    ProfileGUI profileGUI = new ProfileGUI(this.user, profileManager, canEdit);  // Can edit if logged-in user
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
            //userAccountManagement.sendFriendRequest(user.getEmail(), otherUser.getEmail());  // Move to suggestions
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
    
    // Join Group Action
    private void joinGroup(Group group, JButton joinGroupButton) {
        if (!group.getMemberIds().contains(user.getEmail())) {
            group.getMemberIds().add(user.getEmail());
            JOptionPane.showMessageDialog(this, "You have joined the group: " + group.getName());

            // Disable the join button and enable leave button
            joinGroupButton.setEnabled(false);

            // Refresh the UI
            refreshUI();
        }
    }

    // Leave Group Action
     private void leaveGroup(Group group, JButton leaveGroupButton) {
        if (group.getMemberIds().contains(user.getEmail())) {
            group.getMemberIds().remove(user.getEmail());
            JOptionPane.showMessageDialog(this, "You have left the group: " + group.getName());

            // Disable the leave button and enable join button
            leaveGroupButton.setEnabled(false);

            // Refresh the UI
            refreshUI();
        }
    }

    // View Group Action
      private void viewGroup(Group group) {
        // Open a view for the group and its posts
        JOptionPane.showMessageDialog(this, "Viewing Group: " + group.getName());
        // You can create a group details page if necessary
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