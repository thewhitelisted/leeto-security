package sample;

import java.util.ArrayList;
import java.util.List;

public class Recommendations {
    public static List<String> ratingv2(String password) {

        List<String> recommended = new ArrayList<>();

        if (password.length() < 8) {
            recommended.add("Increase the length of your password.");
        }

        if (!password.matches(".*[\\p{Punct}].*")) {
            recommended.add("Add at least one set of punctuation such as '!' or '$' to your password.");
        }

        if (!password.matches(".*\\d+.*")) {
            recommended.add("Add at least one number to your password.");
        }

        boolean hasUppercase = false;
        for (int i = 0; i < password.length(); i++) {
            char letter = password.charAt(i);
            if (Character.isUpperCase(letter)) {
                hasUppercase = true;
            }
        }

        if (!hasUppercase) {
            recommended.add("Add at least one uppercase letter to your password.");
        }

        return recommended;

    }
}
