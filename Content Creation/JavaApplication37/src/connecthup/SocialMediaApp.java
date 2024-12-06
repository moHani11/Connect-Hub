package connecthup;

import connecthup.Post;
import connecthup.PostManager;
import connecthup.Story;
import connecthup.StoryManager;
import connecthup.UserAccountManagement;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private UserAccountManagement.User user;

    public SocialMediaApp() {
        postManager = new PostManager(); // Initialize post manager
        storyManager = new StoryManager(); // Initialize story manager
//        user = new UserAccountManagement.User("user1", "user1@example.com", "user11", "mazen123", "1990-01-01");
        initializeUI(); // Initialize user interface
    }

    private void initializeUI() {
        frame = new JFrame("Social Media App");
        frame.setSize(800, 600); // Set frame dimensions
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on exit
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane(); // Create tabbed layout
        tabbedPane.addTab("Posts", initializePostsTab()); // Add "Posts" tab
        tabbedPane.addTab("Stories", initializeStoriesTab()); // Add "Stories" tab

        frame.add(tabbedPane, BorderLayout.CENTER); // Add tabbed pane to frame
        frame.setVisible(true); // Display frame
    }

    private JPanel initializePostsTab() {
        JPanel postsTab = new JPanel(new BorderLayout());
        JPanel createPostPanel = createPostSection(); // Create section for creating posts
        postsTab.add(createPostPanel, BorderLayout.NORTH);

        postsPanel = createDisplayPanel(); // Create panel to display posts
        postsTab.add(new JScrollPane(postsPanel), BorderLayout.CENTER);

        refreshPosts(); // Refresh posts to display initial content
        return postsTab;
    }

    private JPanel initializeStoriesTab() {
        JPanel storiesTab = new JPanel(new BorderLayout());
        JPanel createStoryPanel = createStorySection(); // Section to create stories
        storiesTab.add(createStoryPanel, BorderLayout.NORTH);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshStories()); // Refresh stories when button clicked
        storiesTab.add(refreshButton, BorderLayout.SOUTH);

        storiesPanel = createDisplayPanel(); // Panel to display stories
        storiesTab.add(new JScrollPane(storiesPanel), BorderLayout.CENTER);

        refreshStories(); // Display initial stories
        return storiesTab;
    }

    private JPanel createPostSection() {
        JPanel panel = new JPanel(new FlowLayout());
        postTextArea = new JTextArea(3, 20); // Text area for writing posts
        postTextArea.setBorder(BorderFactory.createTitledBorder("Write your post:"));
        panel.add(postTextArea);

        postImageLabel = new JLabel("No Image Selected"); // Placeholder for image
        postImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        postImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        postImageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(postImageLabel);

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(e -> selectImageForPost()); // Open file chooser for image
        panel.add(chooseImageButton);

        addButton(panel, "Create Post", e -> createPost()); // Button to create post
        return panel;
    }

    private JPanel createStorySection() {
        JPanel panel = new JPanel(new FlowLayout());
        addImageChooser(panel); // Add image chooser
        addButton(panel, "Create Story", e -> createStory()); // Button to create story
        addButton(panel, "Delete Story", e -> deleteSpecificStory()); // Button to delete a story
        return panel;
    }

    private void addImageChooser(JPanel panel) {
        JButton button = new JButton("Choose Image");
        button.addActionListener(e -> selectImage()); // Open file chooser for story
        panel.add(button);

        imageLabel = new JLabel("No Image Selected"); // Placeholder for story image
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(imageLabel);
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text); // Create button with specified action
        button.addActionListener(action);
        panel.add(button);
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout for items
        return panel;
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame); // Open file chooser
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Store selected image path
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImagePath).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon); // Display selected image
            imageLabel.setText("");
        }
    }

    private void selectImageForPost() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame); // Open file chooser for posts
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Store selected image path
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImagePath).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            postImageLabel.setIcon(imageIcon); // Display selected image
            postImageLabel.setText("");
        }
    }

    private void createPost() {
        String content = postTextArea.getText().trim();
        if (content.isEmpty() && selectedImagePath == null) { // Validate input
            showMessage("Post content cannot be empty or missing an image.");
            return;
        }
        postManager.createPost("Mazen123", content, selectedImagePath); // Create post
        postTextArea.setText("");
        selectedImagePath = null;
        postImageLabel.setIcon(null);
        postImageLabel.setText("No Image Selected");
        refreshPosts(); // Update posts display
    }

    private void createStory() {
        if (selectedImagePath == null) { // Ensure image is selected
            showMessage("Please choose an image for the story.");
            return;
        }
        storyManager.createStory("Mazen123", selectedImagePath); // Create story
        selectedImagePath = null;
        imageLabel.setIcon(null);
        imageLabel.setText("No Image Selected");
        refreshStories(); // Update stories display
    }

 


