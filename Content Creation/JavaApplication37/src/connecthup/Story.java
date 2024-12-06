package connecthup;

import java.util.Date;

public class Story {
    private String userId;       // The user ID of the person who created the story
    private String imagePath;     // The file path of the image in the story
    private Date creationDate;    // The date and time when the story was created

    // Constructor to initialize the story object
    public Story(String userId, String imagePath, Date creationDate) {
        this.userId = userId;
        this.imagePath = imagePath;
        this.creationDate = creationDate;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Getter for imagePath
    public String getImagePath() {
        return imagePath;
    }

    // Getter for creationDate
    public Date getCreationDate() {
        return creationDate;
    }

    // Method to check if the story is expired (older than 24 hours)
    public boolean isExpired() {
       long currentTime = System.currentTimeMillis();  // Get current time in milliseconds
    long storyAge = currentTime - creationDate.getTime();  // Calculate the age of the story
    
    if (storyAge < 0) {
         
        return false;  // If creation date is in the future, return false
    }
    
       return storyAge > 24 * 60 * 60 * 1000L;
    }
}