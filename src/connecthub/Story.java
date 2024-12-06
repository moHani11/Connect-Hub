package connecthub;

import java.util.Date;

public class Story {
    private String userId;
    private String imagePath;
    private Date creationDate;

    public Story(String userId, String imagePath, Date creationDate) {
        this.userId = userId;
        this.imagePath = imagePath;
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long storyAge = currentTime - creationDate.getTime();
        return storyAge > 24 * 60 * 60 * 1000L; // القصة تعتبر منتهية إذا تجاوزت 24 ساعة
    }
}