private void deleteSpecificStory() {
    // Retrieve the list of stories from the story manager
    ArrayList<Story> stories = storyManager.getStories();
    // Create a list of options based on stories and their creation dates
    String[] storyOptions = new String[stories.size()];
    for (int i = 0; i < stories.size(); i++) {
        storyOptions[i] = "Story " + (i + 1) + " - " + formatDate(stories.get(i).getCreationDate());
    }
    // Display a dropdown for the user to select a story to delete
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
        // Find the selected story and delete it
        for (int i = 0; i < stories.size(); i++) {
            if (selectedStory.contains("Story " + (i + 1))) {
                Story storyToDelete = stories.get(i);
                System.out.println("Deleting story " + (i + 1));
                storyManager.deleteStory(storyToDelete.getUserId(), storyToDelete.getCreationDate());
                break;
            }
        }
        refreshStories();  // Refresh the stories display after deletion
    }
}

private void refreshPosts() {
    postsPanel.removeAll();  // Remove all current posts from the panel
    // Retrieve the list of posts from the post manager
    ArrayList<Post> posts = postManager.getPosts();
    for (Post post : posts) {
        JPanel panel = createPostCard(post);  // Create a card for each post
        postsPanel.add(panel, 0);  // Add the new post card to the panel at the beginning
    }
    postsPanel.revalidate();  // Revalidate the layout
    postsPanel.repaint();     // Redraw the panel
}


private void refreshStories() {
    storyManager.deleteExpiredStories();  // Delete any expired stories
    storiesPanel.removeAll();  // Remove all current stories from the panel
    
    // Retrieve the updated list of stories
    ArrayList<Story> stories = storyManager.getStories();  
    for (Story story : stories) {
        // Create a card for each story
        JPanel storyCard = new JPanel();
        storyCard.setLayout(new BoxLayout(storyCard, BoxLayout.Y_AXIS));
        storyCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        storyCard.setPreferredSize(new Dimension(300, 200));
        JLabel contentLabel = new JLabel("Story by " + story.getUserId());
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        storyCard.add(contentLabel);
        
        // Add the story image if available
        if (story.getImagePath() != null) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(story.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setPreferredSize(new Dimension(150, 150));
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            storyCard.add(imageLabel);
        }
        
        // Add the story creation date
        String dateText = "Created on: " + formatDate(story.getCreationDate());
        JLabel dateLabel = new JLabel(dateText);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        storyCard.add(dateLabel);
        
        storiesPanel.add(storyCard);  // Add the story card to the panel
    }
    storiesPanel.revalidate();  // Revalidate the layout
    storiesPanel.repaint();     // Redraw the panel
}
private JPanel createPostCard(Post post) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    card.setPreferredSize(new Dimension(300, 200));
    // Set up the content of the post
    JLabel contentLabel = new JLabel("<html>" + post.getContent() + "</html>");
    contentLabel.setVerticalAlignment(SwingConstants.TOP);
    contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    if (isArabic(post.getContent())) {
        contentLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    } else {
        contentLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }
    card.add(contentLabel);
    // Add the image if available
    if (post.getImagePath() != null) {
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(imageLabel);
    }
    // Add buttons for editing and deleting the post
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JButton editButton = new JButton("Edit");
    editButton.addActionListener(e -> editPost(post));  // Set edit button action
    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(e -> deletePost(post));  // Set delete button action
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    topPanel.add(buttonPanel);
    card.add(topPanel);
    // Add the creation and modification dates
    String dateText = "Created on: " + formatDate(post.getCreationDate());
    if (post.getLastModifiedDate() != null && !post.getCreationDate().equals(post.getLastModifiedDate())) {
        dateText += " | Last modified on: " + formatDate(post.getLastModifiedDate());
    }
    JLabel dateLabel = new JLabel(dateText);
    dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    if (isArabic(post.getContent())) {
        dateLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    } else {
        dateLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }
    card.add(dateLabel);
    return card;
}
private boolean isArabic(String text) {
    // Check if the given text contains Arabic characters
    return text.matches("[\u0600-\u06FF]+");
}

private String formatDate(Date date) {
    // Format the given date into "yyyy-MM-dd HH:mm:ss"
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
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
                postManager.deletePost(postManager.getPosts().indexOf(post));
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
        postManager.savePostsToFile();  // Save the updated posts
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
        postManager.deletePost(postManager.getPosts().indexOf(post));
        refreshPosts();
    }
}
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SocialMediaApp::new);
        
    }
}