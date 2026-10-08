package movie_app.notification_service.domain;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSenderProduct implements NotificationSender {
    private final JavaMailSender mailSender;

    public EmailSenderProduct(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String messageContent) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("alexiastoleru26@gmail.com");
        mailMessage.setTo("thelexy04@gmail.com");
        mailMessage.setSubject("System Notification - Account modification");
        mailMessage.setText("Hey there,\n\n" + messageContent + "\nThought you might want to know :)\n\nHave a nice day!");

        try {
            mailSender.send(mailMessage);
            System.out.println("Email product sent successfully to thelexy04@gmail.com!");
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea mail-ului: " + e.getMessage());
            e.printStackTrace();
        }
    }
}