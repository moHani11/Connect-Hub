package connecthub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {

    private UserAccountManagement userAccountManagement;

    public Search(UserAccountManagement userAccountManagement) {
        this.userAccountManagement = userAccountManagement;
    }

    // Method to search users by username
    public List<User> searchUsersByUsername(String query) {
        List<User> matchingUsers = new ArrayList<>();
        Map<String, User> userDatabase = userAccountManagement.getUserDatabase();

        for (User user : userDatabase.values()) {
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }

    // Method to search groups by name
    public List<Group> searchGroupsByName(String query) {
        List<Group> matchingGroups = new ArrayList<>();
        for (Group group : GroupManager.getAllGroups()) {
            if (group.getName().toLowerCase().contains(query.toLowerCase())) {
                matchingGroups.add(group);
            }
        }
        return matchingGroups;
    }
}
