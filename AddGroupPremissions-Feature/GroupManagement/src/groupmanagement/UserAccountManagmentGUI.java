package connecthub;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author mohamed
 */
public class UserAccountManagmentGUI extends javax.swing.JFrame {
 private UserAccountManagement userAccountManagement;
 private ConnectHubEngine cEngine;

 
    public UserAccountManagmentGUI(UserAccountManagement userAccountManagement, ConnectHubEngine c) {
       this.userAccountManagement = userAccountManagement;
       this.cEngine = c;
        initComponents();
          setTitle("Connect-Hub");
        setLocationRelativeTo(null);
        
        Email.setForeground(Color.GRAY);
        Email.setText("Email");

        // إضافة FocusListener لتغيير النص
        Email.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Email.getText().equals("Email")) {
                    Email.setText("");
                    Email.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Email.getText().isEmpty()) {
                    Email.setForeground(Color.GRAY);
                    Email.setText("Email");
                }
            }
        });
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        Login = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        SignUp = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        Email = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Login.setBackground(new java.awt.Color(153, 153, 153));
        Login.setText("Login");
        Login.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginActionPerformed(evt);
            }
        });
        getContentPane().add(Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 130, 20));

        jLabel2.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connecthub/logo.png"))); // NOI18N
        jLabel2.setText("WELCOME!");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 80));

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel3.setText("Login");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 100, 40));

        SignUp.setBackground(new java.awt.Color(153, 153, 153));
        SignUp.setText("Sign Up");
        SignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignUpActionPerformed(evt);
            }
        });
        getContentPane().add(SignUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 430, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("New to Connect ?");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 400, -1, -1));

        Email.setBackground(new java.awt.Color(204, 204, 204));
        Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailActionPerformed(evt);
            }
        });
        getContentPane().add(Email, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 240, 30));

        Password.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, 140, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Password :");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 100, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connecthub/Gradient Background.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 320, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginActionPerformed
    String email = Email.getText().trim().toLowerCase();  // Trim and lowercase the email
    String password = new String(Password.getPassword()).trim();  // Trim the password

       // Check if email is empty
if (email.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please enter your email.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

// Check if password is empty
if (password.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please enter your password.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

        try {
            User loggedInUser = userAccountManagement.login(email, password); // Pass email and password to login
            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + email + "!");
                this.setVisible(false); // Hide login frame
                NewsFeed userFeed = new NewsFeed(loggedInUser);
                
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing your request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
 
    }//GEN-LAST:event_LoginActionPerformed

    private void SignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignUpActionPerformed
        // Open Sign-Up JFrame
        this.setVisible(false); // Hide current JFrame
        SignUpGUI signUpFrame = new SignUpGUI(userAccountManagement,this, cEngine);
        signUpFrame.setVisible(true);
    
    }//GEN-LAST:event_SignUpActionPerformed

    private void EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailActionPerformed
 public static void main(String args[]) {
        // Create instance of UserAccountManagement (backend)
//        UserAccountManagement userAccountManagement = new UserAccountManagement();
        
        // Create and display the Login frame
        java.awt.EventQueue.invokeLater(() -> {
//            new UserAccountManagmentGUI(userAccountManagement).setVisible(true);  // Show the Login frame
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Email;
    private javax.swing.JButton Login;
    private javax.swing.JPasswordField Password;
    private javax.swing.JButton SignUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
