package connecthub;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
public class GroupManagementGUI {
    private JFrame frame;
    private JPanel mainPanel, groupListPanel, groupActionPanel;
    private GroupManager groupManager;
    private User currentUser;
    private UserAccountManagement userManager;
    private JDialog managePostsDialog;
public GroupManagementGUI(GroupManager groupManager,UserAccountManagement userManager ,User currentUser) {
        this.groupManager = groupManager;
        this.currentUser = currentUser;
        this.userManager = userManager;
        initialize();
    }
    private void initialize() {
        groupManager.loadGroupsFromFile();
        frame = new JFrame("Group Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocation(400, 120);
        frame.setLayout(new BorderLayout());
        
        // Panels
        mainPanel = new JPanel(new BorderLayout());
        groupListPanel = new JPanel();
        groupActionPanel = new JPanel();
        groupListPanel.setLayout(new BoxLayout(groupListPanel, BoxLayout.Y_AXIS));
        groupActionPanel.setLayout(new GridLayout(0, 2, 10, 10));
        // Adding Components
        JLabel title = new JLabel("My Groups", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        
        JButton backToFeed = new JButton("Back To Feed");
       backToFeed.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   frame.setVisible(false);
                   NewsFeed n = new NewsFeed(currentUser);
           }
           });
        backToFeed.setVisible(true);
        mainPanel.add(backToFeed, BorderLayout.WEST); // Button at the bottom
        mainPanel.add(title, BorderLayout.NORTH);
        
        JButton createGroupButton = new JButton("Create New Group");
        createGroupButton.addActionListener(e -> createGroupDialog());
        groupActionPanel.add(createGroupButton);
        // Change "Manage Groups" to "Join Group"
        JButton joinGroupButton = new JButton("Join Group");
        joinGroupButton.addActionListener(e -> joinGroupDialog());
        groupActionPanel.add(joinGroupButton);
        
