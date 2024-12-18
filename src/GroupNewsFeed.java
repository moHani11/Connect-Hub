
    package connecthub;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
public class GroupNewsFeed extends javax.swing.JFrame {
    GroupManager groupManager;
    User currentUser;
    Group currentGroup;   
    JFrame viewFrame = new JFrame("Group properties");
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
        
     
        addPostComponents();  
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
            JPanel panel = createPostCardWithActions(post);  // Create card for each post with actions
            panel.setPreferredSize(new Dimension(500, 250));  // Set a fixed height for each panel
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            postsPanels.add(panel);
        }
        // Loop through and add panels
        for (int i = 0; i < postsPanels.size(); i++) {
            JPanel temp = new JPanel();
            temp.setBackground(Color.lightGray);  // Random background color
            temp.setPreferredSize(new Dimension(500, 50));  // Set a fixed height for each panel
            temp.setBorder(BorderFactory.createBevelBorder(0));
            // Add the panel to the container
            panelContainer.add(postsPanels.get(i));
            panelContainer.add(temp);
        }
        // Revalidate and repaint to ensure the scroll pane updates
        panelContainer.revalidate();
        panelContainer.repaint();
    }
    private JPanel createPostCardWithActions(Post post) {
        JPanel postPanel = SocialMediaApp.createPostCard(post);
        
        
        if (! this.currentUser.getUserId().equals( post.getUserId())){
            return postPanel;
        }
        
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editPost(post));   
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deletePost(post));
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        postPanel.add(actionsPanel, BorderLayout.SOUTH);
        return postPanel;
    }
private String selectedImagePath = null;
private void selectImageForPost() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select an Image");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnValue = fileChooser.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile.exists()) {
            selectedImagePath = selectedFile.getAbsolutePath();
        } else {
            JOptionPane.showMessageDialog(this, "The selected file does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
private void editPost(Post post) {
    JDialog editDialog = new JDialog(this, "Edit Post", true);
    editDialog.setSize(400, 400);
    // Panel to hold all components
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
    // Text area for post content
    JTextArea postContent = new JTextArea(post.getContent(), 5, 20);
    postContent.setWrapStyleWord(true);
    postContent.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(postContent);
    editPanel.add(new JLabel("Edit the content:"));
    editPanel.add(scrollPane);
    // Image label and buttons
    JLabel imageLabel = new JLabel();
    if (post.getImagePath() != null) {
        // Display the image if available
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        imageLabel.setIcon(imageIcon);
        editPanel.add(imageLabel);
        // Change image button
        JButton changeImageButton = new JButton("Change Image");
        changeImageButton.addActionListener(e -> {
            selectImageForPost();
            if (selectedImagePath != null) {
                post.setImagePath(selectedImagePath);
                imageLabel.setIcon(new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                post.setLastModifiedDate(new Date());
            }
        });
        editPanel.add(changeImageButton);
        // Remove image button
        JButton deleteImageButton = new JButton("Remove Image");
        deleteImageButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(editDialog, "Are you sure you want to remove the image?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                post.setImagePath(null);
                post.setLastModifiedDate(new Date());
                imageLabel.setIcon(null);
                imageLabel.setText("No Image Selected");
            }
        });
        editPanel.add(deleteImageButton);
    } else {
        // Add image button if no image exists
        JButton addImageButton = new JButton("Add Image");
        addImageButton.addActionListener(e -> {
            selectImageForPost();
            if (selectedImagePath != null) {
                post.setImagePath(selectedImagePath);
                imageLabel.setIcon(new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                post.setLastModifiedDate(new Date());
            }
        });
        editPanel.add(addImageButton);
    }
    // Save button
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {
        String updatedContent = postContent.getText().trim();
        if (updatedContent.isEmpty() && post.getImagePath() == null) {
            int confirm = JOptionPane.showConfirmDialog(editDialog, "If you leave the content empty, the post will be deleted. Do you want to continue?");
            if (confirm == JOptionPane.YES_OPTION) {
                groupManager.deletePostFromGroup(currentGroup.getGroupId(), post.getContentId());
                JOptionPane.showMessageDialog(editDialog, "Post deleted successfully!");
                editDialog.dispose();
                populateFeed();
            }
        } else {
            post.setContent(updatedContent);
            groupManager.updatePostInGroup(currentGroup.getGroupId(), post);
            JOptionPane.showMessageDialog(editDialog, "Post updated successfully!");
            editDialog.dispose();
            populateFeed();
        }
    });
    editPanel.add(saveButton);
    // Add the panel to the dialog
    editDialog.add(editPanel);
    editDialog.setVisible(true);
}
    private void deletePost(Post post) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this post?", "Delete Post", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            groupManager.deletePostFromGroup(currentGroup.getGroupId(), post.getContentId());
            JOptionPane.showMessageDialog(this, "Post deleted successfully!");
            populateFeed();
        }
    }
    private void addPostComponents() {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  
        
        JTextArea postContent = new JTextArea("Write your post here...");
        postContent.setPreferredSize(new Dimension(250, 100)); 
        postPanel.add(new JScrollPane(postContent));
        JButton imageButton = new JButton("Add Image");
        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
            }
        });
        postPanel.add(imageButton);
        jPanel1.add(postPanel, BorderLayout.SOUTH);
    }
