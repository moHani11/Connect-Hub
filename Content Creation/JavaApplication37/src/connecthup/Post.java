package connecthup;

import java.util.Date;

public class Post {
    private String userId; // ID of the user who created the post
    private String content; // Content of the post
    private String imagePath; // Path to the image associated with the post
    private String contentId; // Unique identifier for the post
    private Date creationDate; // Date the post was created
    private Date lastModifiedDate; // Date the post was last modified

    // Constructor to initialize a new post
    public Post(String userId, String content, String imagePath, Date creationDate, String contentId) {
        this.userId = userId;
        this.content = content;
        this.imagePath = imagePath;
        this.creationDate = creationDate;
        this.contentId = contentId;
    }

    // Getter and setter methods for each field
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