/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


public class Message {

    private String data;
    private String userID;
    private String dateOfCreation;
    
    public Message(String userID,String data){
        this.userID = userID;
        this.data = data;
        this.dateOfCreation = LocalDate.now().toString();
    }
    
    public String getData(){
        return this.data;
    }
    
    public String getDateOfCreation(){
        return this.dateOfCreation;
    }
    
    public String getUserID(){
        return this.userID;
    }
    
    public JPanel toJPanel() {
        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Top section for userID and date
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE);

        JLabel userIDLabel = new JLabel("User: " + this.userID, SwingConstants.LEFT);
        userIDLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel dateLabel = new JLabel("Date: " + this.dateOfCreation, SwingConstants.LEFT);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        topPanel.add(userIDLabel);
        topPanel.add(dateLabel);

        // Center section for the message data
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);

        JLabel messageLabel = new JLabel(this.data, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        messageLabel.setBorder(border);

        centerPanel.add(messageLabel);

        // Add sections to the main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }
}
