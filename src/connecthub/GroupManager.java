package connecthub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class GroupManager {
    private static ArrayList<Group> allGroups = new ArrayList<>();
    private Map<String, Group> userGroups; // Maps userId to groups they belong to
    private static final String FILE_NAME = "groups.json";

    public GroupManager() {
        userGroups = new HashMap<>();
        loadGroupsFromFile();
    }

    public Group createGroup(String name, String description, String groupPhoto, String primaryAdminId) {
        String groupId = "Group-" + System.currentTimeMillis();
        Group newGroup = new Group(groupId, name, description, groupPhoto, primaryAdminId);
        allGroups.add(newGroup);
        userGroups.put(primaryAdminId, newGroup);
        saveGroupsToFile();
        return newGroup;
    }

    public void addMember(String groupId, String userId) {
        Group group = getGroupById(groupId);
        if (group != null && !group.getMemberIds().contains(userId)) {
            group.getMemberIds().add(userId);
            saveGroupsToFile();
        }
    }

    public void removeMember(String groupId, String userId) {
        Group group = getGroupById(groupId);
        if (group != null) {
            group.getMemberIds().remove(userId);
            group.getAdminIds().remove(userId);
            saveGroupsToFile();
        }
    }

    public void promoteToAdmin(String groupId, String userId) {
        Group group = getGroupById(groupId);
        if (group != null && group.getMemberIds().contains(userId) && !group.getAdminIds().contains(userId)) {
            group.getAdminIds().add(userId);
            saveGroupsToFile();
        }
    }

    public void demoteAdmin(String groupId, String userId) {
        Group group = getGroupById(groupId);
        if (group != null && group.getAdminIds().contains(userId) && !group.getPrimaryAdminId().equals(userId)) {
            group.getAdminIds().remove(userId);
            saveGroupsToFile();
        }
    }

    public void deleteGroup(String groupId, String userId) {
        Group group = getGroupById(groupId);
        if (group != null && group.getPrimaryAdminId().equals(userId)) {
            allGroups.remove(group);
            userGroups.remove(userId);
            saveGroupsToFile();
        }
    }

    public static ArrayList<Group> getAllGroups() {
        return allGroups;
    }

    public ArrayList<Group> getUserGroups(String userId) {
        ArrayList<Group> groups = new ArrayList<>();
        for (Group group : allGroups) {
            if (group.getMemberIds().contains(userId) || group.getPrimaryAdminId().equals(userId)) {
                groups.add(group);
            }
        }
        return groups;
    }

    public ArrayList<Post> getAllPosts(String userId) {
        ArrayList<Post> allPosts = new ArrayList<>();

        // Add posts from user's groups
        for (Group group : getUserGroups(userId)) {
            allPosts.addAll(group.getPosts());
        }

        // Sort posts by date (newest first)
        Collections.sort(allPosts, Comparator.comparing(Post::getCreationDate).reversed());

        return allPosts;
    }

    public void addPostToGroup(String groupId, Post post) {
        Group group = getGroupById(groupId);
        if (group != null) {
            group.addPost(post);
            saveGroupsToFile();
        }
    }

    private Group getGroupById(String groupId) {
        for (Group group : allGroups) {
            if (group.getGroupId().equals(groupId)) {
                return group;
            }
        }
        return null;
    }

    private void saveGroupsToFile() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(allGroups, writer);
        } catch (IOException e) {
            System.err.println("Error saving groups to file: " + e.getMessage());
        }
    }

    private void loadGroupsFromFile() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type groupListType = new TypeToken<ArrayList<Group>>() {}.getType();
            allGroups = gson.fromJson(reader, groupListType);
            if (allGroups == null) {
                allGroups = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Error loading groups from file: " + e.getMessage());
        }
    }
}