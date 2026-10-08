package movie_app.notification_service.service;

import movie_app.notification_service.domain.Contact;
import movie_app.notification_service.domain.Notification;
import movie_app.notification_service.domain.dao.INotificationDAO;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final INotificationDAO notificationDAO;
    private final EmailService emailFactory;
    private final WhatsAppService whatsAppFactory;

    public NotificationService(INotificationDAO notificationDAO, EmailService emailFactory, WhatsAppService whatsAppFactory) {
        this.notificationDAO = notificationDAO;
        this.emailFactory = emailFactory;
        this.whatsAppFactory = whatsAppFactory;
    }

    public void dispatchAlerts(Integer userId, String email, String phone){
        String alertMessage = "FYI, your account has been modified by one of our admins.";

        emailFactory.createSender().send(alertMessage);
        notificationDAO.save(new Notification(userId, alertMessage, Contact.EMAIL));

        whatsAppFactory.createSender().send(alertMessage);
        notificationDAO.save(new Notification(userId, alertMessage, Contact.SMS));
    }
}