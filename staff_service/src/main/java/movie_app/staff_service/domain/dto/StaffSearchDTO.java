package movie_app.staff_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffSearchDTO {
    private Integer id;
    private String name;
    private String role;
    private String photoUrl;
    private Integer age;
}
