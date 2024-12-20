package connecthub;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



public class ChatSystemFacade {

    private List<ChatSystem> allChatSystems;
    private String CHATS_FILE_NAME = "chats.json";
    
    public ChatSystemFacade(){
        allChatSystems = new ArrayList<ChatSystem>();
        loadChatsFromFile();
        
    }
    
    public void AddMessageToChat(String msg, String userID, ChatSystem chatSystem){        
        ChatClient currentClient = chatSystem.getClient(userID);
        if (currentClient != null){
                    currentClient.makeNewMessage(msg);
                    currentClient.updatePublisher();
                    saveChatsToFile();
        }
        
        else  System.out.println("There is no Client with such ID in this ChatSystem");
        
    }
    
    public ChatSystem getChatByUsers(List<String> usersIDs){
        loadChatsFromFile();
        
        for (ChatSystem chat : allChatSystems){
            List<String> chatIDs = chat.getChatUsersIds();
            if (isListsTheSame(chatIDs, usersIDs))
                return chat;
        }
      ChatSystem newChat = createNewChatSystem(usersIDs);
      return newChat;
    }
    
    public ChatSystem createNewChatSystem(List<String> usersIDs){
        loadChatsFromFile();
        
        ChatSystem chatSys = new ChatSystem();
        for (String userID : usersIDs){
                ChatClient client = new ChatClient(userID);
                chatSys.subscribe(client);
            }
        allChatSystems.add(chatSys);
        saveChatsToFile();
        return chatSys;
    }
    
    
    public void saveChatsToFile() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(CHATS_FILE_NAME)) {
            gson.toJson(allChatSystems, writer);
        } catch (IOException e) {
            System.err.println("Error saving Chats to file: " + e.getMessage());
        }
    }
    
    public  void loadChatsFromFile() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(CHATS_FILE_NAME)) {
            Type groupListType = new TypeToken<ArrayList<ChatSystem>>() {}.getType();
            allChatSystems = gson.fromJson(reader, groupListType);
            if (allChatSystems == null) {
                allChatSystems = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Error loading Chats from file: " + e.getMessage());
        }
    }
    
    public boolean isListsTheSame(List<String> l1, List<String> l2){
        if (l1.size() != l2.size()) return false;
        
        for(String str : l1){
            if (! l2.contains(str)) return false;
        }
        return true;
    }

}
