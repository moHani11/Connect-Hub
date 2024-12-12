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
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author Asem
 */
public class NotificationManager {
    private static final String PATH = "notifications.json";
    private JSONArray notifications;
 public NotificationManager() throws IOException {
        this.notifications = loadNotificationsFromFile();
    }
 public writeNotificationsToFile(){
     try(FileWriter file =new FileWriter(PATH)){
         
     }
 }
}
