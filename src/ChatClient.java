/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.List;


public class ChatClient implements Subscriber{

    private String userID;
    private ChatSystem currentSubscriebdSystem;
    private Message lastCreatedMessage;
    
    public ChatClient(String userID){
        this.userID = userID;
    }
    
    public void SubscribeToChatSystem(ChatSystem cSystem){
        this.currentSubscriebdSystem = cSystem;
    }
    
    public void makeNewMessage(String messageData){
        
     Message msg = new Message(userID, messageData);
     this.lastCreatedMessage = msg;
    
    }
   
    public String getUserID(){
        return this.userID;
    }
    
    @Override
    public void setPublisher(Publisher p) {
        this.currentSubscriebdSystem = (ChatSystem)p;
    }
    
    @Override
    public void unsetPublisher() {
        this.currentSubscriebdSystem = null;
    }
    
    @Override
    public void updatePublisher() {
        List<Message> fullChat = currentSubscriebdSystem.getFullChat();
//        synchronized(fullChat){
            fullChat.add(lastCreatedMessage);
            currentSubscriebdSystem.notifySubscribers();
//        }
    }

    
}
