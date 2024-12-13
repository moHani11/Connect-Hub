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

    public GroupManagementGUI(GroupManager groupManager, User currentUser) {
        this.groupManager = groupManager;
        this.currentUser = currentUser;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Group Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
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
        JDialog dialog = new JDialog(frame, "Join Group", true);
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
                groupManager.addMember(group.getGroupId(), currentUser.getUserId());
                JOptionPane.showMessageDialog(dialog, "Joined Group: " + group.getName());
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

            groupManager.removeMember(group.getGroupId(), currentUser.getUserId());
            JOptionPane.showMessageDialog(frame, "You left the group: " + group.getName());

            // Refresh group list after leaving
            refreshGroupList();

            // Remove the group from the 'My Groups' list
            groupListPanel.remove(groupPanel);
            groupListPanel.revalidate();
            groupListPanel.repaint();
                
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

    if (currentUser.getUserId().equals(group.getPrimaryAdminId())) {
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
    } else if (group.getAdminIds().contains(currentUser.getUserId())) {
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
        JDialog managePostsDialog = new JDialog(frame, "Manage Posts in Group: " + group.getName(), true);
        managePostsDialog.setSize(500, 400);
        managePostsDialog.setLayout(new BorderLayout());
        JLabel manageLabel = new JLabel("Manage Posts in Group: " + group.getName(), JLabel.CENTER);
        manageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        managePostsDialog.add(manageLabel, BorderLayout.NORTH);

        JPanel postPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        ArrayList<Post> postsInGroup = groupManager.getPostsInGroup(group.getGroupId());

        // Display posts of the group
        for (Post post : postsInGroup) {
            JPanel postItemPanel = new JPanel(new BorderLayout());
            JLabel postContentLabel = new JLabel(post.getContent());
            postContentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            postItemPanel.add(postContentLabel, BorderLayout.CENTER);

            JButton editPostButton = new JButton("Edit Post");
            editPostButton.addActionListener(e -> editPostInGroup(group, post));
            postItemPanel.add(editPostButton, BorderLayout.EAST);

            JButton deletePostButton = new JButton("Delete Post");
            deletePostButton.addActionListener(e -> deletePostFromGroup(group, post));
            postItemPanel.add(deletePostButton, BorderLayout.WEST);

            postPanel.add(postItemPanel);
        }

        JScrollPane scrollPane = new JScrollPane(postPanel);
        managePostsDialog.add(scrollPane, BorderLayout.CENTER);

        managePostsDialog.setVisible(true);
    }

    private void editPostInGroup(Group group, Post post) {
        JDialog editPostDialog = new JDialog(frame, "Edit Post in Group: " + group.getName(), true);
        editPostDialog.setSize(400, 300);

        JPanel editPanel = new JPanel(new BorderLayout());
        JTextArea postContentArea = new JTextArea(post.getContent());
        editPanel.add(new JScrollPane(postContentArea), BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            String newContent = postContentArea.getText().trim();
            if (!newContent.isEmpty()) {
                post.setContent(newContent);
                groupManager.updatePostInGroup(group.getGroupId(), post);
                JOptionPane.showMessageDialog(editPostDialog, "Post updated successfully!");
                editPostDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(editPostDialog, "Post content cannot be empty.");
            }
        });

        editPostDialog.add(editPanel, BorderLayout.CENTER);
        editPostDialog.add(saveButton, BorderLayout.SOUTH);
        editPostDialog.setVisible(true);
    }

    private void deletePostFromGroup(Group group, Post post) {
        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this post?", 
                                                         "Delete Post", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            groupManager.deletePostFromGroup(group.getGroupId(), post.getContentId());
            JOptionPane.showMessageDialog(frame, "Post deleted successfully.");
            refreshGroupList();
        }
    }

    private void promoteMember(Group group) {
        String memberId = JOptionPane.showInputDialog(frame, "Enter Member ID to Promote:");
        if (memberId != null && !memberId.trim().isEmpty()) {
            groupManager.promoteToAdmin(group.getGroupId(), memberId);
            JOptionPane.showMessageDialog(frame, "Member Promoted");
        }
    }

    private void demoteAdmin(Group group) {
        String adminId = JOptionPane.showInputDialog(frame, "Enter Admin ID to Demote:");
        if (adminId != null && !adminId.trim().isEmpty()) {
            groupManager.demoteAdmin(group.getGroupId(), adminId);
            JOptionPane.showMessageDialog(frame, "Admin Demoted");
        }
    }

    private void removeMember(Group group) {
        String memberId = JOptionPane.showInputDialog(frame, "Enter Member ID to Remove:");
        if (memberId != null && !memberId.trim().isEmpty()) {
            groupManager.removeMember(group.getGroupId(), memberId);
            JOptionPane.showMessageDialog(frame, "Member Removed");
        }
    }

    private void postInGroup(Group group) {
        JDialog postDialog = new JDialog(frame, "Post in Group: " + group.getName(), true);
        postDialog.setSize(400, 300);
        JPanel postPanel = new JPanel(new BorderLayout());

        JTextArea postContent = new JTextArea("Write your post here...");
        postPanel.add(new JScrollPane(postContent), BorderLayout.CENTER);

        JButton postButton = new JButton("Post");
        postButton.addActionListener(e -> {
            String content = postContent.getText().trim();
            if (!content.isEmpty()) {
                Post post = new Post(currentUser.getUserId(), content, null, new Date(), "");
                groupManager.addPostToGroup(group.getGroupId(), post);
                JOptionPane.showMessageDialog(postDialog, "Post added successfully!");
                postDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(postDialog, "Post content cannot be empty.");
            }
        });

        postDialog.add(postPanel, BorderLayout.CENTER);
        postDialog.add(postButton, BorderLayout.SOUTH);
        postDialog.setVisible(true);
    }
    
}