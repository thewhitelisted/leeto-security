/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leeto.security;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author clee2
 */
public class Password implements Serializable{
    String name;
    String key;
    String timeCreated;
    //default serialVersion id
    private static final long serialVersionUID = 1L;
    
    public Password(String name, String key, String timeCreated){
        this.name = name;
        this.key = key;
        this.timeCreated = timeCreated;
    }
    
    public static String returnHashed(String key) throws NoSuchAlgorithmException {
        return Algorithms.hashString(key);
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getKey(){
        return this.key;
    }
    
    public void setKey(String key){
        this.key = key;
    }
    
    public String getTimeCreated(){
        return this.timeCreated;
    }
    
    public void setTimeCreated(String timeCreated){
        this.timeCreated = timeCreated;
    }
}
