/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;


public class ChatClient implements Subscriber{

    private User chatUser;
    private ChatSystem currentSubscriebdSystem;
    
    public void SubscribeToChatSystem(ChatSystem cSystem){
        this.currentSubscriebdSystem = cSystem;
    }
    
    public void makeNewMessage(String messageData){
        
     Message msg = new Message(chatUser.getUserId(), messageData);
    
    }
   
    
    @Override
    public void setPublisher(Publisher p) {
        this.currentSubscriebdSystem = (ChatSystem)p;
    }
    
    @Override
    public void updatePublisher() {
        
    }

    
}
