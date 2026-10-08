package movie_app.notification_service.infrastructure.repository;

import movie_app.notification_service.infrastructure.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Integer> {
}
