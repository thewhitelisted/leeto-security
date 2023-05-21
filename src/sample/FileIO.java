/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author clee2
 */
public class FileIO {
    public static void exportData(Password password){
        FileOutputStream fs = null;
        try {
            // make smth that adds a number if it already exists idk when creating multiple of same name
            fs = new FileOutputStream("passwordlist.txt", true);
            List<String> passwordList = store();

            //Check for instances of the same password name:
            //If it already exists, then handle the exception
            int maxIndex = 0;
            String name = password.getName() + maxIndex;

            boolean exists = exists(password, passwordList);

            String newLine = System.lineSeparator();
            //If the password does not exist in the list, print to text file, else return a message saying password already exists
            if (!exists) {
                fs.write((password.name + newLine).getBytes(StandardCharsets.UTF_8));
            } else {
                System.out.println("Password Already Exists");
                return;
            }

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

           // read object from file and print object
           return (Password) ois.readObject();

       } catch (IOException | ClassNotFoundException ex) {
           ex.printStackTrace();
           return null;
       }
    }

    public static List<String> store() throws IOException {
        FileReader fr = new FileReader("passwordlist.txt");
        BufferedReader br = new BufferedReader(fr);

        String line;
        List<String> passwordList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            passwordList.add(line);
        }
        fr.close();
        br.close();
        return passwordList;

    }

    public static boolean exists(Password password, List<String> passwordList) {
        boolean alreadyExists = false;

        for (String s : passwordList) {
            if (password.getName().equals(s)) {
                System.out.println("The password already exists.");
                alreadyExists = true;
                break;
            }
        }

        return alreadyExists;

    }
}
