/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.*;



 
    public class User { 
        private String userId;
        private String email;
        private String username;
        private String password; // hashed password
        public String dateOfBirth;
        private boolean isOnline;
        public PostManager postManager;
        public StoryManager storyManager;
        private Set<String> friends = new HashSet<>();
        private Set<String> friendRequests = new HashSet<>();
        private Set<String> blockedUsers = new HashSet<>();
        private UserAccountManagement userAccountManagement;
        private boolean isAdmin;
        private boolean isPrimaryAdmin;
        private List<String> joinedGroups;

        public User() {
        }

        public User(String userId, String email, String username, String password, String dateOfBirth) {
            postManager = new PostManager(userId);  // Initialize PostManager for handling posts
            storyManager = new StoryManager(userId);  // Initialize StoryManager for handling stories
            
            this.userId = userId;
            this.email = email;
            this.username = username;
            this.password = password;
            this.dateOfBirth = dateOfBirth;
            this.isOnline = false;
             this.isAdmin = false;
        this.isPrimaryAdmin = false;
        this.joinedGroups = new ArrayList<>();
        }

        public String getEmail(){
            return this.email;
        }
        
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public String getPassword() {
            return password;
        }

        public boolean isOnline() {
            return isOnline;    // Getter for online status
        }
        
        public String getstatus(){
            if (this.isOnline) return "Online";
            return "Offline";
        }

        public void setOnline(boolean online) {
            isOnline = online;    // Setter for online status
        }
        
            public Set<String> getFriends() {
            return friends;
        }

        public void setFriends(Set<String> friends) {
            this.friends = friends;
        }
        
        public Set<String> getBlockedUsers() {
            return blockedUsers;
        }
        
        public Set<String> getFriendRequests() {
            return friendRequests;
        }
public void addFriend(String email) {
            this.friends.add(email);
        }public void removeFriend(String email) {
            this.friends.remove(email);
        }

        public void addFriendRequest(String email) {
            this.friendRequests.add(email);
        }

        public void removeFriendRequest(String email) {
            this.friendRequests.remove(email);
        }

        public void blockUser(String email) {
            this.blockedUsers.add(email);
        }

        public void unblockUser(String email) {
            this.blockedUsers.remove(email);
        }
        
        
        // new methods
        //----------------//
         public void joinGroup(String groupName) {
        if (!joinedGroups.contains(groupName)) {
            joinedGroups.add(groupName);
        }
    }

    public void leaveGroup(String groupName) {
        joinedGroups.remove(groupName);
    }

    public boolean isGroupAdmin(String groupName) {
        return isAdmin && joinedGroups.contains(groupName);
    }

    public List<String> getJoinedGroups() {
        return joinedGroups;
    }
    
     // Getter and Setter for isAdmin
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // Getter and Setter for isPrimaryAdmin
    public boolean isPrimaryAdmin() {
        return isPrimaryAdmin;
    }

    public void setPrimaryAdmin(boolean isPrimaryAdmin) {
        this.isPrimaryAdmin = isPrimaryAdmin;
    }
    
   // -------------------------//
    
    
        public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("dateOfBirth", dateOfBirth);
            jsonObject.put("isOnline", isOnline);

    JSONArray friendsArray = new JSONArray();
    friendsArray.addAll(friends);
    jsonObject.put("friends", friendsArray);

    JSONArray friendRequestsArray = new JSONArray();
    friendRequestsArray.addAll(friendRequests);
    jsonObject.put("friendRequests", friendRequestsArray);

    JSONArray blockedUsersArray = new JSONArray();
    blockedUsersArray.addAll(blockedUsers);
    jsonObject.put("blockedUsers", blockedUsersArray);

    
    
            return jsonObject;
        }

        public static User fromJson(JSONObject jsonObject) {
            String userId = (String) jsonObject.get("userId");
            String email = (String) jsonObject.get("email");
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            String dateOfBirth = (String) jsonObject.get("dateOfBirth");
            boolean isOnline = (boolean) jsonObject.get("isOnline");

            User user = new User(userId, email, username, password, dateOfBirth);
            user.isOnline = isOnline;

            // Deserialize JSON arrays to sets
            user.friends = new HashSet<>((JSONArray) jsonObject.get("friends"));
            user.friendRequests = new HashSet<>((JSONArray) jsonObject.get("friendRequests"));
            user.blockedUsers = new HashSet<>((JSONArray) jsonObject.get("blockedUsers"));



            return user;
        }
 }


    