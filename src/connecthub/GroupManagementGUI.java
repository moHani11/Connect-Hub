package connecthub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
        frame.setSize(1200, 800);
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

        JButton manageGroupButton = new JButton("Manage Groups");
        manageGroupButton.addActionListener(e -> manageGroupDialog());
        groupActionPanel.add(manageGroupButton);

        

        mainPanel.add(groupActionPanel, BorderLayout.SOUTH);
        refreshGroupList();

        // Adding Panels to Frame
        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void refreshGroupList() {
        groupListPanel.removeAll();
        ArrayList<Group> userGroups = groupManager.getUserGroups(currentUser.getUserId());
        for (Group group : userGroups) {
            JPanel groupPanel = new JPanel(new BorderLayout());
            JLabel groupLabel = new JLabel(group.getName());
            groupLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            groupPanel.add(groupLabel, BorderLayout.CENTER);

            JButton actionButton = new JButton("Manage");
            actionButton.addActionListener(e -> manageGroupActions(group));
            groupPanel.add(actionButton, BorderLayout.EAST);

            groupListPanel.add(groupPanel);
        }

        mainPanel.add(new JScrollPane(groupListPanel), BorderLayout.CENTER);
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
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
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
                groupManager.createGroup(name, desc, "", currentUser.getUserId());
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
        }

        JButton managePostsButton = new JButton("Manage Posts");
        managePostsButton.addActionListener(e -> JOptionPane.showMessageDialog(manageDialog, "Post Management for Group: " + group.getName()));
        actionPanel.add(managePostsButton);

        JButton leaveGroupButton = new JButton("Leave Group");
        leaveGroupButton.addActionListener(e -> {
            groupManager.removeMember(group.getGroupId(), currentUser.getUserId());
            JOptionPane.showMessageDialog(manageDialog, "Left Group");
            manageDialog.dispose();
            refreshGroupList();
        });
        actionPanel.add(leaveGroupButton);

        manageDialog.add(actionPanel, BorderLayout.CENTER);
        manageDialog.setVisible(true);
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

    public static void main(String[] args) {
        GroupManager groupManager = new GroupManager();
        User currentUser = new User("user123", "email@gmail.com", "mazen", "password", "2000-01-01");
        new GroupManagementGUI(groupManager, currentUser);
    }
}
