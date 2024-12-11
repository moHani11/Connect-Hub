package connecthub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.GroupLayout;

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
/*    public List<GroupLayout.Group> searchGroups(String query) {
    List<GroupLayout.Group> results = new ArrayList<>();
    for (GroupLayout.Group group : groupDatabase.values()) { // let i have `groupDatabase`
        if (group.getName().toLowerCase().contains(query.toLowerCase())) {
            results.add(group);
        }
    }
    return results;
} */
}
