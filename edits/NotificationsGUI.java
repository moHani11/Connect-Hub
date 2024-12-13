/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package connecthub;

import connecthub.Notification;
import connecthub.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Asem
 */
public class NotificationsGUI extends javax.swing.JFrame {

    private User user;
    private NotificationManager manager;

    /**
     * Creates new form NotificationsGUI
     */
    public NotificationsGUI(User user) throws IOException {
        this.user = user;
        setTitle("Notifications");
        setSize(420, 420);
        manager = new NotificationManager();
        setLocationRelativeTo(null);
        initComponents();
        this.setLayout(null);
        jPanel1.removeAll();
        JLabel NotiLabel = new JLabel("Notifications :");
        NotiLabel.setFont(new Font("Arial", Font.BOLD, 18));  // تعيين الخط ليكون بحجم 18 وبالخط العريض
        NotiLabel.setForeground(Color.black);  // تعيين اللون الأبيض للنص
        jLabel2.setText("Number of Unread Notification : " + String.valueOf(manager.getNotificationsForUser(user.getEmail()).size()));
        jPanel1.add(NotiLabel);  // إضافة الـ JLabel إلى الـ JPanel
//        jPanel1.setLayout(null);
//        jPanel1.add(jLabel2);  // إضافة الـ JLabel إلى الـ JPanel
//        jPanel1.add(jLabel3);  // إضافة الـ JLabel إلى الـ JPanel

        // الحصول على إشعارات المستخدم
        List<Notification> notifications = manager.getNotificationsForUser(user.getEmail());

        if (notifications.isEmpty()) {
            JLabel noNotificationsLabel = new JLabel("No notifications available.");
            noNotificationsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noNotificationsLabel.setForeground(Color.black);
            jPanel1.add(noNotificationsLabel);  // إضافة النص عندما لا توجد إشعارات
        } else {
            // إضافة كل إشعار إلى الـ JPanel
            for (Notification noti : notifications) {
                // إنشاء JLabel للإشعار
                JLabel NotiLabel1 = new JLabel(noti.getMessage());
                NotiLabel1.setFont(new Font("Arial", Font.PLAIN, 14));
                NotiLabel1.setForeground(Color.BLACK);

                // التحقق من نوع الإشعار
                if (noti.getType().equals("FriendRequest")) {
                    // زر "Open Friends"
                    JButton openFriendsButton = new JButton("Open Friends");
                    openFriendsButton.setFont(new Font("Arial", Font.PLAIN, 12));
                    openFriendsButton.setForeground(Color.WHITE);
                    openFriendsButton.setBackground(Color.GREEN);

                    // زر "Mark as Read"
                    JButton markAsReadButton = new JButton("Mark as Read");
                    markAsReadButton.setFont(new Font("Arial", Font.PLAIN, 12));
                    markAsReadButton.setForeground(Color.WHITE);
                    markAsReadButton.setBackground(Color.BLUE);

                    // إضافة ActionListener لزر "Open Friends"
                    openFriendsButton.addActionListener(e -> {
                        System.out.println("Opening friends window for user: " + user.getEmail());

                        UserAccountManagement userAccountManagement = new UserAccountManagement(new ConnectHubEngine());
                        FriendsListWithRequest friendsList = new FriendsListWithRequest(userAccountManagement, user.getEmail(), new ConnectHubEngine(), user);

                        this.setVisible(false);
                        friendsList.setVisible(true);
                    });

                    // إضافة ActionListener لزر "Mark as Read"
                    markAsReadButton.addActionListener(e -> {
                        noti.setIsRead(true);
                        manager.markAsRead(noti.getNotificationId());

                        // إزالة الإشعار والزر "Mark as Read" فقط
                        jPanel1.remove(NotiLabel1);
                        jPanel1.remove(markAsReadButton);
                        manager.deleteNotification(noti.getNotificationId());
                        jLabel2.setText("Number of Unread Notification : " + String.valueOf(manager.getNotificationsForUser(user.getEmail()).size()));

                        // تحديث الواجهة
                        jPanel1.revalidate();
                        jPanel1.repaint();

                        System.out.println("Notification marked as read: " + noti.getMessage());
                    });

                    // إضافة الإشعار والأزرار إلى الواجهة
                    jPanel1.add(NotiLabel1);
                    jPanel1.add(openFriendsButton);
                    jPanel1.add(markAsReadButton);

                } else {
                    // زر عام للإشعارات الأخرى
                    JButton markAsReadButton = new JButton("Mark as Read");
                    markAsReadButton.setFont(new Font("Arial", Font.PLAIN, 12));
                    markAsReadButton.setForeground(Color.WHITE);
                    markAsReadButton.setBackground(Color.BLUE);

                    // إضافة ActionListener للزر
                    markAsReadButton.addActionListener(e -> {
                        noti.setIsRead(true);
                        manager.markAsRead(noti.getNotificationId());
                        manager.deleteNotification(noti.getNotificationId());
                        jLabel2.setText("Number of Unread Notification : " + String.valueOf(manager.getNotificationsForUser(user.getEmail()).size()));

                        // إزالة الإشعار والزر
                        jPanel1.remove(NotiLabel1);
                        jPanel1.remove(markAsReadButton);

                        // تحديث الواجهة
                        jPanel1.revalidate();
                        jPanel1.repaint();

                        System.out.println("Notification marked as read: " + noti.getMessage());
                    });

                    // إضافة الإشعار والزر إلى الواجهة
                    jPanel1.add(NotiLabel1);
                    jPanel1.add(markAsReadButton);
                }
            }
        }

        // ترتيب الإشعارات عموديًا
        jPanel1.setPreferredSize(new Dimension(289, 260));
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
        jPanel1.repaint();
    }

    private NotificationsGUI() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connecthub/Firefly_20241213032551-removebg-preview_1_optimized - Copy.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 337, Short.MAX_VALUE)
        );

        jLabel2.setText("Number of Unread Notification :");

        jButton5.setText("<--");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.setVisible(false);
        NewsFeed n = new NewsFeed(user);
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NotificationsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotificationsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotificationsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotificationsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NotificationsGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
