/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package connecthub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author Asem
 */
public class ProfileGUI extends javax.swing.JFrame {

    private User currUser;
    private ProfileManager manager;
    private boolean access;
    public ProfileGUI(User user, ProfileManager manager, boolean access) {
      
        this.currUser = user;
        this.manager = manager;
        this.access = access;

        setTitle("Profile");
        setSize(1000, 500);
        initComponents();
        
        if (!access){
                    jButton1.setVisible(false);
                    jButton2.setVisible(false);
                    jButton3.setVisible(false);
                    jButton4.setVisible(false);

                }
        
        setLocationRelativeTo(null);
        setLayout(null);
        String ProfilePath = manager.getProfilePhoto();
        String CoverPath = manager.getCoverPhoto();

        // عرض الصور في الواجهه
        profileImageFrame1.setImage(ProfilePath);
        profileImageFrame1.setBorderSize(3);
        profileImageFrame1.setBorderColor(Color.black);
        coverImageFrame1.setBorderSize(5);
        coverImageFrame1.setBorderColor(Color.black);
        coverImageFrame1.setImage(CoverPath);
        profileImageFrame1.setPreferredSize(new Dimension(300, 40)); // أبعاد ثابتة        

                                                    // الكود عند الضغط على زرار البروفايل فوتو
        jButton2.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a Profile Picture");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();

                // تحديث الصورة في ProfileImageFrame
                profileImageFrame1.setImage(selectedFilePath);
                profileImageFrame1.repaint();
                manager.updateprofilephoto(selectedFilePath);
            }
            
                this.manager.saveToFile(JsonDataManger.PROFILES_FILE_NAME);

        });
                                                  // الكود عند الضغط على زرار تغيير الكوفر فوتو
        jButton1.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a Cover Picture");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();

                // تحديث الصورة في CoverImageFrame
                coverImageFrame1.setImage(selectedFilePath);
                coverImageFrame1.repaint();
                manager.updateCoverPhoto(selectedFilePath);
            }
                this.manager.saveToFile(JsonDataManger.PROFILES_FILE_NAME);
        });
                                                             // عرض البايو
        jLabel1.setText(manager.getUsername());
        jLabel2.setText("Bio : " + manager.getBio());
        // تعديل البايو
        jButton3.addActionListener(e -> {
            String newBio = JOptionPane.showInputDialog("Enter New Bio");
            manager.updateBio(newBio);
            jLabel2.setText("Bio : " + manager.getBio());
 this.manager.saveToFile(JsonDataManger.PROFILES_FILE_NAME);

        });
                                                        //تعديل الاسم 
     jButton6.addActionListener(e -> {
            String newUsername = JOptionPane.showInputDialog("Enter New User Name");
            manager.updateUsername(newUsername);
            jLabel1.setText("Username : "+manager.getUsername());
 this.manager.saveToFile(JsonDataManger.PROFILES_FILE_NAME);

        });
        
                                                           // عرض الاصدقاء
        
jPanel2.removeAll(); 
JLabel friendsLabel = new JLabel(" Friends");
friendsLabel.setFont(new Font("Arial", Font.BOLD, 18));  // تعيين الخط ليكون بحجم 18 وبالخط العريض
friendsLabel.setForeground(Color.WHITE);  // تعيين اللون الأبيض للنص
jPanel2.add(friendsLabel);  // إضافة الـ JLabel إلى الـ JPanel


    ConnectHubEngine c = new ConnectHubEngine();
    UserAccountManagement userAccountManagement = new UserAccountManagement(c);
    
    Set<String> friendsEmails = manager.user.getFriends();
    for (String email : friendsEmails) {
    // إنشاء JLabel لكل صديق
    String friendUserName = userAccountManagement.getUsernameByEmail(email);
    JLabel friendLabel = new JLabel(" >> " + friendUserName);
    jPanel2.add(friendLabel);  // إضافة الصديق إلى اللوحة
    }
//  ترتيب الأصدقاء عموديً
jPanel2.setPreferredSize(new Dimension(289, 260));
jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
jPanel2.setBorder(new LineBorder(Color.BLACK));
jPanel2.repaint();     // إعادة رسم المكونات
                                                                 //خلص عرض الاصدقاء
    
    // عرض البوستات 
    jPanel3.removeAll(); 
