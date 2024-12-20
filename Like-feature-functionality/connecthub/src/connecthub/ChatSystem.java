/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChatSystem implements Publisher{

    private List<String> currentClientsIDs;
    private List<Message> fullChat;
    private String chatSystemID = "";
    
    public ChatSystem(){
        currentClientsIDs = new ArrayList<String>();
        fullChat = new ArrayList<Message>();
    }
    
    public String getSystemID(){
        return chatSystemID;
    }

    public void setSystemID(String newID){
        chatSystemID = newID;
    }
    
    public List<String> getChatUsersIds(){
        return currentClientsIDs;
    }
    
    public ChatClient getClient(String UserID){
        for (String clientID : currentClientsIDs){
            if (clientID.equals(UserID)){
                return new ChatClient(UserID, this);
            }
        } return null;
    }
    
    public List<Message> getFullChat(){
        return this.fullChat;
    }
    
    @Override
    public void subscribe(Subscriber s) {
        ChatClient ch = (ChatClient)s;
        this.currentClientsIDs.add(ch.getUserID());
        s.setPublisher(this);
    }

    @Override
    public void unSubscribe(Subscriber s) {
        ChatClient ch = (ChatClient)s;
        this.currentClientsIDs.remove(ch.getUserID());
        s.unsetPublisher();
    }

    @Override
    public void notifySubscribers() {
        //Should  Save new Data
    }
    
    
    
}
