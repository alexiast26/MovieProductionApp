package movie_app.notification_service.domain.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private Integer userId;
    private String email;
    private String phone;
    private String message;
}
