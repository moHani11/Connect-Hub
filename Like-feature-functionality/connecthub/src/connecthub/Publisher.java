/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package connecthub;

import java.util.List;
import java.util.ArrayList;

public interface Publisher {
        
    public abstract void subscribe(Subscriber s);
    public abstract void unSubscribe(Subscriber s);
    public abstract void notifySubscribers();
    
}

    

