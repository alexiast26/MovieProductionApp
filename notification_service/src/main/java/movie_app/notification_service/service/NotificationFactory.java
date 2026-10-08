package movie_app.notification_service.service;

import movie_app.notification_service.domain.NotificationSender;

public abstract class NotificationFactory {
    public abstract NotificationSender createSender();
}
