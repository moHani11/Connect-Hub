/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
<<<<<<< HEAD:src/connecthub/FriendManagementGUI.java
package connecthub;
=======
package connecthup;
>>>>>>> cfe010f572d5ad6d25053bf1ec28d3883df0df75:src/connecthup/FriendManagementGUI.java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

<<<<<<< HEAD:src/connecthub/FriendManagementGUI.java

=======
/**
 *
 * @author mohamed
 */
>>>>>>> cfe010f572d5ad6d25053bf1ec28d3883df0df75:src/connecthup/FriendManagementGUI.java
public class FriendManagementGUI extends javax.swing.JFrame {
 private UserAccountManagement userAccountManagement;
    private FriendManagement friendManagement;
    private String currentUserEmail;
    /**
     * Creates new form FriendManagementGUI
     */
    public FriendManagementGUI(UserAccountManagement userAccountManagement, FriendManagement friendManagement, String currentUserEmail) {
         initComponents();
        this.userAccountManagement = userAccountManagement;
        this.friendManagement = friendManagement;
        this.currentUserEmail = currentUserEmail;

        loadFriendSuggestions();
        loadFriendRequests();
        loadFriendList();
         setLocationRelativeTo(null);
    }
     private void loadFriendSuggestions() {
        DefaultListModel<String> model = new DefaultListModel<>();
        Set<String> allUsers = userAccountManagement.getAllUserEmails();
        Set<String> friends = userAccountManagement.getFriends(currentUserEmail);
        Set<String> blocked = userAccountManagement.getBlockedUsers(currentUserEmail);

        for (String email : allUsers) {
            if (!email.equals(currentUserEmail) && !friends.contains(email) && !blocked.contains(email)) {
                model.addElement(userAccountManagement.getUsernameByEmail(email));
            }
        }
        friendsuggestionlist.setModel(model);
    }
<<<<<<< HEAD:src/connecthub/FriendManagementGUI.java
      public void loadFriendRequests() {
    DefaultListModel<String> model = new DefaultListModel<>();
    
    // Get the friend requests for the current user
    Set<String> requests = friendManagement.getFriendRequests(currentUserEmail); 
    
    // Check if requests exist and add them to the list
    if (requests != null && !requests.isEmpty()) {
        for (String senderEmail : requests) {
            String username = userAccountManagement.getUsernameByEmail(senderEmail);
            if (username != null) {
                model.addElement(username);  // Add the username of the sender to the list
            }
        }
    }friendrequestlist.setModel(model);
}
=======
      private void loadFriendRequests() {
        DefaultListModel<String> model = new DefaultListModel<>();
        Set<String> requests = friendManagement.getFriendRequests(currentUserEmail); // Get friend requests
        for (String senderEmail : requests) {
            model.addElement(userAccountManagement.getUsernameByEmail(senderEmail)); // Display usernames
        }
        friendrequestlist.setModel(model); // Update the friend requests list
    }
>>>>>>> cfe010f572d5ad6d25053bf1ec28d3883df0df75:src/connecthup/FriendManagementGUI.java
     private void loadFriendList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String email : userAccountManagement.getFriends(currentUserEmail)) {
            model.addElement(userAccountManagement.getUsernameByEmail(email));
        }
        friendlist.setModel(model);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        friendlist = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Remove = new javax.swing.JButton();
        Block = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        Accept = new javax.swing.JButton();
        Decline = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        friendrequestlist = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        Addfriend = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        friendsuggestionlist = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(friendlist);

        jLabel3.setText("Do You want to Remove OR Block Friend?");

        jLabel4.setText("Enter username:");

        Remove.setText("Remove");
        Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveActionPerformed(evt);
            }
        });

        Block.setText("Block");
        Block.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(Remove)
                        .addGap(88, 88, 88)
                        .addComponent(Block, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(jLabel4)))
                .addContainerGap(262, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Remove)
                    .addComponent(Block))
                .addGap(0, 147, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Friend List", jPanel1);

        Accept.setText("Accept");
        Accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcceptActionPerformed(evt);
            }
        });

        Decline.setText("Decline");
        Decline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeclineActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(friendrequestlist);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                .addComponent(Accept)
                .addGap(27, 27, 27)
                .addComponent(Decline)
                .addGap(219, 219, 219))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(171, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Decline)
                    .addComponent(Accept))
                .addGap(151, 151, 151))
            .addComponent(jScrollPane3)
        );

        jTabbedPane1.addTab("Friend Requests", jPanel2);

        Addfriend.setText("Add friend");
        Addfriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddfriendActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(friendsuggestionlist);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addComponent(Addfriend)
                .addGap(311, 311, 311))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(Addfriend))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Friend Suggestions", jPanel3);

        jLabel1.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel1.setText("Friend Managment");

        back.setText("<--");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(back)
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(back)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlockActionPerformed
        String username = friendlist.getSelectedValue();
        if (username != null) {
            String email = userAccountManagement.getEmailByUsername(username);
            friendManagement.blockUser(currentUserEmail, email);
            JOptionPane.showMessageDialog(this, "User blocked!");
            loadFriendList();
            loadFriendSuggestions();
        }
    
    }//GEN-LAST:event_BlockActionPerformed

    private void AddfriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddfriendActionPerformed
     String username = friendsuggestionlist.getSelectedValue();
