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
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                JSONnotiObj.put("time", noti.getTime().toString());// حولته ل string
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

    public List<Notification> loadNotificationsFromFile() {
        List<Notification> loadedNoti = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(PATH)) {
            // قراءة البيانات من الملف وتحويلها إلى JSONArray
            // هي فيها كائنات وانا هحولها تاني لاشعارات
            JSONArray notificationsArray = (JSONArray) parser.parse(reader);
            for (Object obj : notificationsArray) {
                JSONObject notificationJson = (JSONObject) obj;
                Notification noti = new Notification(
                        (String) notificationJson.get("notificationId"),
                        (String) notificationJson.get("message"),
                        (String) notificationJson.get("userId"),
                        (String) notificationJson.get("type"),
                        (boolean) notificationJson.get("isRead"),
                        LocalDateTime.parse((String) notificationJson.get("time"))
                );
                loadedNoti.add(noti);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(NotificationManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return loadedNoti;
    }

    public void addNotification(String userID, String type, String message) {
        Notification noti = new Notification(
                String.valueOf(System.currentTimeMillis())/*فكرة حلوة لعمل اي دي جديد لكل اشعار بالوقت الحالي*/,
                message,
                userID,
                type,
                false/*الاشعار بيبدأ جديد متقراش*/,
                LocalDateTime.now()
        );
        notificationsList.add(noti);
        writeNotificationsToFile();
    }

    public List<Notification> getNotificationsForUser(String userId) {
        List<Notification> userNotifications = new ArrayList<>();
        System.out.println("Loading notifications for user: " + userId);  // إضافة طباعة للتأكد من المعرف المستخدم
        for (Notification noti : notificationsList) {
            if (noti.getUserid().equals(userId)) {
                userNotifications.add(noti);
            }
        }
        
        // إذا كانت الإشعارات فارغة، طبع رسالة للتحقق
        if (userNotifications.isEmpty()) {
            System.out.println("No notifications found for user: " + userId);
        }
        
        return userNotifications;
    }

    public void markAsRead(String notificationId) {
        for (Notification notification : notificationsList) {
            if (notification.getNotificationId().equals(notificationId)) {
                notification.setIsRead(true); // تغيير حالة الإشعار إلى مقروء
                break;
            }
        }
        writeNotificationsToFile();
    }

    public void deleteNotification(String notificationId) {
        notificationsList.removeIf(notification -> notification.getNotificationId().equals(notificationId));
        writeNotificationsToFile();
    }

    public void sendNotificationsToFriends(String userId, String type) {
        // استرجاع قائمة الأصدقاء للمستخدم
        ConnectHubEngine c = new ConnectHubEngine();
        UserAccountManagement userAccountManagement = new UserAccountManagement(c);
        String email = userAccountManagement.getEmailByID(userId);
        Set<String> friendsEmails = userAccountManagement.getFriends(email);

        // إنشاء إشعار لكل صديق
        String username =userAccountManagement.getUsernameByID(userId);
        for (String friendId : friendsEmails) {
            String message;
            switch (type) {
                case "NewPost":
                    message = "Your friend " + username + " has posted a new post!";
                    break;
                case "FriendRequest":
                    message = "Your friend " + username + " has sent you a friend request.";
                    break;
                case "GroupActivity":
                    message = "Your friend " + username + " has updated group activities.";
                    break;
                case "NewStory":
                    message = "Your friend " + username + " has Posted new Story.";
                    break;
                default:
                    message = "Your friend " + username + " has an update.";
            }
            addNotification(friendId, type, message);
        }

        System.out.println("Notifications sent to all friends of user: " + userId);
    }

//
//public static void main(String[] args) {
//    try {
//        // إنشاء كائن NotificationManager
//        NotificationManager manager = new NotificationManager();
//
//        // 1. اختبار إضافة إشعار جديد
//        System.out.println("Adding a new notification...");
//        manager.addNotification("user123", "FriendRequest", "You have a new friend request from Ahmed.");
//        manager.addNotification("user456", "NewPost", "A new post was added to your group.");
//        System.out.println("Notifications added!");
//
//        // 2. قراءة الإشعارات من الملف
//        System.out.println("\nLoading notifications from file...");
//        List<Notification> loadedNotifications = manager.loadNotificationsFromFile();
//        System.out.println("Loaded notifications:");
//        for (Notification noti : loadedNotifications) {
//            System.out.println(noti);
//        }
//
//        // 3. استرجاع الإشعارات الخاصة بمستخدم معين
//        System.out.println("\nFetching notifications for user123...");
//        List<Notification> userNotifications = manager.getNotificationsForUser("user123");
//        System.out.println("User123 notifications:");
//        for (Notification noti : userNotifications) {
//            System.out.println(noti);
//        }
//
//        // 4. اختبار وضع الإشعار كمقروء
//        if (!userNotifications.isEmpty()) {
//            String notificationId = userNotifications.get(0).getNotificationId();
//            System.out.println("\nMarking notification as read: " + notificationId);
//            manager.markAsRead(notificationId);
//            System.out.println("Notification marked as read!");
//        }
//
//        // 5. اختبار حذف الإشعار
//        System.out.println("\nDeleting notification for user123...");
//        if (!userNotifications.isEmpty()) {
//            String notificationIdToDelete = userNotifications.get(0).getNotificationId();
//            manager.deleteNotification(notificationIdToDelete);
//            System.out.println("Notification deleted!");
//        }
//
//        // 6. إرسال إشعارات إلى الأصدقاء
//        System.out.println("\nSending notifications to friends...");
//        manager.sendNotificationsToFriends("user123", "NewPost");
//        System.out.println("Notifications sent to friends!");
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//}
}