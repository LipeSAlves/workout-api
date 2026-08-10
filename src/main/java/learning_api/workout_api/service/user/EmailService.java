package learning_api.workout_api.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject("Workout API - Password Reset");
        message.setText(buildPasswordResetBody(resetToken));
        mailSender.send(message);
    }

    private String buildPasswordResetBody(String resetToken) {
        return """
                Hello,

                We received a request to reset your password.

                Use the token below in the reset-password endpoint:

                %s

                This token is valid for 15 minutes.

                If you did not request a password reset, you can ignore this e-mail.

                Workout API
                """.formatted(resetToken);
    }
}