JLabel friendsPostsLabel = new JLabel("Posts");
friendsPostsLabel.setFont(new Font("Arial", Font.BOLD, 18));  // تعيين الخط ليكون بحجم 18 وبالخط العريض
friendsPostsLabel.setForeground(Color.WHITE);  // تعيين اللون الأبيض للنص

jPanel3.add(friendsPostsLabel);  // إضافة الـ JLabel إلى الـ JPanel
    for (JPanel post : manager.getPosts()) {
    jPanel3.add(post);  // إضافة الـ JLabel إلى الـ JPanel
}
    
jPanel3.setPreferredSize(new Dimension(680, 292));
jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
// إعادة رسم الـ JPanel بعد التحديث
jPanel3.setBorder(new LineBorder(Color.BLACK));

jPanel3.repaint();  



jButton4.addActionListener(e -> {
    // استخدام JPasswordField للحصول على المدخلات ككلمة مرور
    JPasswordField pass1Field = new JPasswordField(20);
    JPasswordField pass2Field = new JPasswordField(20);
    
    JPanel panel = new JPanel();
    panel.add(new JLabel("Enter New Password:"));
    panel.add(pass1Field);
    panel.add(new JLabel("Confirm New Password:"));
    panel.add(pass2Field);
    
    int option = JOptionPane.showConfirmDialog(this, panel, "Enter New Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
    if (option == JOptionPane.OK_OPTION) {
        // الحصول على كلمات المرور المدخلة
        String pass1 = new String(pass1Field.getPassword());
        String pass2 = new String(pass2Field.getPassword());
        
        if (pass1.isBlank() || pass2.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields !!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "The passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            manager.updatePassword(pass2);
            JOptionPane.showMessageDialog(null, "Password changed successfully!", "Save Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    this.manager.saveToFile(JsonDataManger.PROFILES_FILE_NAME);
    
});


    
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
            // Perform logout or any other necessary cleanup
            ConnectHubEngine c = new ConnectHubEngine();
            UserAccountManagement userAccountManagement = new UserAccountManagement(c);
            
            userAccountManagement.logout(user.getEmail());
            System.out.println("Logout");
            
            frame.dispose(); // Close the window
        } else {
            // Do nothing to ensure the window remains open
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
});

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        coverImageFrame1 = new connecthub.CoverImageFrame();
        profileImageFrame1 = new connecthub.ProfileImageFrame();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Profile");
        setBackground(new java.awt.Color(255, 255, 255));

        coverImageFrame1.setBackground(new java.awt.Color(153, 153, 255));
        coverImageFrame1.setBorderColor(new java.awt.Color(255, 255, 102));

        profileImageFrame1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        profileImageFrame1.setForeground(new java.awt.Color(102, 255, 51));
        profileImageFrame1.setMaximumSize(new java.awt.Dimension(100, 100));
        coverImageFrame1.add(profileImageFrame1);
        profileImageFrame1.setBounds(110, 70, 100, 100);

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

        jButton2.setBackground(new java.awt.Color(10, 57, 129));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Edit ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        coverImageFrame1.add(jButton2);
        jButton2.setBounds(30, 140, 61, 23);

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

        jButton5.setText("<--");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        coverImageFrame1.add(jButton5);
        jButton5.setBounds(20, 10, 72, 23);

        jPanel1.setBackground(new java.awt.Color(212, 235, 248));

        jLabel1.setBackground(new java.awt.Color(10, 57, 129));
        jLabel1.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(10, 57, 129));
        jLabel1.setText("jLabel1");

        jLabel2.setBackground(new java.awt.Color(227, 142, 73));
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(227, 142, 73));
        jLabel2.setText("jLabel2");

        jButton3.setBackground(new java.awt.Color(227, 142, 73));
        jButton3.setText("Edit Bio");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(129, 191, 218));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Friends");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(191, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 235, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(129, 191, 218));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 758, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton6.setBackground(new java.awt.Color(227, 142, 73));
        jButton6.setText("Edit User Name");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.getAccessibleContext().setAccessibleName("friends");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(coverImageFrame1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(coverImageFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.setVisible(false);
        NewsFeed n = new NewsFeed(currUser);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private connecthub.CoverImageFrame coverImageFrame1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private connecthub.ProfileImageFrame profileImageFrame1;
    // End of variables declaration//GEN-END:variables
}
