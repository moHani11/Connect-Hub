package connecthub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StoryManager {
    private final ArrayList<Story> stories;
    private final ArrayList<Story> allStories;
    private final FileManager fileManager;
    private String userID;

    public StoryManager(String userID) {
        this.userID = userID;
        this.stories = new ArrayList<>();
        this.allStories = new ArrayList<>();
        this.fileManager = new FileManager();
        loadStoriesFromFile();
        loadAllStoriesFromFile();
    }

    public void createStory(String userId, String imagePath) throws IOException {
        Story newStory = new Story(userId, imagePath, new Date());
        stories.add(newStory);
        allStories.add(newStory);
        saveStoriesToFile();
        NotificationManager notiManager = new NotificationManager();
        String type = "NewStory";
       notiManager.sendNotificationsToFriends(userId, type);
    }

    public ArrayList<Story> getStories() {
        return new ArrayList<>(stories);
    }

    public void deleteStory(String userId, Date creationDate) {
        allStories.removeIf(story -> 
            story.getUserId().equals(userId) && story.getCreationDate().equals(creationDate)
        );
        stories.removeIf(story -> 
            story.getUserId().equals(userId) && story.getCreationDate().equals(creationDate)
        );
        saveStoriesToFile();
    }

    public void deleteExpiredStories() {
        allStories.removeIf(Story::isExpired);
        stories.removeIf(Story::isExpired);
        saveStoriesToFile();
    }

    public ArrayList<Story> getActiveStories() {
        ArrayList<Story> activeStories = new ArrayList<>();
        for (Story story : stories) {
            if (!story.isExpired()) {
                activeStories.add(story);
            }
        }
        return activeStories;
    }

    public ArrayList<Story> getStoriesByUser(String userId) {
        ArrayList<Story> userStories = new ArrayList<>();
        for (Story story : stories) {
            if (story.getUserId().equals(userId) && !story.isExpired()) {
                userStories.add(story);
            }
        }
        return userStories;
    }

    public ArrayList<Story> getStoriesByFriends(List<String> friendsEmails) {
        ArrayList<Story> friendsStories = new ArrayList<>();
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement userAccountManagement = new UserAccountManagement(c);

        for (Story story : allStories) {
            if (friendsEmails.contains(userAccountManagement.getEmailByID(story.getUserId())) 
                && !story.isExpired()) {
                friendsStories.add(story);
            }
        }

        c = null;
        userAccountManagement = null;

        return friendsStories;
    }

    private void saveStoriesToFile() {
        JSONArray storiesJson = new JSONArray();
        for (Story story : allStories) {
            JSONObject storyJson = new JSONObject();
            storyJson.put("imagePath", story.getImagePath());
            storyJson.put("contentId", "Story-" + story.getCreationDate().getTime());
            storyJson.put("authorId", story.getUserId());
            storyJson.put("content", "");
            storyJson.put("timestamp", story.getCreationDate().toInstant().toString());
            storiesJson.add(storyJson);
        }
        try {
            fileManager.saveStories(storiesJson);
        } catch (Exception e) {
            System.err.println("Error saving stories to file: " + e.getMessage());
        }
    }

    private void loadAllStoriesFromFile() {
        try {
            JSONArray storiesJson = fileManager.readStories();
            allStories.clear();
            for (Object obj : storiesJson) {
                JSONObject storyJson = (JSONObject) obj;
                String userId = (String) storyJson.get("authorId");
                String imagePath = (String) storyJson.get("imagePath");
                String timestampStr = (String) storyJson.get("timestamp");
                Date creationDate = parseDate(timestampStr);
                Story story = new Story(userId, imagePath, creationDate);
                allStories.add(story);
            }
        } catch (Exception e) {
            System.err.println("Error loading stories from file: " + e.getMessage());
        }
    }

    private void loadStoriesFromFile() {
        try {
            JSONArray storiesJson = fileManager.readStories();
            stories.clear();
            for (Object obj : storiesJson) {
                JSONObject storyJson = (JSONObject) obj;
                String userId = (String) storyJson.get("authorId");
                String imagePath = (String) storyJson.get("imagePath");
                String timestampStr = (String) storyJson.get("timestamp");
                Date creationDate = parseDate(timestampStr);
                if (userId.equals(this.userID)) {
                    Story story = new Story(userId, imagePath, creationDate);
                    stories.add(story);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading stories from file: " + e.getMessage());
        }
    }

    private Date parseDate(String dateStr) {
        try {
            return Date.from(java.time.Instant.parse(dateStr));
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }

    public void deleteExpiredStoriesManually() {
        deleteExpiredStories();
    }
}
