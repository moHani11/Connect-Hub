package connecthub;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocialMediaApp {
    private JFrame frame;
    private JTextArea postTextArea;
    private JLabel imageLabel;
    private JPanel postsPanel, storiesPanel;
    private String selectedImagePath = null;
    private JLabel postImageLabel;
    User user  ;
    public SocialMediaApp(User u) {
        
        user = u;
        initializeUI();  // Initialize the user interface
    }

    private void initializeUI() {
        frame = new JFrame("Content Creation Area");
        frame.setSize(800, 600);
        frame.setLocation(400,130);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Posts", initializePostsTab());  // Add Posts tab
        tabbedPane.addTab("Stories", initializeStoriesTab());  // Add Stories tab
        frame.add(tabbedPane, BorderLayout.CENTER);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton feedButton = new JButton("Back to Feed");
    feedButton.setPreferredSize(new Dimension(150, 30)); // Set button size
    feedButton.addActionListener(e -> backToFeed(frame));  // Action for the button
    topPanel.add(feedButton);

    // Add the top panel to the frame
    frame.add(topPanel, BorderLayout.NORTH);
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
        frame.setVisible(true);

    }
    
    public void backToFeed(JFrame frame){
        frame.setVisible(false);
        NewsFeed n = new NewsFeed(user);
    }

    private JPanel initializePostsTab() {
        JPanel postsTab = new JPanel(new BorderLayout());
        JPanel createPostPanel = createPostSection();  // Create section for making posts
        postsTab.add(createPostPanel, BorderLayout.NORTH);
        postsPanel = createDisplayPanel();  // Panel to display posts
        postsTab.add(new JScrollPane(postsPanel), BorderLayout.CENTER);
        refreshPosts();  // Refresh the posts display
        return postsTab;
    }

    private JPanel initializeStoriesTab() {
        JPanel storiesTab = new JPanel(new BorderLayout());
        JPanel createStoryPanel = createStorySection();  // Create section for making stories
        storiesTab.add(createStoryPanel, BorderLayout.NORTH);
        
        JButton refreshButton = new JButton("Refresh Stories");
        refreshButton.addActionListener(e -> refreshStories());  // Refresh the stories when button is clicked
        storiesTab.add(refreshButton, BorderLayout.SOUTH);  // Add refresh button at the bottom
        
        storiesPanel = createDisplayPanel();  // Panel to display stories
        storiesTab.add(new JScrollPane(storiesPanel), BorderLayout.CENTER);
        refreshStories();  // Load stories at the beginning
        return storiesTab;
    }

    private JPanel createPostSection() {
        JPanel panel = new JPanel(new FlowLayout());
        postTextArea = new JTextArea(3, 20);
        postTextArea.setBorder(BorderFactory.createTitledBorder("Write your post:"));
        panel.add(postTextArea);
        
        postImageLabel = new JLabel("No Image Selected");
        postImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        postImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        postImageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(postImageLabel);
        
        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(e -> selectImageForPost());  // Choose an image for the post
        panel.add(chooseImageButton);
        
        addButton(panel, "Create Post", e -> createPost());  // Add button to create the post
        
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JButton editButton = new JButton("Edit");
//            editButton.addActionListener(e -> editPost(post));  // Set edit button action
            editButton.addActionListener(e -> choosePost(true));  // Set edit button action
            
JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> choosePost(false));  // Set delete button action
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            topPanel.add(buttonPanel);
            panel.add(topPanel);
        
        return panel;
    }

    private JPanel createStorySection() {
        JPanel panel = new JPanel(new FlowLayout());
        addImageChooser(panel);  // Add image chooser for story
        addButton(panel, "Create Story", e -> {
            try {
                createStory();
            } catch (IOException ex) {
                Logger.getLogger(SocialMediaApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });  // Button to create a story
        addButton(panel, "Delete Story", e -> deleteSpecificStory());  // Button to delete a specific story
        return panel;
    }

    private void addImageChooser(JPanel panel) {
        JButton button = new JButton("Choose Image");
        button.addActionListener(e -> selectImage());  // Choose image for story
        panel.add(button);
        
        imageLabel = new JLabel("No Image Selected");
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(imageLabel);
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);  // Add the button with an action
        panel.add(button);
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Layout for displaying posts/stories
        return panel;
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImagePath).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
            imageLabel.setText("");
        }
    }

    private void selectImageForPost() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImagePath).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            postImageLabel.setIcon(imageIcon);
            postImageLabel.setText("");
        }
    }

    private void createPost() {
        String content = postTextArea.getText().trim();
        if (content.isEmpty() && selectedImagePath == null) {
            showMessage("Post content cannot be empty or missing an image.");
            return;
        }
        try {
            user.postManager.createPost(user.getUserId(), content, selectedImagePath);  // Create a post
        } catch (IOException ex) {
            Logger.getLogger(SocialMediaApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        postTextArea.setText("");
        selectedImagePath = null;
        postImageLabel.setIcon(null);
        postImageLabel.setText("No Image Selected");
        refreshPosts();  // Refresh the posts display
    }

    private void choosePost(boolean flag){
        
         ArrayList<Post> posts = user.postManager.getPosts();
        if (posts.isEmpty()) {
            showMessage("No posts to delete.");
            return;
        }
        String[] postOptions = new String[posts.size()];
        for (int i = 0; i < posts.size(); i++) {
            postOptions[i] = "Post " + (i + 1) + " - " + formatDate(posts.get(i).getCreationDate());
        }
        String selectedPost = (String) JOptionPane.showInputDialog(
                frame,
                "Select a Post:",
                "Choose Post",
                JOptionPane.PLAIN_MESSAGE,
                null,
                postOptions,
                postOptions[0]
        );
        if (selectedPost != null) {
            for (int i = 0; i < posts.size(); i++) {
                if (selectedPost.contains("Post " + (i + 1))) {
                     Post post = posts.get(i);
                     if (flag)  editPost(post);
                     else deletePost(post);
                     

//                    user.storyManager.deleteStory(storyToDelete.getUserId(), storyToDelete.getCreationDate());
                    break;
                }
            }
//            refreshPosts();  // Refresh the stories display after deletion  --  --------
        }
        
    }
    
    private void editPost(Post post) {
    // Create a panel for editing the post
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        
    // Add a text area for editing the content
    JTextArea textArea = new JTextArea(post.getContent(), 5, 20);
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(textArea);
    editPanel.add(new JLabel("Edit the content:"));
    editPanel.add(scrollPane);

    // Display the current image if available
    JLabel imageLabel = new JLabel();
    if (post.getImagePath() != null) {
        // Show the post's image
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        imageLabel.setIcon(imageIcon);
        editPanel.add(imageLabel);

        // Add a button to change the image
        JButton changeImageButton = new JButton("Change Image");
        
        changeImageButton.addActionListener(e -> {
            selectImageForPost();
            if (selectedImagePath != null) {
                post.setImagePath(selectedImagePath);
                imageLabel.setIcon(new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                post.setLastModifiedDate(new Date());  // Update the modification date
            }
        });
        editPanel.add(changeImageButton);

        // Add a button to remove the image
        JButton deleteImageButton = new JButton("Remove Image");
        deleteImageButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove the image?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                post.setImagePath(null);
                post.setLastModifiedDate(new Date());
                imageLabel.setIcon(null);
                imageLabel.setText("No Image Selected");
            }
        });
        editPanel.add(deleteImageButton);
    } else {
        // Add a button to add an image if none exists
        JButton addImageButton = new JButton("Add Image");
        addImageButton.addActionListener(e -> {
            selectImageForPost();
            if (selectedImagePath != null) {
                post.setImagePath(selectedImagePath);
                imageLabel.setIcon(new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                post.setLastModifiedDate(new Date());  // Update the modification date
            }
        });
        editPanel.add(addImageButton);
    }

    // Display the edit dialog with OK and Cancel options
    int option = JOptionPane.showConfirmDialog(frame, editPanel, "Edit Post", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (option == JOptionPane.OK_OPTION) {
        String newContent = textArea.getText().trim();
        // If the text is empty and the post has no image, confirm deletion
        if (newContent.isEmpty() && post.getImagePath() == null) {
            int confirm = JOptionPane.showConfirmDialog(frame, "If you leave the content empty, the post will be deleted. Do you want to continue?");
            if (confirm == JOptionPane.YES_OPTION) {
                user.postManager.deletePost(user.postManager.getPosts().indexOf(post));
                refreshPosts();
                return;
            } else {
                // Reopen the edit dialog if the user cancels
                editPost(post);
                return;
            }
        }

        // Update the content and modification date if content is changed
        if (!newContent.equals(post.getContent())) {
            post.setContent(newContent);
            post.setLastModifiedDate(new Date());
        }
        
        user.postManager.updateAllPostsFromPosts();
        user.postManager.savePostsToFile();  // Save the updated posts
        selectedImagePath = null;  // Reset the selected image path
        postImageLabel.setIcon(null);  // Clear the image label
        refreshPosts();  // Refresh the posts display
    }
}
    
    private void deletePost(Post post) {
    // Confirm deletion of the post
    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this post?");
    if (confirm == JOptionPane.YES_OPTION) {
        // Delete the post from the manager and refresh the posts display
        user.postManager.deletePost(user.postManager.getPosts().indexOf(post));
        user.postManager.updateAllPostsFromPosts();
        user.postManager.savePostsToFile();  // Save the updated posts
        refreshPosts();
    }
    }
    
    private void createStory() throws IOException {
        if (selectedImagePath == null) {
            showMessage("Please choose an image for the story.");
            return;
        }
        user.storyManager.createStory(user.getUserId(), selectedImagePath);  // Create a story
        selectedImagePath = null;
        imageLabel.setIcon(null);
        imageLabel.setText("No Image Selected");
        refreshStories();  // Refresh the stories display
    }

    private void deleteSpecificStory() {
        ArrayList<Story> stories = user.storyManager.getActiveStories();
        if (stories.isEmpty()) {
            showMessage("No active stories to delete.");
            return;
        }
        String[] storyOptions = new String[stories.size()];
        for (int i = 0; i < stories.size(); i++) {
            storyOptions[i] = "Story " + (i + 1) + " - " + formatDate(stories.get(i).getCreationDate());
        }
        String selectedStory = (String) JOptionPane.showInputDialog(
                frame,
                "Select a story to delete:",
                "Delete Story",
                JOptionPane.PLAIN_MESSAGE,
                null,
                storyOptions,
                storyOptions[0]
        );
        if (selectedStory != null) {
            for (int i = 0; i < stories.size(); i++) {
                if (selectedStory.contains("Story " + (i + 1))) {
                    Story storyToDelete = stories.get(i);
                    user.storyManager.deleteStory(storyToDelete.getUserId(), storyToDelete.getCreationDate());
                    break;
                }
            }
            refreshStories();  // Refresh the stories display after deletion
        }
    }

    private void refreshPosts() {
        postsPanel.removeAll();
        ArrayList<Post> posts = user.postManager.getAllPosts();  // Get all posts
        for (Post post : posts) {
            JPanel panel = createPostCard(post);  // Create card for each post
            postsPanel.add(panel, 0);
        }
        postsPanel.revalidate();
        postsPanel.repaint();  // Revalidate and repaint the panel to show new posts
    }

    private void refreshStories() {
//        user.storyManager.deleteExpiredStories();  // Delete expired stories
        storiesPanel.removeAll();  // Remove all current stories
        ArrayList<Story> stories = user.storyManager.getActiveStories();  // Get active stories
        for (Story story : stories) {
            JPanel storyCard = createStoryCard(story);  // Create card for each story
            storiesPanel.add(storyCard);
        }
        storiesPanel.revalidate();
        storiesPanel.repaint();  // Revalidate and repaint the panel to show new stories
    }

    public static JPanel createStoryCard(Story story) {
        JPanel storyCard = new JPanel();
        storyCard.setLayout(new BoxLayout(storyCard, BoxLayout.Y_AXIS));
        storyCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        storyCard.setPreferredSize(new Dimension(300, 200));
        JLabel contentLabel = new JLabel(">> " + story.getUsername());
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentLabel.setBackground(Color.DARK_GRAY);
        storyCard.add(contentLabel);
        if (story.getImagePath() != null) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(story.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JLabel storyImageLabel = new JLabel(imageIcon);
            storyCard.add(storyImageLabel);
        }
        return storyCard;
    }

    public static JPanel createPostCard(Post post) {
        JPanel postCard = new JPanel();
        postCard.setLayout(new BoxLayout(postCard, BoxLayout.Y_AXIS));
        postCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        postCard.setPreferredSize(new Dimension(300, 200));
            JLabel usernameLabel = new JLabel(">> " + post.getUsername());
    usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    usernameLabel.setBackground(Color.DARK_GRAY);
    usernameLabel.setOpaque(true);
    usernameLabel.setForeground(Color.WHITE);
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
    postCard.add(usernameLabel);
        JLabel contentLabel = new JLabel(post.getContent());
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        postCard.add(contentLabel);
        if (post.getImagePath() != null) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JLabel postImageLabel = new JLabel(imageIcon);
            postCard.add(postImageLabel);
        }
  String dateText = "Created on: " + formatDateTime(post.getCreationDate());
if (post.getLastModifiedDate() != null && !post.getCreationDate().equals(post.getLastModifiedDate())) {
    dateText += " | Last modified on: " + formatDateTime(post.getLastModifiedDate());
}
JLabel dateLabel = new JLabel(dateText);
dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

postCard.add(dateLabel);
        return postCard;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private static String formatDateTime(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    return formatter.format(date);
}
private static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }
    public static void main(String[] args) {
//        new SocialMediaApp();  // Launch the app
    }
}