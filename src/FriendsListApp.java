/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FriendsListApp {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Friends List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Panel to hold the list
        JPanel panel = new JPanel(new BorderLayout());

        // Load friends data (hardcoded for testing)
        List<String> friends = loadFriendsForTest();

        // Create a JList
        JList<String> friendsList = new JList<>(friends.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(friendsList);

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add panel to the frame
        frame.add(panel);

        // Show the frame
        frame.setVisible(true);
    }

    private static List<String> loadFriendsForTest() {
        // Hardcoded list of friends for testing
        List<String> friends = new ArrayList<>();
        friends.add("Alice (Age: 25)");
        friends.add("Bob (Age: 30)");
        friends.add("Charlie (Age: 22)");
        friends.add("Diana (Age: 28)");
        friends.add("Edward (Age: 35)");
        return friends;
    }
}
