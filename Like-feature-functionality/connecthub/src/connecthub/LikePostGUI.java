package connecthub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LikePostGUI {
    private PostManager postManager;
    private Post currentPost;
    private JLabel likeCountLabel;
    private JButton likeButton;

    public LikePostGUI(PostManager postManager, Post currentPost) {
        this.postManager = postManager;
        this.currentPost = currentPost;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Create frame
        JFrame frame = new JFrame("Post Interaction");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Post content panel
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        JLabel postContentLabel = new JLabel("Post Content: " + currentPost.getContent());
        postContentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Like count
        likeCountLabel = new JLabel("Likes: " + currentPost.getLikeCount());
        likeCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        postPanel.add(postContentLabel);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(likeCountLabel);

        // Like button
        likeButton = new JButton("Like");
        likeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = "user123"; // Example user ID
                boolean isLiked = postManager.likePost(currentPost.getContentId(), userId);
                updateLikeUI(isLiked);
            }
        });
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(likeButton);

        frame.add(postPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void updateLikeUI(boolean isLiked) {
        // Update like count and button text
        likeCountLabel.setText("Likes: " + currentPost.getLikeCount());

        // Change button color based on like status
        if (isLiked) {
            likeButton.setText("Unlike");
            likeButton.setBackground(Color.RED); // Change to red when liked
        } else {
            likeButton.setText("Like");
            likeButton.setBackground(UIManager.getColor("Button.background")); // Reset to default color
        }
    }

    public static void main(String[] args) {
        // Example setup
        PostManager postManager = new PostManager("user123");
        Post examplePost = new Post("user123", "This is a sample post.", null, new java.util.Date(), "Post-1");
        postManager.getAllPosts().add(examplePost);
        SwingUtilities.invokeLater(() -> new LikePostGUI(postManager, examplePost));
    }
}