/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Asem
 */
public class Notification {

    private String notificationId;
    private String message;
    private String userid;
    private String type;
    private Boolean isRead;
    private LocalDateTime time;

    public Notification(String notificationId, String message, String userid, String type, Boolean isRead, LocalDateTime time) {
        this.notificationId = notificationId;
        this.message = message;
        this.userid = userid;
        this.type = type;
        this.isRead = isRead;
        this.time = time;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public String getUserid() {
        return userid;
    }

    public String getType() {
        return type;
    }

    public Boolean IsRead() {
        return isRead;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    
}
