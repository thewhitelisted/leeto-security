package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author clee2
 */
public class Algorithms {
    public static byte[] obtainSHA(String input) throws NoSuchAlgorithmException {
        // Create a new MessageDigest object using the SHA-256 hashing algorithm
        MessageDigest md = MessageDigest.getInstance("SHA-256"); //might need to change this to SHA-1 for length purposes.
        
        // Convert the input string to a byte array using the UTF-8 character set
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        
        // Compute the hash of the input byte array using the MessageDigest object
        byte[] hashedBytes = md.digest(inputBytes); 
        
        return hashedBytes;

    }
    public static String conversion(byte[] hash) {
        //Generating random bytes:
        //SecureRandom random = new SecureRandom();
        //byte[] salt = new byte[SALT_LENGTH];
        //random.nextBytes(salt);

        //Convert the byte array in the signum representation
        //BigInteger class to perform operators on large integers, such as the ones used in this code.
        BigInteger num = new BigInteger(1, hash);

        // Create a new StringBuilder object to build the large hashed string / convert message digest to hex value
        StringBuilder convert = new StringBuilder(num.toString(16));

        //Padding with the leading zeros
        while (convert.length() < 32) {
            convert.insert(0, '0');
        }

        //MessageDigest digest = MessageDigest.getInstance("SHA-256");
        //String encodedSalt = Base64.getEncoder().encodeToString(salt);

        //System.out.println(convert.toString());
        return convert.toString();
    }

    public static String hashString(String input) throws NoSuchAlgorithmException {
        return conversion(obtainSHA(input));
    }

    //input variable can be anything, I just needed to make a String method that returned a formatted date.
    public static String getDate_Time() throws Exception {
        LocalDateTime currentDate_Time = LocalDateTime.now();
        //Desired Date/Time Format:
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy h:mm a"); //Format the current date and time
        return currentDate_Time.format(formatter);
    }

    public static String createPassword(int letterCount) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?-().";
        String generatedPassword = "";
        char randomLetter = ' ';
        StringBuilder generate = new StringBuilder(letterCount);
        int maximum = characters.length();

        // Keep appending the generatedPassword with StringBuilder, adding one random character at a time
        for (int i = 0; i < letterCount; i++) {
            randomLetter = characters.charAt((char) (Math.random() * (maximum + 1)));
            generatedPassword = (generate.append(randomLetter)).toString();

        }

        // If the first letter starts with a '-' or a '.'; the letter has to change
        while (generatedPassword.charAt(0) == '-' || generatedPassword.charAt(0) == '.') {
            randomLetter = characters.charAt((char) (Math.random() * 26 + 'a'));
            generatedPassword = randomLetter + generatedPassword.substring(1);
        }

        return generatedPassword;

    }

    public static String ratePassword(String password) {

        int rating = 0;

        if (password.length() >= 8) {
            rating += 1;
        }
        if (password.matches(".*[\\p{Punct}].*")){
            rating += 1;
        }
        if (password.matches(".*\\d+.*")) {
            rating += 1;
        }

        boolean hasUppercase = false;
        for (int i = 0; i < password.length(); i++) {
            char letter = password.charAt(i);
            if (Character.isUpperCase(letter)) {
                hasUppercase = true;
            }
        }

        if (hasUppercase) {
            rating += 1;
        }


        String strRating = "";
        switch (rating) {
            case 0:
                strRating = "Very Weak. Your password truly needs improvements, and I recommend using our Password Creator Algorithms to create a secure password.";
                break;
            case 1:
                strRating = "Weak. You need to make improvements to your password, such as including more symbols for complexity or making it longer.";
                break;
            case 2:
                strRating = "Ok. It can still be improved, but at least your password can defend against less experienced attackers.";
                break;
            case 3:
                strRating = "Strong. Consider adding just a little more improvements to your password, and it will be extremely strong!";
                break;
            case 4:
                strRating = "Very strong. Your complex password, partnered with our advanced hashing algorithms, will surely defend against all types of attackers.";
                break;

        }
        return strRating;
    }



}
