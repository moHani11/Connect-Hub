package connecthub;

import connecthub.Post;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicArrowButton;
import java.util.List;
import java.util.Set;


public class NewsFeed extends javax.swing.JFrame {

    ArrayList<String> friends = new ArrayList<>(); // Bagarab beeha
    ArrayList<String> exploreFriends = new ArrayList<>(); // Bagarab beeha
    ArrayList<JPanel> posts = new ArrayList<>(5);
    ArrayList<JPanel> stories = new ArrayList<>(5);
    
    
    ArrayList<JPanel> postss;    
    private User user;
    ConnectHubEngine c = new ConnectHubEngine();
    UserAccountManagement userAccountManagement = new UserAccountManagement(c);
    int FRAME_HEIGHT = 700;
    int PROPERTIES_PANEL_HEIGHT = 40;
    int STORIES_PANEL_HEIGHT = 100;

    boolean friendsPanelViewed = false;
    JPanel friendsPanel = null;
    boolean explorePanelViewed = false;
    JPanel explorePanel = null;
    public NewsFeed(User user) {
    this.setLocation(400, 120);
    this.user = user;
    generateFeedPosts();

    initComponents();
       // Add the search bar to panel
    postsFeed();
    storiesFeed();
    this.setVisible(true);

    JFrame frame = this;
    frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to leave Connect Hub?",
                    "Confirm Close",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (response == JOptionPane.YES_OPTION) {
                ConnectHubEngine c = new ConnectHubEngine();
                UserAccountManagement userAccountManagement = new UserAccountManagement(c);

                userAccountManagement.logout(user.getEmail());
                System.out.println("Logout");

                frame.dispose(); // Close the window
            } else {
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        }
    });
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        search = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(200, 0, 242));

        jPanel3.setBackground(new java.awt.Color(204, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel3.setForeground(new java.awt.Color(204, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 255), new java.awt.Color(204, 204, 255), null, null));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 700));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 700));

        jButton7.setBackground(new java.awt.Color(198, 231, 231));
        jButton7.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connecthub/addResized.jpeg"))); // NOI18N
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(198, 231, 231));
        jButton4.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton4.setText("ViewProfile");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(198, 231, 231));
        jButton8.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton8.setText("View Friends");
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(198, 231, 231));
        jButton5.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton5.setText("Manage Friendships");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(198, 231, 231));
        jButton9.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton9.setText("< --Logout ");
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(198, 231, 231));
        jButton10.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connecthub/refreshResized.png"))); // NOI18N
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        search.setBackground(new java.awt.Color(198, 231, 231));
        search.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        search.setText("search");
        search.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(198, 231, 231));
        jButton11.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jButton11.setText("Gps");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(540, Short.MAX_VALUE)
                    .addComponent(jButton5)
                    .addGap(112, 112, 112)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(508, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

//        this.showCustomDialog();
    this.setVisible(false);
    SocialMediaApp s = new SocialMediaApp(this.user);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
     
        ProfileManager p = new ProfileManager(user);
        ProfileGUI pGUI = new ProfileGUI(user, p, true);
        this.setVisible(false);
        pGUI.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

       if (explorePanelViewed){
           this.explorePanel.setVisible(false);
            this.getLayeredPane().remove(explorePanel);
           explorePanelViewed = false;
       }
       
       if (friendsPanelViewed){
           this.friendsPanel.setVisible(false);
            this.getLayeredPane().remove(friendsPanel);
           friendsPanelViewed = false;
       }
       else{
        this.friendsPanel = this.updateFriendsPanel();
        
        this.getLayeredPane().add(friendsPanel, JLayeredPane.PALETTE_LAYER);
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // Dynamically adjust the panel's position
                int newX = getWidth() - friendsPanel.getWidth() - 20;
                int newY = PROPERTIES_PANEL_HEIGHT + 23;
                friendsPanel.setLocation(newX, newY);
            }
        });
        
        
        this.setVisible(true);
        friendsPanelViewed = true;
       }


    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

                       UserAccountManagement userAccountManagement = new UserAccountManagement(new ConnectHubEngine());
                FriendsListWithRequest friendsList = new FriendsListWithRequest(userAccountManagement,user.getEmail(),new ConnectHubEngine(), user);
               
                this.setVisible(false);
                friendsList.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement accountManagement = new UserAccountManagement(c);
        
        accountManagement.logout(user.getEmail());

        UserAccountManagmentGUI login = new UserAccountManagmentGUI(accountManagement, c);
        
        this.setVisible(false);
        login.setVisible(true);

        c = null;
        accountManagement = null;
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        NewsFeed newsFeed = new NewsFeed(user);
        this.setVisible(false);
        newsFeed.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
       // When the search button is clicked, open TRYNewsfeedWithSearchBar
        TRYNewsfeedWithSearchBar newsFeedWithSearchBar = new TRYNewsfeedWithSearchBar(user, userAccountManagement);
        this.setVisible(false);  // Hide the current frame
        newsFeedWithSearchBar.setVisible(true);  // Show the new frame
    }//GEN-LAST:event_searchActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        
        this.setVisible(false);
        GroupManager groupManager = new GroupManager();
        new GroupManagementGUI(groupManager, user);
        
    }//GEN-LAST:event_jButton11ActionPerformed

    public JPanel updateFriendsPanel(){

        int panel_width = 220;
        int panel_height = FRAME_HEIGHT;
        
        
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement userAccountManagement = new UserAccountManagement(c);
        user = c.updateUser(userAccountManagement, user);
        Set<String> friendsSet = user.getFriends();
        JPanel friendsPanel = new JPanel();

        friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.Y_AXIS)); // Vertical layout
        friendsPanel.setBackground(new Color(198,231,231));
        friendsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel friendTitle = new JLabel("   Friends List   ");
        friendTitle.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        friendTitle.setLayout(new FlowLayout(FlowLayout.LEFT));

        friendTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); 
        friendTitle.setFont(new Font("Arial", Font.ITALIC, 24));
        friendsPanel.add(friendTitle, BorderLayout.WEST);
        friendsPanel.add(new JLabel("\n"));
        friendsPanel.add(new JLabel("\n"));
        // Add each friend's name to the panel

    for (String email : friendsSet) {
        User friend = userAccountManagement.userDatabase.get(email);
        String username = friend.getUsername();
        String status = friend.isOnline() ? "Online" : "Offline";

        JPanel friendPanel = new JPanel();
        friendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel friendLabel = new JLabel(username + " (" + status + ")");
        friendLabel.setBackground(new Color(208, 201, 191));
        friendPanel.setBackground(new Color(108, 201, 191));
        friendLabel.setFont(new Font("Arial", Font.ROMAN_BASELINE, 17));
        friendLabel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        JButton ViewButton = new JButton("View Account");
                ViewButton.addActionListener(e -> {
        ProfileManager p = new ProfileManager(friend);
        ProfileGUI pGUI = new ProfileGUI(user, p, false);
        this.setVisible(false);
        pGUI.setVisible(true);
         
        });
        
        
        friendPanel.add(friendLabel);
        friendPanel.add(ViewButton);
        friendsPanel.add(friendPanel);
}
        friendsPanel.setSize(panel_width+100, panel_height);
        friendsPanel.setLocation(getWidth() - friendsPanel.getWidth() - 20, PROPERTIES_PANEL_HEIGHT + 13);
       
    friendsPanel.revalidate();
    friendsPanel.repaint();
        
        

        return friendsPanel;

    }

  
    public JPanel updateExplorePanel(){
      int panel_width = 300;
        int panel_height = FRAME_HEIGHT;

        JPanel explorePanel = new JPanel();

        explorePanel.setLayout(new BoxLayout(explorePanel, BoxLayout.Y_AXIS)); // Vertical layout
        explorePanel.setBackground(new Color(198,231,231));
        explorePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        explorePanel.add(friendButton());
        
        JLabel friendTitle = new JLabel("Explore New Friends");
        friendTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); 
        friendTitle.setFont(new Font("Arial", Font.ITALIC, 20));
        explorePanel.add(friendTitle);
        explorePanel.add(new JLabel("\n"));
        explorePanel.add(new JLabel("\n"));
        // Add each friend's name to the panel
        for (String friend : this.exploreFriends) {
            JPanel friendEntryPanel = new JPanel();
            friendEntryPanel.setLayout(new BoxLayout(friendEntryPanel, BoxLayout.X_AXIS)); // Horizontal layout
            friendEntryPanel.setBackground(new Color(108, 201, 191)); // Match parent panel background
            friendEntryPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder()); // Padding
        // Friend's name label
            JLabel friendLabel = new JLabel(friend);
            friendLabel.setFont(new Font("Arial", Font.ROMAN_BASELINE, 18));
            friendLabel.setBorder(BorderFactory.createEtchedBorder());

        // Add Friend button
            JButton addButton = addFriend(friendEntryPanel);
            
        // Add components to the horizontal panel
            friendEntryPanel.add(friendLabel);
            friendEntryPanel.add(Box.createHorizontalGlue()); // Push button to the right
            friendEntryPanel.add(addButton);

        // Add the horizontal panel to the main explore panel
            friendEntryPanel.setSize(panel_width, 80);
            explorePanel.add(friendEntryPanel);
            explorePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing between entries
  
        }
        
            explorePanel.setSize(panel_width, panel_height);
            explorePanel.setLocation(getWidth() - explorePanel.getWidth() - 20, PROPERTIES_PANEL_HEIGHT + 13);
        
        return explorePanel;
    }
   
    
    public JButton friendButton(){
            JButton addButton = new JButton("Manage Friendships");
            addButton.setFont(new Font("Arial", Font.PLAIN, 14));
            addButton.addActionListener(e -> {
                UserAccountManagement userAccountManagement = new UserAccountManagement(new ConnectHubEngine());
                FriendsListWithRequest friendsList = new FriendsListWithRequest(userAccountManagement,user.getEmail(),new ConnectHubEngine(), user);
               
                this.setVisible(false);
                friendsList.setVisible(true);
        });
            return addButton;
    }
    
    public JButton addFriend(JPanel friendEntryPanel){
            JButton addButton = new JButton("Add");
            addButton.setFont(new Font("Arial", Font.PLAIN, 14));
            addButton.addActionListener(e -> {
                // Remove the button and add a label saying "ADDED"
                friendEntryPanel.remove(addButton);
                JLabel addedLabel = new JLabel("Pending  ");
                addedLabel.setFont(new Font("Arial", Font.BOLD, 16));
                addedLabel.setForeground(Color.GREEN); 
            
            
                JButton rmvButton = unAdd(friendEntryPanel, addedLabel);
            
                friendEntryPanel.add(addedLabel);
                friendEntryPanel.add(rmvButton);
                friendEntryPanel.revalidate();
                friendEntryPanel.repaint();
        });
            
            return addButton;
    }
    
    public JButton unAdd(JPanel friendEntryPanel, JLabel adddedLabel){
            JButton unaddButton = new JButton("Un Add");
            unaddButton.setFont(new Font("Arial", Font.PLAIN, 14));
            unaddButton.addActionListener(e -> {
                // Remove the button and add a label saying "ADDED"
                friendEntryPanel.remove(unaddButton);
                friendEntryPanel.remove(adddedLabel);
                  
                JButton addButton = addFriend(friendEntryPanel);
            
                friendEntryPanel.add(addButton);
                friendEntryPanel.revalidate();
                friendEntryPanel.repaint();
        });
            return unaddButton;
    }
 
    public void storiesFeed(){
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.X_AXIS)); // 2 rows, 1 columns, 10px gaps

        ArrayList<Story> storiesArray = new ArrayList<>();
