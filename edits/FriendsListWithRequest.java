package connecthub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connecthub.ConnectHubEngine;
import connecthub.UserAccountManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FriendsListWithRequest extends JFrame {

    private static Map<String, User> users = new HashMap<>();
    private UserAccountManagement userAccountManagement;
    private String currentUserEmail; // The logged-in user's email
    
    private JTabbedPane tabbedPane;
    private JPanel friendListPanel;
    private JPanel friendSuggestionsPanel;
    private JPanel friendRequestsPanel;
     private JPanel blockedPanel;
    
    private Map<String, String> requestStatus = new HashMap<>(); // To track friend request status
    
    ConnectHubEngine cEngine;
    User user;
    
    public FriendsListWithRequest(UserAccountManagement userAccountManagement, String currentUserEmail, ConnectHubEngine c, User user) {
        this.userAccountManagement = userAccountManagement;
        this.currentUserEmail = currentUserEmail;
        this.user = user;
        this.cEngine = c;
        cEngine.loadData(userAccountManagement);
        this.users = userAccountManagement.userDatabase;

//        loadUserData();
        
        // Create the main frame
        setTitle("Friends List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);   
        setLayout(new BorderLayout()); // Use BorderLayout

        // Create a "Back" button
        JButton backButton = new JButton("Back to feed");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                User u = c.updateUser(userAccountManagement, user);
                NewsFeed n = new NewsFeed(u);
            }
        });
        add(backButton, BorderLayout.NORTH);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        friendListPanel = new JPanel();
        friendSuggestionsPanel = new JPanel();
        friendRequestsPanel = new JPanel();
        blockedPanel = new JPanel();  // Panel for Blocked users

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Friend List", friendListPanel);
        tabbedPane.addTab("Friend Suggestions", friendSuggestionsPanel);
        tabbedPane.addTab("Friend Requests", friendRequestsPanel);
        tabbedPane.addTab("Blocked", blockedPanel);  // Add Blocked tab

        // Add the tabbed pane to the frame
        add(tabbedPane);

        // Set layout for each panel
        friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));
        friendSuggestionsPanel.setLayout(new BoxLayout(friendSuggestionsPanel, BoxLayout.Y_AXIS));
        friendRequestsPanel.setLayout(new BoxLayout(friendRequestsPanel, BoxLayout.Y_AXIS));
        blockedPanel.setLayout(new BoxLayout(blockedPanel, BoxLayout.Y_AXIS));  // Blocked panel layout

        // Load the data into each tab
        loadFriendList();
        loadFriendSuggestions();
        loadFriendRequests();
        loadBlockedUsers();  // Load blocked users
        
        JFrame frame = this;
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Custom action on close
                int response = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to leave Connect Hub?",
                        "Confirm Close",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    
                    ConnectHubEngine c = new ConnectHubEngine();
                    UserAccountManagement userAccountManagement =  new UserAccountManagement(c);
                    
                    userAccountManagement.logout(user.getEmail());
                    
                    System.out.println("Logged out");
                    frame.dispose(); // Close the window
                }
            }
        });

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
    Set<String> blockedUsers = userAccountManagement.getBlockedUsers(currentUserEmail);
    friendRequestsPanel.removeAll();
 
    for (String email : requests) {
         if (blockedUsers.contains(email)) continue; 
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
            loadBlockedUsers();

            // Inform the user
            JOptionPane.showMessageDialog(this, "Friend request accepted!");
        });

        declineButton.addActionListener(e -> {
            // Decline the friend request
            userAccountManagement.declineFriendRequest(currentUserEmail, email);

            // Refresh the Friend Requests and Friend Suggestions
            loadFriendRequests();
            loadFriendSuggestions();
            loadBlockedUsers();

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
// Block the user and refresh the Blocked Users panel
private void loadFriendList() {
    Set<String> friends = userAccountManagement.getFriends(currentUserEmail); // Get friends of logged-in user
    Set<String> blockedUsers = userAccountManagement.getBlockedUsers(currentUserEmail); 
    friendListPanel.removeAll();

    for (String email : friends) {
         if (blockedUsers.contains(email)) continue; 
        User user = users.get(email);
        String username = user.getUsername();
        String status = user.isOnline() ? "Online" : "Offline";

        JPanel friendPanel = new JPanel();
        friendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLabel = new JLabel(username + " (" + status + ")");
        JButton removeButton = new JButton("Remove");
        JButton blockButton = new JButton("Block");

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
        
        blockButton.addActionListener(e -> {
            // Handle blocking friend logic
            userAccountManagement.blockUser(currentUserEmail, email);

            // Refresh the blocked users list immediately after blocking
            loadBlockedUsers();  // Immediately load and display the Blocked Users list
            loadFriendList();    // Optionally, refresh the Friend List

            // Show a confirmation dialog to notify the user
            JOptionPane.showMessageDialog(this, username + " has been successfully blocked.", 
                                          "User Blocked", JOptionPane.INFORMATION_MESSAGE);
        });

        friendPanel.add(nameLabel);
        friendPanel.add(removeButton);
        friendPanel.add(blockButton);
        friendListPanel.add(friendPanel);
    }

    // Revalidate and repaint the Friend List panel to immediately reflect changes
    friendListPanel.revalidate();
    friendListPanel.repaint();
}


private void loadFriendSuggestions() {
   Set<String> allUsers = users.keySet();
   Set<String> blockedUsers = userAccountManagement.getBlockedUsers(currentUserEmail);
    friendSuggestionsPanel.removeAll(); // Clear the panel before loading suggestions
    

    for (String email : allUsers) {
        // Skip the current user and existing friends
        if (email.equals(currentUserEmail) || userAccountManagement.getFriends(currentUserEmail).contains(email)) {
            continue;
        }

        // Skip users who have already sent a friend request to the current user
        if (userAccountManagement.getFriendRequests(currentUserEmail).contains(email)) {
            continue;
        }
         if (blockedUsers.contains(email) || userAccountManagement.getBlockedUsers(email).contains(currentUserEmail)) {
            continue;
        }
        
        

        User user = users.get(email);
        String username = user.getUsername();
        String status = user.isOnline() ? "Online" : "Offline";

        JPanel suggestionPanel = new JPanel();
        suggestionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLabel = new JLabel(username + " (" + status + ")");
        JButton addButton = new JButton("Add Friend");

        // If the current user has already sent a request, show "Pending"
        if ((requestStatus.containsKey(username) && requestStatus.get(username).equals("Pending")) ||
            userAccountManagement.getFriendRequests(email).contains(currentUserEmail)) {
            addButton.setText("Pending");
            addButton.setEnabled(false); // Disable the button once it's pending
        }

        addButton.addActionListener(e -> {
            try {
                // Send friend request logic
                userAccountManagement.sendFriendRequest(currentUserEmail, email);
                requestStatus.put(username, "Pending"); // Update the request status
                addButton.setText("Pending"); // Change button text to Pending
                addButton.setEnabled(false); // Disable the button after sending the request
                
                // Immediately update suggestion list and requests list
                loadFriendSuggestions(); // Reload suggestion list to reflect the change
                loadFriendRequests();    // Reload the friend requests tab to reflect the new request
            } catch (IOException ex) {
                Logger.getLogger(FriendsListWithRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        suggestionPanel.add(nameLabel);
        suggestionPanel.add(addButton);
        friendSuggestionsPanel.add(suggestionPanel);
    }

    // Revalidate and repaint the Friend Suggestions panel to immediately reflect changes
    friendSuggestionsPanel.revalidate();
    friendSuggestionsPanel.repaint();
}

// Blocked Users List
private void loadBlockedUsers() {
    Set<String> blockedUsers = userAccountManagement.getBlockedUsers(currentUserEmail);
    blockedPanel.removeAll();  // Clear the blocked users panel

    for (String email : blockedUsers) {
        User user = users.get(email);
        String username = user.getUsername();
        String status = user.isOnline() ? "Online" : "Offline";

        JPanel blockedPanelRow = new JPanel();
        blockedPanelRow.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel nameLabel = new JLabel(username + " (" + status + ")");
        JButton unblockButton = new JButton("Unblock");

        unblockButton.addActionListener(e -> {
            // Unblock the user
            userAccountManagement.unblockUser(currentUserEmail, email);

            // Refresh the blocked list and friend suggestions after unblocking
            loadBlockedUsers();   // Immediately reload the blocked list
            loadFriendSuggestions(); // Reload the friend suggestions list
            loadFriendList();  // Optionally reload friend list

            // Show a confirmation dialog
            JOptionPane.showMessageDialog(this, username + " has been successfully unblocked.",
                                          "User Unblocked", JOptionPane.INFORMATION_MESSAGE);
        });

        blockedPanelRow.add(nameLabel);
        blockedPanelRow.add(unblockButton);  // Add unblock button
        blockedPanel.add(blockedPanelRow);  // Add the panel row to the blocked panel
    }

    // Revalidate and repaint the blocked panel to show the updated list of blocked users
    blockedPanel.revalidate();
    blockedPanel.repaint();
}
}