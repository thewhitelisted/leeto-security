package sample;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.math.BigInteger;

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
        SecureRandom random = new SecureRandom();
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
        String hashed = conversion(obtainSHA(input));
        return hashed;
    }


}
