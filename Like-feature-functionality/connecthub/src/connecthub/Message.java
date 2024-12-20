/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connecthub;

import java.util.Date;
import java.time.LocalDate;


public class Message {

    private String data;
    private String userID;
    private LocalDate dateOfCreation;
    
    public Message(String userID,String data){
        this.userID = userID;
        this.data = data;
        this.dateOfCreation = LocalDate.now();
    }
    
    public String getData(){
        return this.data;
    }
    
    public LocalDate getDateOfCreation(){
        return this.dateOfCreation;
    }
    
    public String getUserID(){
        return this.userID;
    }
    
}
