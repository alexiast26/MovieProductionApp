package movie_app.staff_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class RequestDTO {
    private String name;
    private int age;
    private String gender;
    private String image_url;
}
