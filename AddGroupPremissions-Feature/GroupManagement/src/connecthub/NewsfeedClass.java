/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class NewsfeedClass {
    
    public NewsfeedClass(){
        
        JFrame newsFeedframe = new JFrame("News Feed");
        newsFeedframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newsFeedframe.setSize(700, 700);
        newsFeedframe.setLayout(new BorderLayout());
        
        JButton ContentCreationButton = new JButton("Create Content");
        int buttonWidth = 200;
        int buttonHeight = 30;
        int frameWidth = newsFeedframe.getWidth();
        ContentCreationButton.setBounds(0  + 20, 20, buttonWidth, buttonHeight);
        
        JComboBox<String> comboBox1 = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
        JComboBox<String> comboBox2 = new JComboBox<>(new String[]{"Choice A", "Choice B", "Choice C"});

        // Set size and initial positions for the combo boxes
        int comboWidth = 120;
        int comboHeight = 25;
        comboBox1.setBounds(newsFeedframe.getWidth() - comboWidth - 20, 20, comboWidth, comboHeight);
        comboBox2.setBounds(newsFeedframe.getWidth() - comboWidth - 20, 60, comboWidth, comboHeight);

        newsFeedframe.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int newFrameWidth = newsFeedframe.getWidth();
                ContentCreationButton.setBounds(0  + 20, 20, buttonWidth, buttonHeight);
                comboBox1.setBounds(newFrameWidth - comboWidth - 20, 20, comboWidth, comboHeight);
                comboBox2.setBounds(newFrameWidth - comboWidth - 20, 60, comboWidth, comboHeight);
            }
        });
        
        newsFeedframe.add(comboBox1);
        newsFeedframe.add(comboBox2);
        newsFeedframe.add(ContentCreationButton);
        
        newsFeedframe.setVisible(true);
        
    }
    
}
