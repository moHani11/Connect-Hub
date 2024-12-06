/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package connecthub;

import java.util.*;

public class ConnectHub {

    
    public static void main(String[] args) {

       User u = new User("111", "12@gmail.com","111","1", "2020-10-12");
       User u2 = new User("211", "2@gmail.com","33","2", "2020-10-12");

//            NewsFeed n = new NewsFeed(u2);
        
    ConnectHub.newMain();

//    SocialMediaApp s = new SocialMediaApp(u2);

    }
    
    public static void newMain(){
                    
            ConnectHubEngine c = new ConnectHubEngine();
            UserAccountManagement u = new UserAccountManagement(c);
            UserAccountManagmentGUI uGUI = new UserAccountManagmentGUI(u, c);
            
            c.loadData(u);
                   

            uGUI.setVisible(true);
            
            
    }
    
}
