/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package connecthub;

/**
 *
 * @author Dell
 */
public interface Subscriber {
    
    public abstract void setPublisher(Publisher p);
    public abstract void unsetPublisher();
    public abstract void updatePublisher();
}
