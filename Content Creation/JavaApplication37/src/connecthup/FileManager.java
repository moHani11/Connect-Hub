package connecthup;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
    private final String postsFilePath = "posts.json"; // Path to posts JSON file
    private final String storiesFilePath = "stories.json"; // Path to stories JSON file

    // Method to read posts from the JSON file
    public JSONArray readPosts() throws Exception {
        return readJsonFile(postsFilePath); // Read and return posts data
    }

    // Method to read stories from the JSON file
    public JSONArray readStories() throws Exception {
        return readJsonFile(storiesFilePath); // Read and return stories data
    }

    // Method to save posts data to the JSON file
    public void savePosts(JSONArray posts) throws Exception {
        saveJsonFile(postsFilePath, posts); // Save posts data to the file
    }

    // Method to save stories data to the JSON file
    public void saveStories(JSONArray stories) throws Exception {
        saveJsonFile(storiesFilePath, stories); // Save stories data to the file
    }

    // Helper method to read JSON data from a file
    private JSONArray readJsonFile(String filePath) throws Exception {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader); // Parse the JSON content
            return (JSONArray) obj; // Return parsed data
        } catch (Exception e) {
            return new JSONArray(); // Return an empty array if error occurs
        }
    }

    // Helper method to save JSON data to a file
    private void saveJsonFile(String filePath, JSONArray data) throws Exception {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toJSONString()); // Write data to file in JSON format
        }
    }
}
