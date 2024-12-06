package connecthup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class PostManager {
    private ArrayList<Post> posts; // List to store posts
    private FileManager fileManager; // Object to handle file operations

    // Constructor initializes posts list and loads posts from file
    public PostManager() {
        this.posts = new ArrayList<>();
        this.fileManager = new FileManager();
        loadPostsFromFile();
    }

    // Method to create a new post and add it to the list
    public void createPost(String userId, String content, String imagePath) {
        String contentId = "Post-" + new Date().getTime();  // Generate contentId using current time
        Post newPost = new Post(userId, content, imagePath, new Date(), contentId);
        posts.add(newPost);
        savePostsToFile(); // Save new post to file
    }

    // Method to edit an existing post
    public void editPost(int postIndex, String newContent, String newImagePath) {
        if (postIndex >= 0 && postIndex < posts.size()) {
            Post post = posts.get(postIndex);
            post.setContent(newContent); // Update content
            post.setImagePath(newImagePath); // Update image path
            post.setLastModifiedDate(new Date()); // Set last modified date
            savePostsToFile(); // Save changes to file
        }
    }

    // Method to delete a post by index
    public void deletePost(int postIndex) {
        if (postIndex >= 0 && postIndex < posts.size()) {
            posts.remove(postIndex); // Remove post from list
            savePostsToFile(); // Save changes to file
        }
    }

    // Method to get the list of all posts
    public ArrayList<Post> getPosts() {
        return posts;
    }

    // Method to save posts to a file
    public void savePostsToFile() {
        JSONArray postsJson = new JSONArray(); // Create a JSON array to hold posts data
        for (Post post : posts) {
            JSONObject postJson = new JSONObject(); // Create a JSON object for each post
            postJson.put("imagePath", post.getImagePath());
            postJson.put("contentId", post.getContentId());
            postJson.put("authorId", post.getUserId());
            postJson.put("content", post.getContent());
            postJson.put("timestamp", post.getCreationDate().toInstant().toString());  // Store creation timestamp
            if (post.getLastModifiedDate() != null) {
                postJson.put("lastModifiedDate", post.getLastModifiedDate().toInstant().toString()); // Store last modified date
            }
            postsJson.add(postJson);
        }
        try {
            fileManager.savePosts(postsJson); // Save posts JSON to file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load posts from a file
    private void loadPostsFromFile() {
        try {
            JSONArray postsJson = fileManager.readPosts(); // Read posts data from file
            for (Object obj : postsJson) {
                JSONObject postJson = (JSONObject) obj;
                String contentId = (String) postJson.get("contentId");
                String authorId = (String) postJson.get("authorId");
                String content = (String) postJson.get("content");
                String imagePath = (String) postJson.get("imagePath");
                String timestampStr = (String) postJson.get("timestamp");
                Date timestamp = parseDate(timestampStr); // Convert creation date string to Date
                Date lastModifiedDate = null;
                if (postJson.containsKey("lastModifiedDate")) {
                    String lastModifiedDateStr = (String) postJson.get("lastModifiedDate");
                    lastModifiedDate = parseDate(lastModifiedDateStr); // Convert last modified date string to Date
                }
                Post post = new Post(authorId, content, imagePath, timestamp, contentId);
                post.setLastModifiedDate(lastModifiedDate); // Set last modified date
                posts.add(post); // Add post to the list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to parse date string to Date object
    private Date parseDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            Instant instant = Instant.from(formatter.parse(dateStr));
            return Date.from(instant);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(dateStr); // Try alternative date format if the first one fails
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null; // Return null if parsing fails
            }
        }
    }

    // Method to get all posts
    public ArrayList<Post> getAllPosts() {
        return posts;
    }

    // Method to get posts by a specific user
    public ArrayList<Post> getPostsByUser(String userId) {
        ArrayList<Post> userPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getUserId().equals(userId)) {
                userPosts.add(post); // Add post to the list if it belongs to the user
            }
        }
        return userPosts;
    }

    // Method to get posts by friends
    public ArrayList<Post> getPostsByFriends(List<String> friendsIds) {
        ArrayList<Post> friendsPosts = new ArrayList<>();
        for (Post post : posts) {
            if (friendsIds.contains(post.getUserId())) {
                friendsPosts.add(post); // Add post to the list if it is from a friend
            }
        }
        return friendsPosts;
    }
}