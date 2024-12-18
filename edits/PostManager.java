package connecthub;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.swing.JLabel;

public class PostManager {

    private static ArrayList<Post> allPosts;
    private ArrayList<Post> posts;
    private static FileManager fileManager;
    private String userID;
    private NotificationManager notiManager ;
    public PostManager(String userID) {
        this.userID = userID;
        this.posts = new ArrayList<>();
        this.allPosts = new ArrayList<>();
        this.fileManager = new FileManager();
        loadPostsFromFile();
        loadAllPostsFromFile();
//        System.out.println(posts.size());
    }

    public void createPost(String userId, String content, String imagePath) throws IOException {
        String contentId = "Post-" + new Date().getTime();  // استخدام الوقت الحالي كـ contentId
        Post newPost = new Post(userId, content, imagePath, new Date(), contentId);
        posts.add(newPost);
        allPosts.add(newPost);
        savePostsToFile();
        // ارسال اشعار للاصدقاء
        notiManager = new NotificationManager();
        String type = "NewPost";
       notiManager.sendNotificationsToFriends(userId, type);
    }

    public void editPost(int postIndex, String newContent, String newImagePath) {
        if (postIndex >= 0 && postIndex < posts.size()) {
            Post post = posts.get(postIndex);
            post.setContent(newContent);
            post.setImagePath(newImagePath);
            post.setLastModifiedDate(new Date());
            savePostsToFile();
        }
    }

    public void deletePost(int postIndex) {
        if (postIndex >= 0 && postIndex < posts.size()) {
            Post deletedPost = posts.get(postIndex);
            System.out.println(deletedPost.getContentId());
            for (Post post : allPosts) {
                if (post.getContentId().equals(deletedPost.getContentId())) {
                    allPosts.remove(post);
                    break;
                }
            }
            posts.remove(postIndex);
            savePostsToFile();
        }
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void updateAllPostsFromPosts() {
        if (allPosts == null || posts == null) {
            return; // Safety check: Ensure lists are not null
        }

        for (Post postInPosts : posts) {
            // Find the corresponding post in allPosts by ID
            for (int i = 0; i < allPosts.size(); i++) {
                Post postInAllPosts = allPosts.get(i);

                if (postInAllPosts.getContentId().equals(postInPosts.getContentId())) {
                    // Update the post in allPosts
                    allPosts.set(i, postInPosts);
//                break; // Exit inner loop once the match is found and updated
                }
            }
        }
    }

    public static void savePostsToFile() {
        JSONArray postsJson = new JSONArray();
        for (Post post : allPosts) {
            JSONObject postJson = new JSONObject();
            postJson.put("imagePath", post.getImagePath());
            postJson.put("contentId", post.getContentId());
            postJson.put("authorId", post.getUserId());
            postJson.put("content", post.getContent());
            postJson.put("timestamp", post.getCreationDate().toInstant().toString());  // تاريخ الإنشاء
            if (post.getLastModifiedDate() != null) {
                postJson.put("lastModifiedDate", post.getLastModifiedDate().toInstant().toString()); // تاريخ التعديل
            }
            postsJson.add(postJson);
        }
        try {
            fileManager.savePosts(postsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadAllPostsFromFile() {
        try {
            JSONArray postsJson = fileManager.readPosts();
            for (Object obj : postsJson) {
                JSONObject postJson = (JSONObject) obj;
                String contentId = (String) postJson.get("contentId");
                String authorId = (String) postJson.get("authorId");
                String content = (String) postJson.get("content");
                String imagePath = (String) postJson.get("imagePath");
                String timestampStr = (String) postJson.get("timestamp");
                Date timestamp = parseDate(timestampStr); // تحويل تاريخ الإنشاء
                Date lastModifiedDate = null;
                if (postJson.containsKey("lastModifiedDate")) {
                    String lastModifiedDateStr = (String) postJson.get("lastModifiedDate");
                    lastModifiedDate = parseDate(lastModifiedDateStr); // تحويل تاريخ التعديل
                }
                Post post = new Post(authorId, content, imagePath, timestamp, contentId);
                post.setLastModifiedDate(lastModifiedDate); // تعيين تاريخ التعديل
                allPosts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    private void loadPostsFromFile() {
        try {
            JSONArray postsJson = fileManager.readPosts();
            for (Object obj : postsJson) {
                JSONObject postJson = (JSONObject) obj;
                String contentId = (String) postJson.get("contentId");
                String authorId = (String) postJson.get("authorId");
                String content = (String) postJson.get("content");
                String imagePath = (String) postJson.get("imagePath");
                String timestampStr = (String) postJson.get("timestamp");
                Date timestamp = parseDate(timestampStr); // تحويل تاريخ الإنشاء
                Date lastModifiedDate = null;
                if (postJson.containsKey("lastModifiedDate")) {
                    String lastModifiedDateStr = (String) postJson.get("lastModifiedDate");
                    lastModifiedDate = parseDate(lastModifiedDateStr); // تحويل تاريخ التعديل
                }
                if (authorId.equals(userID)) {
                    Post post = new Post(authorId, content, imagePath, timestamp, contentId);
                    post.setLastModifiedDate(lastModifiedDate); // تعيين تاريخ التعديل
                    posts.add(post);
//                System.out.println("Found files" + posts.size());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    private static Date parseDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            Instant instant = Instant.from(formatter.parse(dateStr));
            return Date.from(instant);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(dateStr); // التعامل مع التنسيق البديل في حالة فشل التنسيق الأول
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null; // إرجاع null إذا فشلت عملية التحويل
            }
        }
    }

    public ArrayList<Post> getAllPosts() {
//    System.out.println(posts.size() + "--");
        return posts;
    }

    public ArrayList<Post> getPostsByUser(String userId) {
        ArrayList<Post> userPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getUserId().equals(userId)) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }

    public ArrayList<Post> getPostsByFriends(List<String> friendsEmails) {
        ArrayList<Post> friendsPosts = new ArrayList<>();
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement userAccountManagement = new UserAccountManagement(c);

        for (Post post : allPosts) {
            if (friendsEmails.contains(userAccountManagement.getEmailByID(post.getUserId()))) {
                friendsPosts.add(post);
            }
        }

        c = null;
        userAccountManagement = null;

        return friendsPosts;

    }

}
