package movie_app.notification_service.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movie_app.notification_service.domain.Contact;
import movie_app.notification_service.domain.Notification;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String message;
    private LocalDateTime timestamp;
    private String status;

    @Enumerated(EnumType.STRING)
    private Contact contactType;

    public NotificationEntity(Integer userId, String message, LocalDateTime timestamp, String status, Contact contactType) {
        this.userId = userId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = "SENT";
        this.contactType = contactType;
    }

    public Notification toDomain(){
        return new Notification(this.id, this.userId, this.message, this.timestamp, this.status, this.contactType);
    }

    public static NotificationEntity fromDomain(Notification notification) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(notification.getId());
        entity.setUserId(notification.getUserId());
        entity.setMessage(notification.getMessage());
        entity.setTimestamp(notification.getTimestamp());
        entity.setStatus(notification.getStatus());
        entity.setContactType(notification.getContactType());
        return entity;
    }
}
