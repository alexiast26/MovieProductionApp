package movie_app.notification_service.domain;

public interface NotificationSender {
    void send(String message);
}
