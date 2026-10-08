package movie_app.notification_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO {
    private Integer userId;
    private String email;
    private String phone;
    private String name;
}
