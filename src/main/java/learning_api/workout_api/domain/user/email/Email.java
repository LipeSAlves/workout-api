package learning_api.workout_api.domain.user.email;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
