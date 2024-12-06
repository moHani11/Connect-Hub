package connecthup;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
    private final String postsFilePath = "posts.json";
    private final String storiesFilePath = "stories.json";

    public JSONArray readPosts() throws Exception {
        return readJsonFile(postsFilePath);
    }

    public JSONArray readStories() throws Exception {
        return readJsonFile(storiesFilePath);
    }

    public void savePosts(JSONArray posts) throws Exception {
        saveJsonFile(postsFilePath, posts);
    }

    public void saveStories(JSONArray stories) throws Exception {
        saveJsonFile(storiesFilePath, stories);
    }

    private JSONArray readJsonFile(String filePath) throws Exception {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (Exception e) {
            return new JSONArray(); 
        }
    }

    private void saveJsonFile(String filePath, JSONArray data) throws Exception {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toJSONString());
        }
    }
}