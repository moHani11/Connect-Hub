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
    private final ArrayList<Story> stories;
    private final FileManager fileManager;
    private final ScheduledExecutorService scheduler;

    public StoryManager() {
        this.stories = new ArrayList<>();
        this.fileManager = new FileManager();
        this.scheduler = Executors.newScheduledThreadPool(1);
        loadStoriesFromFile();
        startAutoCleanup();
    }

    public void startAutoCleanup() {
        scheduler.scheduleAtFixedRate(this::deleteExpiredStories, 0, 24, TimeUnit.HOURS);
    }

    public void createStory(String userId, String imagePath) {
        Story newStory = new Story(userId, imagePath, new Date());
        synchronized (stories) {
            stories.add(newStory);
        }
        saveStoriesToFile();
    }

    public ArrayList<Story> getStories() {
        synchronized (stories) {
            return new ArrayList<>(stories);
        }
    }

    public void deleteStory(String userId, Date creationDate) {
        synchronized (stories) {
            stories.removeIf(story ->
                story.getUserId().equals(userId) &&
                story.getCreationDate().equals(creationDate)
            );
        }
        saveStoriesToFile();
    }

    public void deleteExpiredStories() {
        synchronized (stories) {
            stories.removeIf(Story::isExpired);
        }
        saveStoriesToFile();
    }

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
            fileManager.saveStories(storiesJson);
        } catch (Exception e) {
            System.err.println("Error saving stories to file: " + e.getMessage());
        }
    }

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

    private Date parseDate(String dateStr) {
        try {
            return Date.from(java.time.Instant.parse(dateStr));
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr);
            return null;
        }
    }

    public void shutdownScheduler() {
        scheduler.shutdown();
    }

    public void deleteExpiredStoriesManually() {
        deleteExpiredStories();
    }
}