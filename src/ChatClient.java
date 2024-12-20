/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.List;


public class ChatClient implements Subscriber{

    private User chatUser;
    private ChatSystem currentSubscriebdSystem;
    private Message lastCreatedMessage;
    
    public void SubscribeToChatSystem(ChatSystem cSystem){
        this.currentSubscriebdSystem = cSystem;
    }
    
    public void makeNewMessage(String messageData){
        
     Message msg = new Message(chatUser.getUserId(), messageData);
     this.lastCreatedMessage = msg;
    
    }
   
    public String getUserID(){
        return this.chatUser.getUserId();
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
