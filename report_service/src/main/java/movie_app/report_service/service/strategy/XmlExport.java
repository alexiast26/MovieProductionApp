package movie_app.report_service.service.strategy;
import movie_app.report_service.domain.Movie;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class XmlExport implements MovieExportStrategy{
    @Override
    public byte[] generateReport(List<Movie> movies) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<movies>\n");

        for (Movie movie : movies) {
            xmlBuilder.append("  <movie>\n");
            xmlBuilder.append("    <id>").append(movie.getId()).append("</id>\n");
            xmlBuilder.append("    <name>").append(movie.getName()).append("</name>\n");
            xmlBuilder.append("    <duration>").append(movie.getDuration()).append("</duration>\n");
            xmlBuilder.append("    <year>").append(movie.getReleaseYear()).append("</year>\n");
            xmlBuilder.append("    <genre>").append(movie.getGenre()).append("</genre>\n");
            xmlBuilder.append("    <category>").append(movie.getCategory()).append("</category>\n");
            xmlBuilder.append("  </movie>\n");
        }

        xmlBuilder.append("</movies>");
        return xmlBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

}
