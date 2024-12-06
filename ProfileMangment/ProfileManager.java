package connecthub;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.List;
import java.util.ArrayList;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import java.io.FileWriter;
//import java.io.IOException;
/**
 *
 * @author Asem
 */
public class ProfileManager {

    private String userId = "123";
    private String username = "ِAsem Kilanyِ";
    private String bio = "3antil el magal ";
    private String profilePhotoPath = "profile.jpg";
    private String coverPhotoPath = "cover.jpg";
    private String status = "offline";
    private String dateOfBirth = "2000-01-01";
    private String password = "0000";
    private List<String> posts = new ArrayList<>(); // قائمة بالمنشورات
    private List<String> friends = new ArrayList<>(); // حاجه مؤقته بالاسامي بس

    // badeel lel data base
    public String getProfilePhoto(){
        return profilePhotoPath;
    }

    public String getUsername() {
        return username;
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
    public List<String> getPosts(){
        return posts;
    }
    public List<String> getFriends(){
        return friends;
    }
    public void AddTempData() {
        posts.add("Post 1: Hello world!");
        posts.add("Post 2: Java is awesome!");
        friends.add("Ahmed (Online)");
        friends.add("Sara (Offline)");
        friends.add("Mohamed (Online)");
        friends.add("Laila (Offline)");
    }
}

//public void saveToDatabase() {
//    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    try (FileWriter writer = new FileWriter("profile_data.json")) {
//        writer.write(gson.toJson(this));
//        System.out.println("Profile data saved successfully!");
//    } catch (IOException e) {
//        System.err.println("Error saving profile data: " + e.getMessage());
//    }
//}
