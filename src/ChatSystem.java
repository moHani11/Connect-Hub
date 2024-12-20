/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChatSystem implements Publisher{

    private List<ChatClient> currentClients;
    private List<Message> fullChat;
    private List<String> usersIds;
    private String chatSystemID = "";
    
    public ChatSystem(){
        currentClients = new ArrayList<ChatClient>();
        fullChat = new ArrayList<Message>();
        usersIds = new ArrayList<String>();
    }
    
    public String getSystemID(){
        return chatSystemID;
    }

    public void setSystemID(String newID){
        chatSystemID = newID;
    }
    
    public List<String> getChatUsersIds(){
        return usersIds;
    }
    
    public ChatClient getClient(String UserID){
        for (ChatClient client : currentClients){
            if (client.getUserID().equals(UserID)){
                return client;
            }
        } return null;
    }
    
    public List<Message> getFullChat(){
        return this.fullChat;
    }
    
    @Override
    public void subscribe(Subscriber s) {
        currentClients.add((ChatClient) s);
        s.setPublisher(this);
        ChatClient ch = (ChatClient)s;
        this.usersIds.add(ch.getUserID());
    }

    @Override
    public void unSubscribe(Subscriber s) {
        currentClients.remove((ChatClient) s);
        s.unsetPublisher();
        ChatClient ch = (ChatClient)s;
        this.usersIds.remove(ch.getUserID());
    }

    @Override
    public void notifySubscribers() {
        //Should  Save new Data
    }
    
    
    
}
