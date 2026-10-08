package movie_app.report_service.service.strategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import movie_app.report_service.domain.Movie;

import java.nio.charset.StandardCharsets;
import java.util.List;
public class JsonExport implements MovieExportStrategy {
    @Override
    public byte[] generateReport(List<Movie> movies) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movies);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[]".getBytes(StandardCharsets.UTF_8);
        }
    }
}
