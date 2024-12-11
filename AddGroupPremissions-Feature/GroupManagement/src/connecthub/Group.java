package connecthub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class Group {
    private String groupId;
    private String name;
    private String description;
    private String groupPhoto;
    private String primaryAdminId;
    private ArrayList<String> adminIds;
    private ArrayList<String> memberIds;
    private ArrayList<Post> posts;
    private Date creationDate;

    public Group(String groupId, String name, String description, String groupPhoto, String primaryAdminId) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.groupPhoto = groupPhoto;
        this.primaryAdminId = primaryAdminId;
        this.adminIds = new ArrayList<>();
        this.memberIds = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.creationDate = new Date();
        adminIds.add(primaryAdminId); // Primary admin is added to the admin list
    }

    // Getters and Setters
    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public String getPrimaryAdminId() {
        return primaryAdminId;
    }

    public ArrayList<String> getAdminIds() {
        return adminIds;
    }

    public ArrayList<String> getMemberIds() {
        return memberIds;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}