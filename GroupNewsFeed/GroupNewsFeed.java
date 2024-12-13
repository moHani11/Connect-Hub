/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package connecthub;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.border.Border;

public class GroupNewsFeed extends javax.swing.JFrame {

    GroupManager groupManager;
    User currentUser;
    Group currentGroup;    
    
    public GroupNewsFeed(User currentUser, String GroupId) {
        
        this.currentUser = currentUser;
        this.groupManager = new GroupManager();
        this.currentGroup = groupManager.getGroupById(GroupId);
        initComponents();
        
        this.setLocation(400, 120);
                
        setGroupName();
        setGroupPhoto();
        populateFeed();
        
        this.setVisible(true);

    }

    public void setGroupName(){
        String GroupName = this.currentGroup.getName();
        jLabel1.setText(GroupName);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setFont(new Font("Arial", Font.BOLD, 26)); // Font(name, style, size)


        jLabel1.setOpaque(true); 
        jLabel1.setBackground(Color.CYAN);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2); // Black border with 3px thickness
        jLabel1.setBorder(border);
        
        jLabel2.setOpaque(true); 
        jLabel2.setBackground(Color.CYAN);
        jLabel2.setBorder(border);

    }

    public void setGroupPhoto(){
        
        String imagePath = this.currentGroup.getGroupPhoto();
        if (imagePath.isBlank()){
        imagePath  = "cover.jpg" ;
        }
        
        coverImageFrame1.setBorderSize(10);
        coverImageFrame1.setBorderColor(Color.black);
        coverImageFrame1.setImage(imagePath);
    }
    
    public void populateFeed(){
      
        jScrollPane1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));  // Arrange panels vertically
        jScrollPane1.setViewportView(panelContainer);
        
        ArrayList<Post> groupPosts = groupManager.getPostsInGroup(currentGroup.getGroupId());
        ArrayList<JPanel> postsPanels = new ArrayList<>();

        for (Post post : groupPosts) {
            JPanel panel = SocialMediaApp.createPostCard(post);  // Create card for each post
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            postsPanels.add(panel);
        }

    // Loop through and add panels
        for (int i = 0; i < postsPanels.size(); i++) {
            JPanel temp = new JPanel();
            temp.setBackground(Color.lightGray);  // Random background color
            temp.setPreferredSize(new Dimension(500, 20));  // Set a fixed height for each panel
            temp.setBorder(BorderFactory.createBevelBorder(0));
        // Add the panel to the container
            panelContainer.add(postsPanels.get(i));
            panelContainer.add(temp);
    }

    // Revalidate and repaint to ensure the scroll pane updates
    panelContainer.revalidate();
    panelContainer.repaint();
        
    }
    
    public JFrame createGroupPanel(String groupTitle, String description, ArrayList<String> members) {
        // Create the main panel that will contain all components
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
        
        // Panel for group title and manage group button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(groupTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set big font for title
        JButton manageButton = new JButton("Manage Group");
        
        // Add title label and button to the header panel
        headerPanel.add(titleLabel);
        headerPanel.add(manageButton);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to the header panel
        
        // Add header panel to the main panel
        mainPanel.add(headerPanel);
        
        // Description panel
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        JLabel descriptionLabel = new JLabel("<html>" + description + "</html>"); // HTML to allow line breaks
        descriptionPanel.add(descriptionLabel);
        descriptionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to the description panel
        
        // Add description panel to the main panel
        mainPanel.add(descriptionPanel);
        
        // Create a container for the list of members (using JScrollPane for scrolling)
        JPanel membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        
        // Add a panel for each member with a button
        for (String member : members) {
            JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel memberLabel = new JLabel(member);
            JButton memberButton = new JButton("View");
            
            memberButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(mainPanel, "View member: ");
                }
            });

            // Add member label and button to the member panel
            memberPanel.add(memberLabel);
            memberPanel.add(memberButton);
            membersPanel.add(memberPanel);
        }
        
        // Add the members list to a JScrollPane
        JScrollPane membersScrollPane = new JScrollPane(membersPanel);
        membersScrollPane.setPreferredSize(new Dimension(400, 200)); // Set a fixed size for the scrollable area
        
        // Add JScrollPane to the main panel
        mainPanel.add(membersScrollPane);
        
        // Handle button actions (for example purposes)
        manageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Manage group functionality");
            }
        });
                
        JFrame frame = new JFrame("Group Properties");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        
        // Add the group panel to the frame
        frame.add(mainPanel);
        
        return frame;
    }
    
    private void postInGroup(Group group) {
        JDialog postDialog = new JDialog(this, "Post in Group: " + group.getName(), true);
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
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        coverImageFrame1 = new connecthub.CoverImageFrame();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 0, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 650));

        coverImageFrame1.setBackground(new java.awt.Color(255, 204, 204));
        coverImageFrame1.setBorderColor(new java.awt.Color(255, 255, 102));
        coverImageFrame1.setOpaque(true);

        jButton1.setBackground(new java.awt.Color(10, 57, 129));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Edit Cover");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        coverImageFrame1.add(jButton1);
        jButton1.setBounds(990, 140, 90, 23);

        jButton4.setBackground(new java.awt.Color(42, 51, 53));
        jButton4.setForeground(new java.awt.Color(255, 51, 51));
        jButton4.setText("Update Password");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        coverImageFrame1.add(jButton4);
        jButton4.setBounds(850, 140, 130, 23);

        jLabel1.setText("Group title");
        jLabel1.setToolTipText("");
        jLabel1.setOpaque(true);
        coverImageFrame1.add(jLabel1);
        jLabel1.setBounds(320, 0, 132, 33);

        jButton6.setBackground(new java.awt.Color(204, 204, 204));
        jButton6.setText("Back to Newsfeed");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        coverImageFrame1.add(jButton6);
        jButton6.setBounds(0, 0, 130, 20);

        jLabel2.setBackground(new java.awt.Color(153, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 21)); // NOI18N
        jLabel2.setText("Group Feed");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel2.setOpaque(true);

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jButton5.setText("Post in Group");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(204, 204, 204));
        jButton7.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jButton7.setText("View Group");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(coverImageFrame1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coverImageFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        postInGroup(currentGroup);
        populateFeed();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.setVisible(false);
        NewsFeed feed = new NewsFeed(currentUser);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String title = currentGroup.getName();
        String description = currentGroup.getDescription();
        ArrayList<String> members = new ArrayList<>();
        members.add("John Doe");
        members.add("Jane Smith");
        members.add("Alice Johnson");
        members.add("Bob Brown");
        
        JFrame viewGP = createGroupPanel(title, description, members);
//        this.setVisible(false);
        viewGP.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new GroupNewsFeed().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private connecthub.CoverImageFrame coverImageFrame1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
