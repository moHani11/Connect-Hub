/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Asem
 */
public class NotificationManager {

    private static final String PATH = "notifications.json";
    private List<Notification> notificationsList;

    public NotificationManager() throws IOException {
        this.notificationsList = loadNotificationsFromFile();
    }

    public void writeNotificationsToFile() {

        try (FileWriter file = new FileWriter(PATH)) {
            JSONArray notiJSONArray = new JSONArray();

            // عمل object من كل اشعار
            for (Notification noti : notificationsList) {
                JSONObject JSONnotiObj = new JSONObject();
                JSONnotiObj.put("notificationId", noti.getNotificationId());
                JSONnotiObj.put("userId", noti.getUserid());
                JSONnotiObj.put("type", noti.getType());
                JSONnotiObj.put("message", noti.getMessage());
                JSONnotiObj.put("timestamp", noti.getTime().toString());// حولته ل string
                JSONnotiObj.put("isRead", noti.IsRead());
                notiJSONArray.add(JSONnotiObj);
            }
            // كتابة البيانات إلى الملف في تنسيق JSON
            file.write(notiJSONArray.toJSONString());  
            System.out.println("Notifications saved to file successfully!"); 

        } catch (IOException e) {
            e.printStackTrace();  
        }
    }
    
    public List<Notification> loadNotificationsFromFile (){
        List<Notification> loadedNoti = new ArrayList<>();
    }
}

