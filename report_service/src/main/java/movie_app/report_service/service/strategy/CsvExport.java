package movie_app.report_service.service.strategy;

import movie_app.report_service.domain.Movie;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvExport implements MovieExportStrategy {
    @Override
    public byte[] generateReport(List<Movie> movies) {
        StringBuilder sb = new StringBuilder("ID,Name,Year,Genre,Category\n");
        for (Movie movie : movies) {
            sb.append(movie.getId()).append(",")
                    .append(movie.getName()).append(",")
                    .append(movie.getDuration()).append(",")
                    .append(movie.getReleaseYear()).append(",")
                    .append(movie.getGenre()).append(",")
                    .append(movie.getCategory()).append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
