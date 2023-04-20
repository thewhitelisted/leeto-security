/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author clee2
 */
public class FileIO {
    public static void exportData(Password password){
        FileOutputStream fs = null;
        try {
            // make smth that adds a number if it already exists idk when creating multiple of same name
            fs = new FileOutputStream("passwordlist.txt");
            fs.write(password.name.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (FileOutputStream fos = new FileOutputStream(password.name+".dat");
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // create a new object
            Password user = new Password(password.name, password.key, password.timeCreated);

            // write object to file
            oos.writeObject(user);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static Password importData(String name){
        try (FileInputStream fis = new FileInputStream(name+".dat");
            ObjectInputStream ois = new ObjectInputStream(fis)) {

           // read object from file
           Password user = (Password) ois.readObject();

           // print object
           return user;

       } catch (IOException | ClassNotFoundException ex) {
           ex.printStackTrace();
           return null;
       }
    }
}