private void postInGroup(Group group) {
    JDialog postDialog = new JDialog(this, "Post in Group: " + group.getName(), true);
    postDialog.setSize(400, 400);
    // Main panel for the dialog
    JPanel postPanel = new JPanel(new BorderLayout());
    // Text input area
    JTextArea postContent = new JTextArea();
    postContent.setPreferredSize(new Dimension(350, 100));
    JScrollPane textScrollPane = new JScrollPane(postContent);
    postPanel.add(textScrollPane, BorderLayout.NORTH);
    // Image preview panel
    JPanel imagePreviewPanel = new JPanel();
    JLabel imagePreviewLabel = new JLabel("No Image Selected");
    imagePreviewPanel.add(imagePreviewLabel);
    postPanel.add(imagePreviewPanel, BorderLayout.CENTER);
    // Image selection
    final String[] selectedImagePath = {null};
    JButton imageButton = new JButton("Add Image");
    imageButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            selectedImagePath[0] = file.getAbsolutePath();
            // Update the image preview
            try {
                if (file.exists()) {
                    ImageIcon imageIcon = new ImageIcon(selectedImagePath[0]);
                    Image scaledImage = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                    imagePreviewLabel.setText("");
                } else {
                    JOptionPane.showMessageDialog(postDialog, "The selected image file does not exist.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(postDialog, "Error displaying image: " + ex.getMessage());
            }
        }
    });
    // Button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.add(imageButton);
    JButton postButton = new JButton("Post");
    postButton.addActionListener(e -> {
        String content = postContent.getText().trim();
        // Validation for empty content and image
        if (content.isEmpty() && (selectedImagePath[0] == null || selectedImagePath[0].isEmpty())) {
            JOptionPane.showMessageDialog(postDialog, "Cannot create an empty post. Please add text or an image.");
            return;
        }
        // Create and add the post
        Post post = new Post(
            currentUser.getUserId(),
            content.isEmpty() ? null : content,
            selectedImagePath[0],
            new Date(),
           ( "Post-" + new Date().getTime())
        );
        groupManager.addPostToGroup(group.getGroupId(), post);
        JOptionPane.showMessageDialog(postDialog, "Post added successfully!");
        postDialog.dispose();
    });
    buttonPanel.add(postButton);
    postPanel.add(buttonPanel, BorderLayout.SOUTH);
    postDialog.add(postPanel);
    postDialog.setVisible(true);
}
   public static void createGroupPanel(Group groupViwed, User activeUser,JFrame mainFrame,  JFrame viewFrame) {
        // Create the main panel that will contain all components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
        
        // Panel for group title and manage group button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(groupViwed.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set big font for title
        
        // Add title label and button to the header panel
        headerPanel.add(titleLabel);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to the header panel
        headerPanel.setMaximumSize(new Dimension(350,600));
        
        // Add header panel to the main panel
        mainPanel.add(headerPanel);
        
        // Description panel
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        JLabel descriptionLabel = new JLabel("Group Desicription: " + groupViwed.getDescription());
        descriptionPanel.add(descriptionLabel);
        headerPanel.setMaximumSize(new Dimension(350,800));
        descriptionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to the description panel
        
        // Add description panel to the main panel
        mainPanel.add(descriptionPanel);
        
        // Create a container for the list of members (using JScrollPane for scrolling)
        JPanel membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        
        String primaryID = groupViwed.getPrimaryAdminId();
        ArrayList<String> adminIDs = groupViwed.getAdminIds();
        ArrayList<String> memberIDs = groupViwed.getMemberIds();
        
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement userAccountManagement = new UserAccountManagement(c);
        
        User primaryUser = userAccountManagement.getUserByEmail( userAccountManagement.getEmailByID(primaryID) );
     
        JPanel primaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel primaryLabel = new JLabel("Primary Admin: " + primaryUser.getUsername() +" (" +primaryUser.getstatus()+ ")");
        JButton primaryButton = new JButton("View Account");
        
        primaryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
            viewFrame.setVisible(false);
            ProfileManager p = new ProfileManager(primaryUser);
            ProfileGUI pGUI = new ProfileGUI(activeUser, p, true);
            mainFrame.setVisible(false);
            pGUI.setVisible(true);                }
            });
        
            primaryPanel.add(primaryLabel);
            primaryPanel.add(primaryButton);
            primaryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            membersPanel.add(primaryPanel);

        
        JLabel adminstitle = new JLabel("Admins: ");
        
        membersPanel.add(adminstitle);
        
        for (String adminID: adminIDs){
        
            User adminUser = userAccountManagement.getUserByEmail( userAccountManagement.getEmailByID(adminID) );
   
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel adminLabel = new JLabel("Admin: " + adminUser.getUsername() +" (" +adminUser.getstatus()+ ")");
        JButton adminButton = new JButton("View Account");
            
                    adminButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewFrame.setVisible(false);
            ProfileManager p = new ProfileManager(adminUser);
            ProfileGUI pGUI = new ProfileGUI(activeUser, p, true);
            mainFrame.setVisible(false);
            pGUI.setVisible(true);                }
            });
        
            adminPanel.add(adminLabel);
            adminPanel.add(adminButton);
            membersPanel.add(adminPanel);
        }

        JLabel memberTitle = new JLabel("Members: ");
        membersPanel.add(memberTitle);
        // Add a panel for each member with a button
        
        for (String member : memberIDs) {
 
        User memberUser = userAccountManagement.getUserByEmail( userAccountManagement.getEmailByID(member) );
 
        JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel memberLabel = new JLabel("member: " + memberUser.getUsername() +" (" +memberUser.getstatus()+ ")");
        JButton memberButton = new JButton("View Profile");
            
            memberButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewFrame.setVisible(false);
            ProfileManager p = new ProfileManager(memberUser);
            ProfileGUI pGUI = new ProfileGUI(activeUser, p, true);
            mainFrame.setVisible(false);
            pGUI.setVisible(true);                }
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
                
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setSize(500, 700);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this frame
        viewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Execute your specific function here
                // After handling your function, dispose the frame
//                viewFrame.dispose();
//                viewFrame = new JFrame("Group Properties");
            }
        });
        viewFrame.add(mainPanel);       
        viewFrame.setLocation(400, 120);
        
        viewFrame.setVisible(true);
//        setVisible(false);
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

        createGroupPanel(this.currentGroup, this.currentUser, this, this.viewFrame);
        
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
