


package connecthub;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.List;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonDataManger {


    public static void main(String[] args) {
        // Create a User instance

    }

    
    
    public static void writeUsers(List<User> users, String filename) {
        // Convert List of Users to JSONArray
        JSONArray usersArray = new JSONArray();
        for (User user : users) {
            usersArray.add(user.toJson());
        }

        // Write JSONArray to file
        try (FileWriter file = new FileWriter(filename)) {
            file.write(usersArray.toJSONString());
            file.flush();
            System.out.println("Users data written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    
     public static List<User> loadUsersFromJsonFile(String filename) {
        List<User> users = new ArrayList<>();

        // Read JSON file and parse into List<User>
        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            JSONArray usersArray = (JSONArray) parser.parse(reader);

            // Convert each JSONObject in the array to a User object
            for (Object obj : usersArray) {
                JSONObject userJson = (JSONObject) obj;
                users.add(User.fromJson(userJson));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return users;
    }

}
