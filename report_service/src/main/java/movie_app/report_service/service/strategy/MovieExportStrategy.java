package movie_app.report_service.service.strategy;

import movie_app.report_service.domain.Movie;
import java.util.List;

public interface MovieExportStrategy {
    byte[] generateReport(List<Movie> movies);
}