          JButton manageGroupButton = new JButton("Manage Groups");
        manageGroupButton.addActionListener(e -> manageGroupDialog());
//        groupActionPanel.add(manageGroupButton);
        mainPanel.add(groupActionPanel, BorderLayout.SOUTH);
        refreshGroupList();
        // Adding Panels to Frame
        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }
    private void joinGroupDialog() {
        JDialog dialog = new JDialog(frame, "Request to join Group", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        JLabel joinLabel = new JLabel("Select a Group to Join", JLabel.CENTER);
        joinLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dialog.add(joinLabel, BorderLayout.NORTH);
        JPanel groupPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        // Fetch groups that the current user can join (not created by them and not a member)
        ArrayList<Group> joinableGroups = getJoinableGroups();
        for (Group group : joinableGroups) {
            JButton groupButton = new JButton(group.getName());
            groupButton.addActionListener(e -> {
                
                if (group.getRequestsIds().contains(currentUser.getUserId())){
                    JOptionPane.showMessageDialog(dialog, "Already Requested before to join Group: " + group.getName());
                }
                else {
                groupManager.requestToJoinGroup(currentUser.getUserId(), group.getGroupId() );
                groupManager.saveGroupsToFile(); // Save changes to file
                JOptionPane.showMessageDialog(dialog, "Requested to join Group: " + group.getName());
                }
                dialog.dispose();
                refreshGroupList();  // Update the group list after joining
            });
            groupPanel.add(groupButton);
        }
        dialog.add(groupPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    private ArrayList<Group> getJoinableGroups() {
        ArrayList<Group> joinableGroups = new ArrayList<>();
        ArrayList<Group> allGroups = groupManager.getAllGroups(); // This function should return all groups
        for (Group group : allGroups) {
            if (!group.getAdminIds().contains(currentUser.getUserId()) && 
                !group.getMemberIds().contains(currentUser.getUserId())) {
                joinableGroups.add(group);
            }
        }
        return joinableGroups;
    }
    private void refreshGroupList() {
    groupListPanel.removeAll(); // Clear previous group list
    ArrayList<Group> userGroups = groupManager.getUserGroups(currentUser.getUserId());
            for (Group group : userGroups) {
            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
            JLabel groupImageLabel = new JLabel();
            ImageIcon groupImageIcon = new ImageIcon(group.getGroupPhoto());
            Image image = groupImageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            groupImageLabel.setIcon(new ImageIcon(image));
            groupPanel.add(groupImageLabel, BorderLayout.WEST);
            JLabel groupLabel = new JLabel("Group Name: " + group.getName());
            groupLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            groupLabel.setBorder(BorderFactory.createEtchedBorder());
            groupLabel.setBackground(Color.LIGHT_GRAY);
            groupPanel.add(groupLabel);
            
            JLabel groupDescLabel = new JLabel("Group description: " + group.getDescription());
            groupDescLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            groupDescLabel.setBackground(Color.DARK_GRAY);
            groupPanel.add(groupDescLabel);
            // Action Button
                    
            JButton leaveButton = new JButton("Leave Group");
leaveButton.addActionListener(e -> {
    String currentUserId = currentUser.getUserId();
    String primaryAdminId = group.getPrimaryAdminId();
    ArrayList<String> memberIds = group.getMemberIds();
    ArrayList<String> adminIds = group.getAdminIds();
    if (!currentUserId.equals(primaryAdminId)) {
        groupManager.removeMember(group.getGroupId(), currentUserId);
        JOptionPane.showMessageDialog(frame, "You left the group: " + group.getName());
         refreshGroupList();
    } 
    else {
        boolean hasOtherAdmins = adminIds != null && adminIds.size() > 1;
        boolean hasMembers = memberIds != null && !memberIds.isEmpty();
       
        if (!hasOtherAdmins && !hasMembers) {
            int choice = JOptionPane.showConfirmDialog(
                frame,
                "If you leave the group '" + group.getName() + "', the group will be deleted. Are you sure?",
                "Delete Group",
                JOptionPane.OK_CANCEL_OPTION
            );
            if (choice == JOptionPane.OK_OPTION) {
                groupManager.deleteGroup(group.getGroupId(),currentUserId);
                JOptionPane.showMessageDialog(frame, "Group '" + group.getName() + "' has been deleted.");
                refreshGroupList();
                
            }
        } 
       
        if (!hasOtherAdmins && hasMembers) {
    String newPrimaryAdminId = memberIds.get(0); // Get the second member (first in the list)
    group.setPrimaryAdminId(newPrimaryAdminId);
    group.removeMember(newPrimaryAdminId);
    
    if (!adminIds.contains(newPrimaryAdminId)) {
        adminIds.add(newPrimaryAdminId); // Ensure the new primary admin is added to admin list
    }
    groupManager.updateGroup(group); // Save the updated group info
    groupManager.removeMember(group.getGroupId(), currentUserId);
    String newPrimaryAdminUsername = userManager.getUsernameByID(newPrimaryAdminId);
    JOptionPane.showMessageDialog(frame, 
        "You left the group. The first member has been promoted to Primary Admin: " + newPrimaryAdminUsername);
    refreshGroupList();
}
     
        else if (hasOtherAdmins) {
           
            String newPrimaryAdmin = null;
            for (String adminId : adminIds) {
                if (!adminId.equals(currentUserId)) {
                    newPrimaryAdmin = adminId;
                    break;
                }
            }
            if (newPrimaryAdmin != null) {
                group.setPrimaryAdminId(newPrimaryAdmin);
                groupManager.updateGroup(group);
                groupManager.removeMember(group.getGroupId(), currentUserId);
                 String newPrimaryAdmin1 = userManager.getUsernameByID(newPrimaryAdmin);
                JOptionPane.showMessageDialog(frame, 
                    "You left the group. Another admin has been promoted to Primary Admin: " + newPrimaryAdmin1);
                 refreshGroupList();
            }
        }
    }
});       
            
            JButton openButton = new JButton("Open Group");
            openButton.addActionListener(e -> {
                frame.setVisible(false);
                GroupNewsFeed gpFeed = new GroupNewsFeed(currentUser, group.getGroupId());
                refreshGroupList(); // Refresh group list after leaving
            });
            
            JButton ManageButton = new JButton("Manage Group");
            ManageButton.addActionListener(e -> {
                manageGroupActions(group);
                refreshGroupList(); // Refresh group list after leaving
            });
            
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            
            buttonsPanel.add(leaveButton);
            buttonsPanel.add(openButton);
            buttonsPanel.add(ManageButton);
            
            groupPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,4));
            groupPanel.add(buttonsPanel);
            
            groupListPanel.add(groupPanel);
        }
    // Remove old JScrollPane if exists
    Component[] components = mainPanel.getComponents();
    for (Component component : components) {
        if (component instanceof JScrollPane) {
            mainPanel.remove(component);
        }
    }
    // Add updated group list to mainPanel
    mainPanel.add(new JScrollPane(groupListPanel), BorderLayout.CENTER);
    groupListPanel.revalidate();
    groupListPanel.repaint();
    frame.revalidate();
    frame.repaint();
}
private void createGroupDialog() {
    JDialog dialog = new JDialog(frame, "Create Group", true);
    dialog.setSize(500, 400);
    dialog.setLayout(new BorderLayout());
    JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    JLabel nameLabel = new JLabel("Group Name:");
    JTextField nameField = new JTextField();
    JLabel descLabel = new JLabel("Description:");
    JTextField descField = new JTextField();
    JLabel photoLabel = new JLabel("Group Photo:");
    JLabel previewLabel = new JLabel();
    previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    previewLabel.setPreferredSize(new Dimension(150, 100));
    previewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    JButton uploadButton = new JButton("Upload");
    final String[] photoPath = {""}; // Use a single-element array to store the photo path
    uploadButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(dialog);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            photoPath[0] = selectedFile.getAbsolutePath(); // Save the file path in the array
            ImageIcon imageIcon = new ImageIcon(photoPath[0]);
            Image image = imageIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(image));
        }
    });
    fieldsPanel.add(nameLabel);
    fieldsPanel.add(nameField);
    fieldsPanel.add(descLabel);
    fieldsPanel.add(descField);
    fieldsPanel.add(photoLabel);
    fieldsPanel.add(uploadButton);
    JPanel previewPanel = new JPanel(new BorderLayout());
    previewPanel.add(new JLabel("Photo Preview:", JLabel.CENTER), BorderLayout.NORTH);
    previewPanel.add(previewLabel, BorderLayout.CENTER);
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {
        String name = nameField.getText();
        String desc = descField.getText();
        if (!name.isEmpty()) {
            groupManager.createGroup(name, desc, photoPath[0], currentUser.getUserId()); // Pass the photo path
            JOptionPane.showMessageDialog(dialog, "Group Created: " + name);
            dialog.dispose();
            refreshGroupList();
        } else {
            JOptionPane.showMessageDialog(dialog, "Please enter a group name.");
        }
    });
    dialog.add(fieldsPanel, BorderLayout.CENTER);
    dialog.add(previewPanel, BorderLayout.EAST);
    dialog.add(saveButton, BorderLayout.SOUTH);
    dialog.setVisible(true);
}
    private void manageGroupDialog() {
        JDialog dialog = new JDialog(frame, "Manage Groups", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        JLabel manageLabel = new JLabel("Select a Group to Manage", JLabel.CENTER);
        manageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dialog.add(manageLabel, BorderLayout.NORTH);
        JPanel groupPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        for (Group group : groupManager.getUserGroups(currentUser.getUserId())) {
            JButton groupButton = new JButton(group.getName());
            groupButton.addActionListener(e -> manageGroupActions(group));
            groupPanel.add(groupButton);
        }
        dialog.add(groupPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
  private void manageGroupActions(Group group) {
    JDialog manageDialog = new JDialog(frame, "Manage Group: " + group.getName(), true);
    manageDialog.setSize(500, 400);
    manageDialog.setLayout(new BorderLayout());
    JLabel manageLabel = new JLabel("Manage Group: " + group.getName(), JLabel.CENTER);
    manageLabel.setFont(new Font("Arial", Font.BOLD, 18));
    manageDialog.add(manageLabel, BorderLayout.NORTH);
    JPanel actionPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    
    if (currentUser.getUserId().equals(group.getPrimaryAdminId()))
    {
        // Primary Admin actions
        JButton promoteButton = new JButton("Promote Member");
        promoteButton.addActionListener(e -> promoteMember(group));
        actionPanel.add(promoteButton);
        
        JButton demoteButton = new JButton("Demote Admin");
        demoteButton.addActionListener(e -> demoteAdmin(group));
        actionPanel.add(demoteButton);
        
        JButton deleteButton = new JButton("Delete Group");
        deleteButton.addActionListener(e -> {
            groupManager.deleteGroup(group.getGroupId(), currentUser.getUserId());
            JOptionPane.showMessageDialog(manageDialog, "Group Deleted");
            manageDialog.dispose();
            refreshGroupList();
        });
        actionPanel.add(deleteButton);
    } if (group.getAdminIds().contains(currentUser.getUserId())) {
    JButton viewRequestsButton = new JButton("View Requests");
    viewRequestsButton.addActionListener(e -> manageRequests(group));
    actionPanel.add(viewRequestsButton);
    // Other Admin actions
    JButton managePostsButton = new JButton("Manage Posts");
    managePostsButton.addActionListener(e -> managePosts(group));
    actionPanel.add(managePostsButton);
    JButton removeMemberButton = new JButton("Remove Member");
    removeMemberButton.addActionListener(e -> removeMember(group));
    actionPanel.add(removeMemberButton);
    } else if (group.getMemberIds().contains(currentUser.getUserId())) {
        // Normal Member actions
        JButton leaveGroupButton = new JButton("Leave Group");
        leaveGroupButton.addActionListener(e -> {
            groupManager.removeMember(group.getGroupId(), currentUser.getUserId());
            JOptionPane.showMessageDialog(manageDialog, "Left the group");
            manageDialog.dispose();
            refreshGroupList();
        });
        actionPanel.add(leaveGroupButton);
        
        JButton postButton = new JButton("Post in Group");
        postButton.addActionListener(e -> postInGroup(group));
        actionPanel.add(postButton);
    } else {
        // Non-member actions
        JButton joinGroupButton = new JButton("Join Group");
        joinGroupButton.addActionListener(e -> {
            groupManager.addMember(group.getGroupId(), currentUser.getUserId());
            
            JOptionPane.showMessageDialog(manageDialog, "Joined Group");
            manageDialog.dispose();
            refreshGroupList();
        });
        actionPanel.add(joinGroupButton);
        
        JButton postButton = new JButton("Post in Group");
        postButton.addActionListener(e -> postInGroup(group));
        actionPanel.add(postButton);
    }
    
    manageDialog.add(actionPanel, BorderLayout.CENTER);
    manageDialog.setVisible(true);
}
  private void managePosts(Group group) {
  
    if (managePostsDialog != null && managePostsDialog.isVisible()) {
        managePostsDialog.dispose();
    }
 
    managePostsDialog = new JDialog(frame, "Manage Posts in Group: " + group.getName(), true);
    managePostsDialog.setSize(500, 400);
    managePostsDialog.setLayout(new BorderLayout());
    JLabel manageLabel = new JLabel("Manage Posts in Group: " + group.getName(), JLabel.CENTER);
    manageLabel.setFont(new Font("Arial", Font.BOLD, 18));
    managePostsDialog.add(manageLabel, BorderLayout.NORTH);
  
    JPanel postPanel = new JPanel(new GridLayout(0, 1, 10, 10));
    ArrayList<Post> postsInGroup = groupManager.getPostsInGroup(group.getGroupId());
    for (Post post : postsInGroup) {
        postPanel.add(createPostCardWithActions(group, post));
    }
    JScrollPane scrollPane = new JScrollPane(postPanel);
    managePostsDialog.add(scrollPane, BorderLayout.CENTER);
    managePostsDialog.setVisible(true);
}
   
  
private JPanel createPostCardWithActions(Group group, Post post) {
    JPanel postPanel = new JPanel(new BorderLayout());
    postPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    JLabel postContentLabel = new JLabel(post.getContent());
    postContentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    postPanel.add(postContentLabel, BorderLayout.CENTER);
    if (post.getImagePath() != null) {
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(new ImageIcon(post.getImagePath())
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        postPanel.add(imageLabel, BorderLayout.WEST);
    }
    JButton editButton = new JButton("Edit");
    editButton.addActionListener(e -> editPostInGroup(group, post));
    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(e -> deletePostFromGroup(group, post));
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actionsPanel.add(editButton);
    actionsPanel.add(deleteButton);
    postPanel.add(actionsPanel, BorderLayout.SOUTH);
    return postPanel;
}
private void editPostInGroup(Group group, Post post) {
    groupManager.loadGroupsFromFile();
    JDialog editDialog = new JDialog(frame, "Edit Post", true);
    editDialog.setSize(400, 400);
    editDialog.setLayout(new BorderLayout());
 
    JTextArea postContentArea = new JTextArea(post.getContent());
    postContentArea.setLineWrap(true);
    postContentArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(postContentArea);
    editDialog.add(scrollPane, BorderLayout.CENTER);
  
    JPanel imagePanel = new JPanel();
    JLabel imageLabel = new JLabel();
    if (post.getImagePath() != null) {
        imageLabel.setIcon(new ImageIcon(new ImageIcon(post.getImagePath())
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
    }
    imagePanel.add(imageLabel);
    JButton removeImageButton = new JButton("Remove Image");
    removeImageButton.addActionListener(e -> {
        post.setImagePath(null);
        imageLabel.setIcon(null);
    });
    imagePanel.add(removeImageButton);
    editDialog.add(imagePanel, BorderLayout.NORTH);
    JButton saveButton = new JButton("Save Changes");
    saveButton.addActionListener(e -> {
        String newContent = postContentArea.getText().trim();
        if (!newContent.isEmpty() || post.getImagePath() != null) {
            post.setContent(newContent);
            groupManager.updatePostInGroup(group.getGroupId(), post);
            JOptionPane.showMessageDialog(editDialog, "Post updated successfully!");
            editDialog.dispose();
            
           managePosts(group); 
        } else {
            JOptionPane.showMessageDialog(editDialog, "Post content cannot be empty.");
        }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(saveButton);
    editDialog.add(buttonPanel, BorderLayout.SOUTH);
   editDialog.setVisible(true);
}
   private void deletePostFromGroup(Group group, Post post) {
       groupManager.loadGroupsFromFile();
    int confirmation = JOptionPane.showConfirmDialog(frame, 
        "Are you sure you want to delete this post?", "Delete Post", JOptionPane.YES_NO_OPTION);
    if (confirmation == JOptionPane.YES_OPTION) {
        groupManager.deletePostFromGroup(group.getGroupId(), post.getContentId());
        JOptionPane.showMessageDialog(frame, "Post deleted successfully.");
        managePosts(group); 
    }
}
  private void promoteMember(Group group) {
    JDialog dialog = new JDialog(frame, "Promote Member", true);
    dialog.setSize(400, 300);
    dialog.setLayout(new BorderLayout());
    JLabel label = new JLabel("Select a member to promote:", JLabel.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 16));
    dialog.add(label, BorderLayout.NORTH);
    JPanel membersPanel = new JPanel(new GridLayout(0, 1));
    for (String memberId : group.getMemberIds()) {
        if(!group.getAdminIds().contains(memberId)) 
        {
        User member = userManager.getUserById(memberId); // استخدام getUserById
        if (member != null) {
            JButton memberButton = new JButton(member.getUsername()); 
            memberButton.addActionListener(e -> {
                group.promoteToAdmin(memberId); 
                JOptionPane.showMessageDialog(dialog, "Promoted " + member.getUsername() + " to admin.");
                groupManager.updateGroup(group);
                       refreshGroupList();
                dialog.dispose();
            });
            membersPanel.add(memberButton);
        }
    }
    }
    dialog.add(new JScrollPane(membersPanel), BorderLayout.CENTER);
    dialog.setVisible(true);
}
 
private void demoteAdmin(Group group) {
    JDialog dialog = new JDialog(frame, "Demote Admin", true);
    dialog.setSize(400, 300);
    dialog.setLayout(new BorderLayout());
    JLabel label = new JLabel("Select an admin to demote:", JLabel.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 16));
    dialog.add(label, BorderLayout.NORTH);
    JPanel adminPanel = new JPanel(new GridLayout(0, 1));
    for (String adminId : group.getAdminIds()) {
        if (!adminId.equals(group.getPrimaryAdminId())) { // استثناء الـ Primary Admin
            User admin = userManager.getUserById(adminId); // استخدام getUserById
            if (admin != null) {
                JButton adminButton = new JButton(admin.getUsername());
                adminButton.addActionListener(e -> {
                    group.demoteFromAdmin(adminId); 
                    JOptionPane.showMessageDialog(dialog, "Demoted " + admin.getUsername() + " from admin.");
                    dialog.dispose();
                    groupManager.updateGroup(group);
                       refreshGroupList();
                });
                adminPanel.add(adminButton);
            }
        }
    }
    dialog.add(new JScrollPane(adminPanel), BorderLayout.CENTER);
    dialog.setVisible(true);
}
private void manageRequests(Group group) {
    JDialog dialog = new JDialog(frame, "Manage Requets", true);
    dialog.setSize(400, 300);
    dialog.setLayout(new BorderLayout());
    JLabel label = new JLabel("Select a user to manage their request to join the group:", JLabel.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 16));
    dialog.add(label, BorderLayout.NORTH);
    JPanel requestsPanel = new JPanel(new GridLayout(0, 1));
    for (String userID : group.getRequestsIds()) {
            User requestedUser = userManager.getUserById(userID); 
            if (requestedUser != null) {
                JButton userButton = new JButton(requestedUser.getUsername());
                userButton.addActionListener(e -> {
                    showRequestDialog(this.frame, userID, group.getGroupId(), group.getPrimaryAdminId());
                    groupManager.updateGroup(group);
                    refreshGroupList();
                    dialog.dispose();
                });
                requestsPanel.add(userButton);
        }
    }
    dialog.add(new JScrollPane(requestsPanel), BorderLayout.CENTER);
    dialog.setVisible(true);
}
    private  void showRequestDialog(JFrame parentFrame, String userID, String groupID, String adminID) {
        // Create a dialog
        JDialog dialog = new JDialog(parentFrame, "Manage Request", true); // Modal dialog
        dialog.setSize(300, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(parentFrame); // Center relative to parent frame
        // Message label
        JLabel messageLabel = new JLabel("Manage the request?", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dialog.add(messageLabel, BorderLayout.CENTER);
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        // Accept button
        JButton acceptButton = new JButton("Accept Request");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupManager.acceptRequestToJoin(userID, groupID, adminID);
                JOptionPane.showMessageDialog(dialog, "Request Accepted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose(); // Close the dialog
            }
        });
        // Refuse button
        JButton refuseButton = new JButton("Refuse Request");
        refuseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupManager.refuseRequestToJoin(userID, groupID, adminID);
                JOptionPane.showMessageDialog(dialog, "Request Refused!", "Info", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose(); 
            }
        });
                // Add buttons to the panel
        buttonPanel.add(acceptButton);
        buttonPanel.add(refuseButton);
        // Add button panel to the dialog
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        // Show the dialog
        dialog.setVisible(true);
        }
        
   
        
  private void removeMember(Group group) {
    JDialog dialog = new JDialog(frame, "Remove Member", true);
    dialog.setSize(400, 300);
    dialog.setLayout(new BorderLayout());
    JLabel label = new JLabel("Select a Member to Delete:", JLabel.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 16));
    dialog.add(label, BorderLayout.NORTH);
    
    JPanel memberPanel = new JPanel(new GridLayout(0, 1));
    for (String memberId : group.getMemberIds()) {
        if (!memberId.equals(group.getPrimaryAdminId()) && 
            (group.getAdminIds() == null || !group.getAdminIds().contains(memberId))) {
            User member = userManager.getUserById(memberId); // استخدام getUserById
            if (member != null) {
                JButton memberButton = new JButton(member.getUsername());
                memberButton.addActionListener(e -> {
                    // إزالة العضو
                    groupManager.removeMember(group.getGroupId(), memberId);
                    group.getMemberIds().remove(memberId); // إزالة العضو من قائمة الأعضاء
                    groupManager.saveGroupsToFile(); // حفظ التحديثات
                    JOptionPane.showMessageDialog(dialog, 
                        "Deleted " + member.getUsername() + " from Group.");
                    
                    dialog.dispose(); // إغلاق الحوار
                    refreshGroupList(); // تحديث قائمة الجروب
                    updateMemberPanel(memberPanel, group); // تحديث واجهة الأعضاء
                });
                memberPanel.add(memberButton);
            }
        }
    }
    dialog.add(new JScrollPane(memberPanel), BorderLayout.CENTER);
    dialog.setVisible(true);
}
private void updateMemberPanel(JPanel memberPanel, Group group) {
   
    memberPanel.removeAll();
    for (String memberId : group.getMemberIds()) {
        if (!memberId.equals(group.getPrimaryAdminId()) && 
            (group.getAdminIds() == null || !group.getAdminIds().contains(memberId))) {
            User member = userManager.getUserById(memberId);
            if (member != null) {
                JButton memberButton = new JButton(member.getUsername());
                memberPanel.add(memberButton);
            }
        }
    }
    memberPanel.revalidate();
    memberPanel.repaint();
}
   private void postInGroup(Group group) {
        
    JDialog postDialog = new JDialog(frame, "Post in Group: " + group.getName(), true);
    postDialog.setSize(400, 300);
    JPanel postPanel = new JPanel(new BorderLayout());
    JTextArea postContent = new JTextArea();
    postPanel.add(new JScrollPane(postContent), BorderLayout.CENTER);
    JButton addImageButton = new JButton("Add Image");
    String[] imagePath = {null};  // Store image path
    addImageButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
        int returnValue = fileChooser.showOpenDialog(postDialog);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            imagePath[0] = fileChooser.getSelectedFile().getAbsolutePath();  // Get the image path
        }
    });
    JButton postButton = new JButton("Post");
    postButton.addActionListener(e -> {
        String content = postContent.getText().trim();
        if (!content.isEmpty() || imagePath[0] != null) {
            Post post = new Post(currentUser.getUserId(), content, imagePath[0], new Date(), "");
            groupManager.addPostToGroup(group.getGroupId(), post);
            JOptionPane.showMessageDialog(postDialog, "Post added successfully!");
            postDialog.dispose();
        } else {
            JOptionPane.showMessageDialog(postDialog, "Post content cannot be empty.");
        }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addImageButton);
    buttonPanel.add(postButton);
    postDialog.add(postPanel, BorderLayout.CENTER);
    postDialog.add(buttonPanel, BorderLayout.SOUTH);
    postDialog.setVisible(true);
}
    
    public static void main(String[] args) {
        GroupManager groupManager = new GroupManager();
       // User currentUser = new User("user1234", "emaill@gmail.com", "mazen", "password", "2000-01-01");
//        new GroupManagementG  UI(groupManager, currentUser);
    }
}