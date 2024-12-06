package connecthub;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import connecthub.Post;
import connecthub.SocialMediaApp;
import connecthub.User;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;
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
    private List<JPanel> posts = new ArrayList<>(); // قائمة بالمنشورات
    private List<String> friends = new ArrayList<>(); // حاجه مؤقته بالاسامي بس
    public User user;
    
    // badeel lel data base
public ProfileManager(User u) {
         
        this.user = u;
                
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.bio = "Add a Bio";
        this.status = " online ";
        this.dateOfBirth = user.dateOfBirth;
        this.password = user.dateOfBirth;
        this.addPosts();
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
