package movie_app.notification_service.service;

import movie_app.notification_service.domain.EmailSenderProduct;
import movie_app.notification_service.domain.NotificationSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends NotificationFactory {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public NotificationSender createSender() {
        return new EmailSenderProduct(mailSender);
    }
}