//        for (User friend: this.friendss){
             List<String> friendsList = new ArrayList<>(this.user.getFriends());
            storiesArray.addAll(this.user.storyManager.getStoriesByFriends(friendsList));
//        }
        
        
        for (Story story : storiesArray) {
            JPanel storyCard = SocialMediaApp.createStoryCard(story);  // Create card for each story
            stories.add(storyCard);
        }
                
        for (JPanel storie: this.stories){
            
            storie.setMaximumSize(new Dimension(100,80));
            storie.setBorder(BorderFactory.createLineBorder(new Color(100,100,200), 3));
            jPanel2.add(storie);

            JPanel paddingPanel = new JPanel();
            paddingPanel.setMaximumSize(new Dimension(10, 0)); // 20px vertical space
            paddingPanel.setOpaque(false); // Transparent padding
            jPanel2.add(paddingPanel);
        }
    
    }
    
    public void generateFeedPosts(){
        ArrayList<Post> postsArray = new ArrayList<>();
             List<String> friendsList = new ArrayList<>(this.user.getFriends());
            postsArray.addAll(user.postManager.getPostsByFriends(friendsList));
        
        this.postss = new ArrayList<>();

    for (Post post : postsArray) {
            JPanel panel = SocialMediaApp.createPostCard(post);  // Create card for each post
            this.postss.add(panel);
        }

    }
    
    public void postsFeed(){
        
        int posts_width  = getWidth() - 30;
        int posts_height = getHeight() - PROPERTIES_PANEL_HEIGHT - STORIES_PANEL_HEIGHT;
        
        int LIKE_BUTTON_WIDTH = 30;
        int LIKE_BUTTON_HEIGHT = 15;
        int COMMENT_BUTTON_WIDTH = 40;
        int COMMENT_BUTTON_HEIGHT = 15;

        
        JPanel postsPanel = new JPanel();
        postsPanel.setBackground(Color.BLACK);
        postsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE)); // Padding
        
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS)); // 2 rows, 1 columns, 10px gaps
        postsPanel.setSize(posts_width, posts_height);
        postsPanel.setLocation(10, PROPERTIES_PANEL_HEIGHT + STORIES_PANEL_HEIGHT + 10);
        
        
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.X_AXIS)); // 2 rows, 1 columns, 10px gaps
        previewPanel.setMaximumSize(new Dimension(posts_width, posts_height-LIKE_BUTTON_HEIGHT-60));
        // Second row: A panel and a scrollbar
        
        CardLayout cardLayout = new CardLayout();
        JPanel changJPanel = new JPanel(cardLayout); // Parent panel with CardLayout

        // Create panels (frames) to switch between
        JPanel panel1 = createPanel("Frame 1", Color.LIGHT_GRAY);
        JPanel panel2 = createPanel("Frame 2", Color.CYAN);
        JPanel panel3 = createPanel("Frame 3", Color.PINK);
 
        // Add panels to the main panel
        for (JPanel panel: this.postss) changJPanel.add(panel, "o");
     changJPanel.setSize(posts_width - 20, posts_height-LIKE_BUTTON_HEIGHT-15);
        changJPanel.setMaximumSize(new Dimension(posts_width - 20, posts_height-LIKE_BUTTON_HEIGHT-15));
        changJPanel.setBackground(Color.LIGHT_GRAY);
        


        JPanel scrollPanel = new JPanel();
        scrollPanel.setMaximumSize(new Dimension(20, posts_height-LIKE_BUTTON_HEIGHT-15));
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS)); 
        
        JButton upButton = new BasicArrowButton(SwingConstants.NORTH);
        JButton downButton = new BasicArrowButton(SwingConstants.SOUTH);
        
        upButton.setMaximumSize(new Dimension(20, (posts_height-LIKE_BUTTON_HEIGHT-15)/2 ) );
        downButton.setMaximumSize(new Dimension(20, (posts_height-LIKE_BUTTON_HEIGHT-15)/2) );

                // Action listeners for the buttons
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(changJPanel); // Show the previous panel
            }
        });

        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.next(changJPanel); // Show the next panel
            }
        });

        scrollPanel.add(upButton);
        scrollPanel.add(downButton);

        previewPanel.add(changJPanel); // Panel for changing content
        previewPanel.add(scrollPanel);
        
        JPanel postsActionsPanel = new JPanel();
        postsActionsPanel.setSize(posts_width, LIKE_BUTTON_HEIGHT + 10);
        postsActionsPanel.setLayout(new BoxLayout(postsActionsPanel, BoxLayout.X_AXIS)); 
        
        JButton likeButton = new JButton("Like");
        JButton commentButton = new JButton("Comment");
        likeButton.setSize(LIKE_BUTTON_WIDTH, LIKE_BUTTON_HEIGHT);
        commentButton.setSize(COMMENT_BUTTON_WIDTH, COMMENT_BUTTON_HEIGHT);
        
        postsActionsPanel.add(likeButton);
        postsActionsPanel.add(commentButton);
        
        
        postsPanel.add(previewPanel);
        postsPanel.add(postsActionsPanel);
        
        
        this.getLayeredPane().add(postsPanel, JLayeredPane.DEFAULT_LAYER);
        this.setVisible(true);
    }
    
    private static JPanel createPanel(String title, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
    
     public void showCustomDialog() {
        // Create a custom JDialog
        JDialog dialog = new JDialog(this, "Create New Content", true);
        dialog.setSize(600, 200);
        dialog.setLayout(new BorderLayout());

        // Add a title label
        JLabel titleLabel = new JLabel("Create New Content!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Create buttons
        JButton postButton = new JButton("Make a new Post");
        JButton storyButton = new JButton("Add a Story");

        // Customize button styles
        postButton.setPreferredSize(new Dimension(200, 40));
        postButton.setBackground(new Color(59, 89, 152));
        postButton.setForeground(Color.WHITE);
        postButton.setFocusPainted(false);
        postButton.setFont(new Font("Arial", Font.BOLD, 17));

        storyButton.setPreferredSize(new Dimension(200, 40));
        storyButton.setBackground(new Color(72, 181, 166));
        storyButton.setForeground(Color.WHITE);
        storyButton.setFocusPainted(false);
        storyButton.setFont(new Font("Arial", Font.BOLD, 17));

        // Add button actions
        postButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Post selected!");
            dialog.dispose();
        });

        storyButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Story selected!");
            dialog.dispose();
        });

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20)); // Set spacing between buttons
        buttonPanel.add(postButton);
        buttonPanel.add(storyButton);

        dialog.add(buttonPanel, BorderLayout.CENTER);

        // Center the dialog on the screen
        dialog.setLocationRelativeTo(this);

        // Make the dialog visible
        dialog.setVisible(true);
    }
    
     
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new NewsFeed(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton search;
    // End of variables declaration//GEN-END:variables
}
