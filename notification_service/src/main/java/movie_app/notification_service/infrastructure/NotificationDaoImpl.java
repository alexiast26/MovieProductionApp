package movie_app.notification_service.infrastructure;

import movie_app.notification_service.domain.Notification;
import movie_app.notification_service.domain.dao.INotificationDAO;
import movie_app.notification_service.infrastructure.repository.JpaNotificationRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class NotificationDaoImpl implements INotificationDAO {
    private final JpaNotificationRepository jpaNotificationRepository;

    public NotificationDaoImpl(JpaNotificationRepository jpaNotificationRepository) {
        this.jpaNotificationRepository = jpaNotificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity notificationEntity = NotificationEntity.fromDomain(notification);
        NotificationEntity savedNotification = jpaNotificationRepository.save(notificationEntity);
        return savedNotification.toDomain();
    }

    @Override
    public List<Notification> findAll() {
        List<NotificationEntity> notificationEntities = jpaNotificationRepository.findAll();
        return notificationEntities.stream()
                .map(NotificationEntity::toDomain)
                .collect(Collectors.toList());
    }

}
