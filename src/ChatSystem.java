/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.List;
import java.util.Map;


public class ChatSystem implements Publisher{

    private List<ChatClient> currentClients;
    private List<Message> fullChat;
    
    @Override
    public void subscribe(Subscriber s) {
        currentClients.add((ChatClient) s);
    }

    @Override
    public void unSubscribe(Subscriber s) {
        currentClients.remove((ChatClient) s);
    }

    @Override
    public void notifySubscribers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
