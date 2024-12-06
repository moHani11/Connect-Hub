package connecthup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StoryManager {
    private final ArrayList<Story> stories; // List to store all the stories
    private final FileManager fileManager;  // Responsible for saving and loading stories from a file
    private final ScheduledExecutorService scheduler; // Scheduled task to delete expired stories

    // Constructor initializes story list, file manager, and scheduler
    public StoryManager() {
        this.stories = new ArrayList<>();
        this.fileManager = new FileManager();
        this.scheduler = Executors.newScheduledThreadPool(1);
        loadStoriesFromFile(); // Load stories from file when starting
        startAutoCleanup(); // Start the scheduled cleanup task
    }

    // Starts an automatic cleanup every 24 hours to remove expired stories
    public void startAutoCleanup() {
        scheduler.scheduleAtFixedRate(this::deleteExpiredStories, 0, 24, TimeUnit.HOURS);
    }

    // Create a new story and add it to the list
    public void createStory(String userId, String imagePath) {
        Story newStory = new Story(userId, imagePath, new Date());
        synchronized (stories) {
            stories.add(newStory);
        }
        saveStoriesToFile(); // Save the updated list of stories to file
    }

    // Get all stories
    public ArrayList<Story> getStories() {
        synchronized (stories) {
            return new ArrayList<>(stories);
        }
    }

    // Delete a story based on user ID and creation date
    public void deleteStory(String userId, Date creationDate) {
        synchronized (stories) {
            stories.removeIf(story ->
                story.getUserId().equals(userId) &&
                story.getCreationDate().equals(creationDate)
            );
        }
        saveStoriesToFile(); // Save the updated list of stories to file
    }

    // Delete all expired stories
    public void deleteExpiredStories() {
        synchronized (stories) {
            stories.removeIf(Story::isExpired);
        }
        saveStoriesToFile(); // Save the updated list of stories to file
    }

    // Get all active (non-expired) stories
    public ArrayList<Story> getActiveStories() {
        ArrayList<Story> activeStories = new ArrayList<>();
        synchronized (stories) {
            for (Story story : stories) {
                if (!story.isExpired()) {
                    activeStories.add(story);
                }
            }
        }
        return activeStories;
    }

    // Get all stories from a specific user
    public ArrayList<Story> getStoriesByUser(String userId) {
        ArrayList<Story> userStories = new ArrayList<>();
        synchronized (stories) {
            for (Story story : stories) {
                if (story.getUserId().equals(userId) && !story.isExpired()) {
                    userStories.add(story);
                }
            }
        }
        return userStories;
    }

    // Get all stories from friends (users in the friends list)
    public ArrayList<Story> getStoriesByFriends(List<String> friendsIds) {
        ArrayList<Story> friendsStories = new ArrayList<>();
        synchronized (stories) {
            for (Story story : stories) {
                if (friendsIds.contains(story.getUserId()) && !story.isExpired()) {
                    friendsStories.add(story);
                }
            }
        }
        return friendsStories;
    }

    // Save all stories to file
    private void saveStoriesToFile() {
        JSONArray storiesJson = new JSONArray();
        synchronized (stories) {
            for (Story story : stories) {
                JSONObject storyJson = new JSONObject();
                storyJson.put("imagePath", story.getImagePath());
                storyJson.put("contentId", "Story-" + story.getCreationDate().getTime());
                storyJson.put("authorId", story.getUserId());
                storyJson.put("content", "");
                storyJson.put("timestamp", story.getCreationDate().toInstant().toString());
                storiesJson.add(storyJson);
            }
        }
        try {
            fileManager.saveStories(storiesJson); // Save the JSON data to the file
        } catch (Exception e) {
            System.err.println("Error saving stories to file: " + e.getMessage());
        }
    }

    // Load all stories from the file
    private void loadStoriesFromFile() {
        try {
            JSONArray storiesJson = fileManager.readStories();
            synchronized (stories) {
                stories.clear();
                for (Object obj : storiesJson) {
                    JSONObject storyJson = (JSONObject) obj;
                    String userId = (String) storyJson.get("authorId");
                    String imagePath = (String) storyJson.get("imagePath");
                    String timestampStr = (String) storyJson.get("timestamp");
                    Date creationDate = parseDate(timestampStr);
                    Story story = new Story(userId, imagePath, creationDate);
                    stories.add(story);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading stories from file: " + e.getMessage());
        }
    }

    // Parse a date string to a Date object
    private Date parseDate(String dateStr) {
        try {
            return Date.from(java.time.Instant.parse(dateStr));
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }

    // Shutdown the scheduler when no longer needed
    public void shutdownScheduler() {
        scheduler.shutdown();
    }

    // Manually delete expired stories
    public void deleteExpiredStoriesManually() {
        deleteExpiredStories();
    }
}