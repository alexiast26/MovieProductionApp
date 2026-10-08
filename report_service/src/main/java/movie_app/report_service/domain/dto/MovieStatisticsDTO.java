package movie_app.report_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@AllArgsConstructor
@Getter
public class MovieStatisticsDTO {
    private int totalMovies;
    private Map<String, Long> moviesByCategory;
    private Map<Integer, Long> moviesPerYear;
    private Map<String, Integer> actorsPerMovie;

}
