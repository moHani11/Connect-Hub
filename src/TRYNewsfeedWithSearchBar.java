package connecthub;

import static connecthub.User.fromJson;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class TRYNewsfeedWithSearchBar extends JFrame {
    private User user;
    private UserAccountManagement userAccountManagement;
    private String currentUserEmail;
    // To track pending friend requests
    private Map<String, String> requestStatus = new HashMap<>(); // Email -> "Pending"
    JFrame viewFrame = new JFrame("Group properties");
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
 // Ensure groups are loaded before searching
    GroupManager groupManager = new GroupManager();
    groupManager.loadGroupsFromFile(); // Load groups from file
    // Search for users
    Search search = new Search(userAccountManagement);
    List<User> userResults = search.searchUsersByUsername(query);
    
userResults.removeIf(result -> 
    user.getBlockedUsers().contains(result.getEmail()) || 
    result.getBlockedUsers().contains(user.getEmail())
);
    // Search for groups
    List<Group> groupResults = search.searchGroupsByName(query);
    // Filter out the logged-in user from the userResults
    userResults.removeIf(result -> result.getEmail().equals(user.getEmail()));
// If no results, show a message that users or groups don't exist
    if (userResults.isEmpty() && groupResults.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No results found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    // Create result panels for users and groups
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
        addFriendButton.addActionListener(e -> {
            try {
                addFriend(result, addFriendButton);
            } catch (IOException ex) {
                Logger.getLogger(TRYNewsfeedWithSearchBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        removeFriendButton.addActionListener(e -> {
    try {
        removeFriend(result, addFriendButton, removeFriendButton);
    } catch (IOException ex) {
        Logger.getLogger(TRYNewsfeedWithSearchBar.class.getName()).log(Level.SEVERE, null, ex);
    }
});
blockUserButton.addActionListener(e -> blockUser(result, addFriendButton, removeFriendButton));
        userPanel.add(viewProfileButton);
        userPanel.add(addFriendButton);
        userPanel.add(blockUserButton);
        userPanel.add(removeFriendButton);
        resultsFrame.add(userPanel);
    }
    // Display groups
    for (Group result : groupResults) {
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        groupPanel.add(new JLabel(result.getName()));
        JButton joinGroupButton = new JButton("Requset to Join Group");
        JButton leaveGroupButton = new JButton("Leave Group");
        JButton viewGroupButton = new JButton("View Group");
        // If the user is already a member, disable Join Group button and show Leave Group
        if (result.getMemberIds().contains(user.getUserId())  || result.getAdminIds().contains(user.getUserId())) {
            joinGroupButton.setEnabled(false);
            leaveGroupButton.setEnabled(true);
            viewGroupButton.setVisible(true);
        }
        else if (result.getRequestsIds().contains(user.getUserId())) {
            joinGroupButton.setEnabled(false);
            leaveGroupButton.setEnabled(false);
            viewGroupButton.setVisible(true);
        }else {
            joinGroupButton.setEnabled(true);
            leaveGroupButton.setEnabled(false);
             viewGroupButton.setVisible(true);
        }
          joinGroupButton.addActionListener(e -> {
                joinGroup(result, joinGroupButton, leaveGroupButton);
            });
            // Leave Group Button Action
            leaveGroupButton.addActionListener(e -> {
                leaveGroup(result, joinGroupButton, leaveGroupButton);
            });
      viewGroupButton.addActionListener(e -> {
    // Close TRYNewsfeedWithSearchBar frame
    GroupNewsFeed.createGroupPanel(result, user, this, viewFrame);
//    this.dispose();
    // Open GroupNewsFeed frame for the selected group
    
});
        groupPanel.add(joinGroupButton);
        groupPanel.add(leaveGroupButton);
        groupPanel.add(viewGroupButton);
        resultsFrame.add(groupPanel);
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
    ProfileGUI profileGUI = new ProfileGUI(this.user, profileManager, canEdit);  // Can edit if logged-in user
    profileGUI.setVisible(true); // Open the profile window for the selected user
}
    private void addFriend(User otherUser, JButton addFriendButton) throws IOException {
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
    private void removeFriend(User otherUser, JButton addFriendButton, JButton removeFriendButton) throws IOException {
    if (user.getFriends().contains(otherUser.getEmail())) {
        userAccountManagement.removeFriend(user.getEmail(), otherUser.getEmail());
        JOptionPane.showMessageDialog(this, otherUser.getUsername() + " has been removed from your friends.");
        // Update button states
        addFriendButton.setVisible(true);
        addFriendButton.setEnabled(true);
        addFriendButton.setText("Add Friend");
        removeFriendButton.setVisible(false);
    } else {
        JOptionPane.showMessageDialog(this, "You are not friends with " + otherUser.getUsername());
    }
}
    // Block User Action
    private void blockUser(User otherUser, JButton addFriendButton, JButton removeFriendButton) {
    userAccountManagement.blockUser(user.getEmail(), otherUser.getEmail());
    user.getBlockedUsers().add(otherUser.getEmail());
    JOptionPane.showMessageDialog(this, otherUser.getUsername() + " has been blocked.");
    // Update button states
    addFriendButton.setVisible(false);
    removeFriendButton.setVisible(false);
    // Remove the blocked user from the search results frame
    Component userPanel = (Component) addFriendButton.getParent();
    Container resultsContainer = userPanel.getParent();
    if (resultsContainer != null) {
        resultsContainer.remove(userPanel); // Remove the user's panel
        resultsContainer.revalidate();     // Revalidate the container to refresh layout
        resultsContainer.repaint();        // Repaint the container to update UI
    }
}
    
  // Join Group Action
private void joinGroup(Group group, JButton joinGroupButton, JButton leaveGroupButton) {
    GroupManager groupManager = new GroupManager();
    // Check if the user is not already a member or hasn't already requested to join
    if (!group.getMemberIds().contains(user.getUserId()) && 
        !group.getRequestsIds().contains(user.getUserId())) {
        // Add the current user to the group's pending requests
        groupManager.requestToJoinGroup(user.getUserId(), group.getGroupId());
        groupManager.saveGroupsToFile();
        // Reload the group to reflect changes
        groupManager.loadGroupsFromFile();
        Group updatedGroup = groupManager.getGroupById(group.getGroupId());
        // Update button states accordingly
        joinGroupButton.setEnabled(false);
        leaveGroupButton.setEnabled(false);
        JOptionPane.showMessageDialog(this, "You have requested to join the group: " + updatedGroup.getName());
    } else {
        JOptionPane.showMessageDialog(this, "You have already requested to join or are a member.");
    }
}
// Leave Group Action
private void leaveGroup(Group group, JButton joinGroupButton, JButton leaveGroupButton) {
    GroupManager groupManager = new GroupManager();
    if (group.getMemberIds().contains(user.getUserId()) || group.getAdminIds().contains(user.getUserId()) ) {
        // Remove the current user from the group
        groupManager.removeMember(group.getGroupId(), user.getUserId());
        groupManager.saveGroupsToFile();
        // Reload the group to confirm changes
        groupManager.loadGroupsFromFile();
        
        Group updatedGroup = groupManager.getGroupById(group.getGroupId());
if(updatedGroup == null)
{
    removeGroupPanel(leaveGroupButton);
}
        // If the group is empty, delete it
        if(updatedGroup != null){
        if (updatedGroup.getMemberIds().isEmpty()) {
            groupManager.deleteGroup(updatedGroup.getGroupId(), user.getUserId());
            removeGroupPanel(leaveGroupButton);
        } else {
            // Update button states
            joinGroupButton.setEnabled(true);
            leaveGroupButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "You have left the group: " + updatedGroup.getName());
        }
    }
    }
    else {
        JOptionPane.showMessageDialog(this, "You are not a member of this group.");
    }
}
private void removeGroupPanel(JButton leaveGroupButton) {
    // Get the group panel containing the button
    Component groupPanel = leaveGroupButton.getParent();
    // Access the parent container (the search results frame's container)
    Container resultsContainer = groupPanel.getParent();
    if (resultsContainer != null) {
        resultsContainer.remove(groupPanel); // Remove the group's panel
        resultsContainer.revalidate();       // Refresh the container's layout
        resultsContainer.repaint();          // Redraw the container
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
        // Implement this method to show blocked users
    }
}