<<<<<<< HEAD:src/connecthub/FriendManagementGUI.java
    if (username != null) {
        // Get the email of the selected user
        String email = userAccountManagement.getEmailByUsername(username);

        // Send friend request from current user to the selected user
        friendManagement.sendFriendRequest(currentUserEmail, email);

        // Remove the added user from the friend suggestions list
        loadFriendSuggestions();  // Reload the friend suggestions to reflect the changes

        // Optionally, you can also show a success message
        JOptionPane.showMessageDialog(this, "Friend request sent to " + username + "!");
    }
=======
        if (username != null) {
            String email = userAccountManagement.getEmailByUsername(username);
            friendManagement.sendFriendRequest(currentUserEmail, email);
            JOptionPane.showMessageDialog(this, "Friend request sent!");
            loadFriendSuggestions();
        }
>>>>>>> cfe010f572d5ad6d25053bf1ec28d3883df0df75:src/connecthup/FriendManagementGUI.java
    }//GEN-LAST:event_AddfriendActionPerformed

    private void AcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcceptActionPerformed
      String username = friendrequestlist.getSelectedValue();
        if (username != null) {
            String email = userAccountManagement.getEmailByUsername(username);
            friendManagement.acceptFriendRequest(email, currentUserEmail);
            JOptionPane.showMessageDialog(this, "Friend request accepted!");
            loadFriendRequests();
            loadFriendList();
        }
    }//GEN-LAST:event_AcceptActionPerformed

    private void DeclineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeclineActionPerformed
      String username = friendrequestlist.getSelectedValue();
        if (username != null) {
            String email = userAccountManagement.getEmailByUsername(username);
            friendManagement.declineFriendRequest(email, currentUserEmail);
            JOptionPane.showMessageDialog(this, "Friend request declined!");
            loadFriendRequests();
        }
    }//GEN-LAST:event_DeclineActionPerformed

    private void RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveActionPerformed
       String username = friendlist.getSelectedValue();
        if (username != null) {
            String email = userAccountManagement.getEmailByUsername(username);
            friendManagement.removeFriend(currentUserEmail, email);
            JOptionPane.showMessageDialog(this, "Friend removed!");
            loadFriendList();
            loadFriendSuggestions();
        }
    }//GEN-LAST:event_RemoveActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
    
    }//GEN-LAST:event_backActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Accept;
    private javax.swing.JButton Addfriend;
    private javax.swing.JButton Block;
    private javax.swing.JButton Decline;
    private javax.swing.JButton Remove;
    private javax.swing.JButton back;
    private javax.swing.JList<String> friendlist;
    private javax.swing.JList<String> friendrequestlist;
    private javax.swing.JList<String> friendsuggestionlist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
