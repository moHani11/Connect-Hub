package connecthub;
import java.util.Date;

public class Post {
    private String userId;
    private String content;
    private String imagePath;
    private String contentId;
    private Date creationDate;
    private Date lastModifiedDate;

    public Post(String userId, String content, String imagePath, Date creationDate, String contentId) {
        this.userId = userId;
        this.content = content;
        this.imagePath = imagePath;
        this.creationDate = creationDate;
        this.contentId = contentId;
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
}