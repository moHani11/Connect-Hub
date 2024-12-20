package connecthub;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Post {
    private String userId;
    private String content;
    private String imagePath;
    private String contentId;
    private Date creationDate;
    private Date lastModifiedDate;
     private int likeCount; // Track the total number of likes
    private Set<String> likedBy; // Track user IDs of those who liked the post

    public Post(String userId, String content, String imagePath, Date creationDate, String contentId) {
        this.userId = userId;
        this.content = content;
        this.imagePath = imagePath;
        this.creationDate = creationDate;
        this.contentId = contentId;
        this.likeCount = 0; // Initialize like count
        this.likedBy = new HashSet<>(); // Initialize likedBy set
    }

    public Post(String userId, String content, String imagePath, String contentId, Date creationDate, int likeCount) {
        this.userId = userId;
        this.content = content;
        this.imagePath = imagePath;
        this.contentId = contentId;
        this.creationDate = creationDate;
        this.likeCount = likeCount;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getContentId() {
        return contentId;
    }
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
      public String getUsername(){
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement u = new UserAccountManagement(c);
        return u.getUsernameByID(this.userId);
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Set<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<String> likedBy) {
        this.likedBy = likedBy;
    }
    
    // Method to update the image of the post
    public void updateImage(String newImagePath) {
        this.imagePath = newImagePath;
    }

    // Method to remove the image from the post
    public void removeImage() {
        this.imagePath = null;
    }
    
    
    
    // Toggle Like
    public boolean toggleLike(String userId) {
        if (likedBy.contains(userId)) {
            likedBy.remove(userId);
            likeCount--;
            return false; // Indicates like removed
        } else {
            likedBy.add(userId);
            likeCount++;
            return true; // Indicates like added
        }
    }
}