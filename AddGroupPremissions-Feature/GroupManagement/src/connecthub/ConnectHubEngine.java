/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.*;

public class ConnectHubEngine {

    private List<User> usersList;
    private List<String> usersStatus;


    // Constructor to initialize the lists
    public ConnectHubEngine() {
        this.usersList = new ArrayList<>();
        this.usersStatus = new ArrayList<>();
        
    }

    public void loadData(UserAccountManagement u){
        this.usersList = JsonDataManger.loadUsersFromJsonFile("usersss.json");
        
        this.populateUserDatabase(u);
    }
    
    public void saveData(UserAccountManagement u){
        this.populateUsersListFromMap(u.userDatabase);
//        System.out.println(u.getUserByEmail("2@gmail.com").getFriendRequests());
        JsonDataManger.writeUsers(usersList, "usersss.json");
    }
    
    // Getters and Setters for usersList
    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    // Getters and Setters for usersStatus
    public List<String> getUsersStatus() {
        return usersStatus;
    }

    public void setUsersStatus(List<String> usersStatus) {
        this.usersStatus = usersStatus;
    }

    // Methods for usersList
    public void addUser(User user) {
        if (!usersList.contains(user)) {
            usersList.add(user);
        }
    }

    public boolean removeUser(User user) {
        return usersList.remove(user);
    }

    public boolean containsUser(User user) {
        return usersList.contains(user);
    }

    // Methods for usersStatus
    public void addUserStatus(String status) {
        if (!usersStatus.contains(status)) {
            usersStatus.add(status);
        }
    }

    public boolean removeUserStatus(String status) {
        return usersStatus.remove(status);
    }

    public boolean containsUserStatus(String status) {
        return usersStatus.contains(status);
    }

    // Utility to clear all data from the lists
    public void clearAll() {
        usersList.clear();
        usersStatus.clear();
    }
    
    public void updateUserDatabase(Map<String, User> userDatabase){
           
        System.out.println((userDatabase));
        populateUsersListFromMap(userDatabase);
        JsonDataManger.writeUsers(usersList, "usersss.json");
    }
   
    public void populateUsersListFromMap(Map<String, User> userDatabase) {
    if (userDatabase != null) {
        usersList = new ArrayList<>(userDatabase.values());
        
    } else {
        System.out.println("userDatabase is null. Cannot populate usersList.");
    }
    }

    public void populateUserDatabase(UserAccountManagement u) {
        // Check if usersList is not null and is not empty
        if (usersList != null) {
            for (User user : usersList) {
                u.userDatabase.put(user.getEmail(), user);
            }
        } else {
            System.out.println("The user list is null or empty!");
        }
    }
    
    public User updateUser(UserAccountManagement userAccountManagement, User user){
        this.usersList = JsonDataManger.loadUsersFromJsonFile("usersss.json");
        String Email = userAccountManagement.getEmailByID(user.getUserId());
        populateUserDatabase(userAccountManagement);
        user = userAccountManagement.getUserByEmail(Email);
        return user;
    } 


}
