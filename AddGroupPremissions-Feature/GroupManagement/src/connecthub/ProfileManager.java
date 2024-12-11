package connecthub;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.swing.JPanel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import java.io.FileWriter;
//import java.io.IOException;
/**
 *
 * @author Asem
 */
public class ProfileManager {

    
    private String userId;
    private String username;
    private String bio = "Add a Bio";
    private String profilePhotoPath = "profile.jpg";
    private String coverPhotoPath = "cover.jpg";
    private String status;
    private String dateOfBirth;
    private String password;
    private List<JPanel> posts = new ArrayList<>(); // قائمة بالمنشورات
    private List<String> friends = new ArrayList<>(); // حاجه مؤقته بالاسامي بس
    public User user;
    
    // badeel lel data base
public ProfileManager(User u) {
         
        this.user = u;
                
        this.userId = user.getUserId();
        this.username = "Username: " + user.getUsername();
        this.status = user.getstatus();
        this.dateOfBirth = user.dateOfBirth;
        this.password = user.dateOfBirth;
        this.addPosts();
        this.loadProfile(JsonDataManger.PROFILES_FILE_NAME);
//        this.posts = posts != null ? posts : new ArrayList<>();  // To handle null list input
//        this.friends = friends != null ? friends : new ArrayList<>();  // To handle null list input
    }
    
    public void addPosts(){
        ArrayList<Post> postsArray = new ArrayList<>();
        
        postsArray.addAll(user.postManager.getAllPosts());
        
        this.posts = new ArrayList<>();

        for (Post post : postsArray) {
            JPanel panel = SocialMediaApp.createPostCard(post);  // Create card for each post
            this.posts.add(panel);
            System.out.println(postsArray.size());
        }
    }
    
    
    public String getProfilePhoto(){
        return profilePhotoPath;
    }

    public String getUsername() {
        return username;
    }
    public void updateUsername(String newUsername){
        this.username= newUsername;
    }

    public String getBio() {
        return bio;
    }
    public String getCoverPhoto(){
        return coverPhotoPath;
    }
    public void updateBio(String newbio){
        this.bio=newbio;
    }
    public void updateprofilephoto (String newprofilePhotoPath){
        this.profilePhotoPath =newprofilePhotoPath;
    }
    public void updateCoverPhoto(String newcoverPhotoPath){
        this.coverPhotoPath =newcoverPhotoPath;
    }
    public void updatePassword(String newpassword){
        this.password =newpassword;
    }
    public List<JPanel> getPosts(){
        return posts;
    }
    public List<String> getFriends(){
        return friends;
    }
    public void AddTempData() {
//        posts.add("Post 1: Hello world!");
//        posts.add("Post 2: Java is awesome!");
        friends.add("Ahmed (Online)");
        friends.add("Sara (Offline)");
        friends.add("Mohamed (Online)");
        friends.add("Laila (Offline)");
    }

    public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.put("userId", user.getUserId());
            jsonObject.put("coverPath", coverPhotoPath);
            jsonObject.put("profilePath", profilePhotoPath);
            jsonObject.put("Bio", bio);
    
            return jsonObject;
        }
    
    public void loadProfile(String filename){
        
        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            JSONArray profilesArray = (JSONArray) parser.parse(reader);

            
            for (Object obj : profilesArray) {
                JSONObject userJson = (JSONObject) obj;
                Map<String, String> map = ProfileManager.fromJson(userJson);
                String ID = map.get("userId");
                if (ID.equals(user.getUserId())){
                    
                    this.bio = map.get("Bio");
                    this.coverPhotoPath = map.get("coverPath");
                    this.profilePhotoPath = map.get("profilePath");
                    break;
                }
            }
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public static Map<String, String> fromJson(JSONObject jsonObject) {

            Map<String, String> userMap = new HashMap<>();
            userMap.put("userId", (String) jsonObject.get("userId"));
            userMap.put("coverPath", (String) jsonObject.get("coverPath"));
            userMap.put("profilePath", (String) jsonObject.get("profilePath"));
            userMap.put("Bio", (String) jsonObject.get("Bio"));

            return userMap;
        }
    
    public void saveToFile(String filename) {
        JSONParser parser = new JSONParser();
        JSONArray profilesArray = new JSONArray();

        try {
            // Load existing data from file
            FileReader reader = new FileReader(filename);
            Object obj = parser.parse(reader);
            profilesArray = (JSONArray) obj; // Assumes the file contains a JSON array
            reader.close();
        } catch (IOException | ParseException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }

        // Update the bio for the given userId
        boolean found = false;
        for (Object obj : profilesArray) {
            JSONObject jsonObject = (JSONObject) obj;
            if (userId.equals(jsonObject.get("userId"))) {
            jsonObject.put("coverPath", coverPhotoPath);
            jsonObject.put("profilePath", profilePhotoPath);
            jsonObject.put("Bio", bio);
            found = true;
             break;
            }
        }

        if (!found) {
            JSONObject j = new JSONObject();
            j.put("userId", userId);
            j.put("coverPath", coverPhotoPath);
            j.put("profilePath", profilePhotoPath);
            j.put("Bio", bio);

            profilesArray.add(j);
            System.out.println("User with ID " + userId + " not found.");

        }

        // Write updated data back to file
        try (FileWriter file = new FileWriter(filename)) {
            file.write(profilesArray.toJSONString());
            file.flush();
            System.out.println("Database updated successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

}

