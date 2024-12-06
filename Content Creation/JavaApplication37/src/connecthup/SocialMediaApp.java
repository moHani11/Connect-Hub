package connecthup;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SocialMediaApp {
    private JFrame frame;
    private JTextArea postTextArea;
    private JLabel imageLabel;
    private JPanel postsPanel, storiesPanel;
    private String selectedImagePath = null;
    private PostManager postManager;
    private StoryManager storyManager;
    private JLabel postImageLabel;
     UserAccountManagement.User user ;
    public SocialMediaApp() {
        postManager = new PostManager();  // Initialize PostManager for handling posts
        storyManager = new StoryManager();  // Initialize StoryManager for handling stories
        storyManager.startAutoCleanup();  // Start auto cleanup for expired stories
        initializeUI();  // Initialize the user interface
    }

    private void initializeUI() {
        frame = new JFrame("Social Media App");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Posts", initializePostsTab());  // Add Posts tab
        tabbedPane.addTab("Stories", initializeStoriesTab());  // Add Stories tab
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
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
        return panel;
    }

    private JPanel createStorySection() {
        JPanel panel = new JPanel(new FlowLayout());
        addImageChooser(panel);  // Add image chooser for story
        addButton(panel, "Create Story", e -> createStory());  // Button to create a story
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
        postManager.createPost(user.getUserId(), content, selectedImagePath);  // Create a post
        postTextArea.setText("");
        selectedImagePath = null;
        postImageLabel.setIcon(null);
        postImageLabel.setText("No Image Selected");
        refreshPosts();  // Refresh the posts display
    }

    private void createStory() {
        if (selectedImagePath == null) {
            showMessage("Please choose an image for the story.");
            return;
        }
        storyManager.createStory(user.getUserId(), selectedImagePath);  // Create a story
        selectedImagePath = null;
        imageLabel.setIcon(null);
        imageLabel.setText("No Image Selected");
        refreshStories();  // Refresh the stories display
    }

    private void deleteSpecificStory() {
        ArrayList<Story> stories = storyManager.getActiveStories();
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
                    storyManager.deleteStory(storyToDelete.getUserId(), storyToDelete.getCreationDate());
                    break;
                }
            }
            refreshStories();  // Refresh the stories display after deletion
        }
    }

    private void refreshPosts() {
        postsPanel.removeAll();
        ArrayList<Post> posts = postManager.getAllPosts();  // Get all posts
        for (Post post : posts) {
            JPanel panel = createPostCard(post);  // Create card for each post
            postsPanel.add(panel, 0);
        }
        postsPanel.revalidate();
        postsPanel.repaint();  // Revalidate and repaint the panel to show new posts
    }

    private void refreshStories() {
        storyManager.deleteExpiredStories();  // Delete expired stories
        storiesPanel.removeAll();  // Remove all current stories
        ArrayList<Story> stories = storyManager.getActiveStories();  // Get active stories
        for (Story story : stories) {
            JPanel storyCard = createStoryCard(story);  // Create card for each story
            storiesPanel.add(storyCard);
        }
        storiesPanel.revalidate();
        storiesPanel.repaint();  // Revalidate and repaint the panel to show new stories
    }

    private JPanel createStoryCard(Story story) {
        JPanel storyCard = new JPanel();
        storyCard.setLayout(new BoxLayout(storyCard, BoxLayout.Y_AXIS));
        storyCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        storyCard.setPreferredSize(new Dimension(300, 200));
        JLabel contentLabel = new JLabel("Story by " + story.getUserId());
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        storyCard.add(contentLabel);
        if (story.getImagePath() != null) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(story.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JLabel storyImageLabel = new JLabel(imageIcon);
            storyCard.add(storyImageLabel);
        }
        return storyCard;
    }

    private JPanel createPostCard(Post post) {
        JPanel postCard = new JPanel();
        postCard.setLayout(new BoxLayout(postCard, BoxLayout.Y_AXIS));
        postCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        postCard.setPreferredSize(new Dimension(300, 200));
        JLabel contentLabel = new JLabel(post.getContent());
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        postCard.add(contentLabel);
        if (post.getImagePath() != null) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JLabel postImageLabel = new JLabel(imageIcon);
            postCard.add(postImageLabel);
        }
        return postCard;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

    public static void main(String[] args) {
        new SocialMediaApp();  // Launch the app
    }
}