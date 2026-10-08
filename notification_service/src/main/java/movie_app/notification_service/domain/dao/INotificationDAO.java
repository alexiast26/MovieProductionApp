package movie_app.notification_service.domain.dao;

import movie_app.notification_service.domain.Notification;

import java.util.*;

public interface INotificationDAO {
    Notification save(Notification notification);
    List<Notification> findAll();
}
