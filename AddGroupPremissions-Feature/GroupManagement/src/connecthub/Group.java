package connecthub;

import java.util.ArrayList;
import java.util.Date;

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

    public void setPrimaryAdminId(String primaryAdminId) {
        this.primaryAdminId = primaryAdminId;
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

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void addMember(String userId) {
        if (!this.memberIds.contains(userId)) {
            this.memberIds.add(userId);
        }
    }

    public void removeMember(String userId) {
        this.memberIds.remove(userId);
        this.adminIds.remove(userId);
    }

    public void promoteToAdmin(String userId) {
        if (!this.adminIds.contains(userId)) {
            this.adminIds.add(userId);
        }
    }

    public void demoteFromAdmin(String userId) {
        if (this.adminIds.contains(userId)) {
            this.adminIds.remove(userId);
        }
    }
    public void addAdmin(String userId) {
        if (!this.adminIds.contains(userId) && !this.primaryAdminId.equals(userId)) {
            this.adminIds.add(userId);
        } else {
            System.err.println("User is already an admin or is the primary admin.");
        }
    }

    // Remove an admin from the group (ensuring primary admin isn't removed)
    public void removeAdmin(String userId) {
        if (this.adminIds.contains(userId) && !this.primaryAdminId.equals(userId)) {
            this.adminIds.remove(userId);
        } else if (this.primaryAdminId.equals(userId)) {
            System.err.println("Cannot remove the primary admin.");
        } else {
            System.err.println("User is not an admin.");
        }
    }

}
