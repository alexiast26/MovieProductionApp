package movie_app.report_service.service.strategy;
import movie_app.report_service.domain.Movie;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class WordExport implements MovieExportStrategy {
    @Override
    public byte[] generateReport(List<Movie> movies) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Lista Filme");
            titleRun.setBold(true);
            titleRun.setFontSize(20);

            for (Movie movie : movies) {
                XWPFParagraph p = document.createParagraph();
                XWPFRun run = p.createRun();
                run.setText("ID: " + movie.getId() + " | Name: " + movie.getName() + " | Release Year: " + movie.getReleaseYear() + " | Duration: " + movie.getDuration() + " | Genre: " + movie.getGenre() + " | Category: " + movie.getCategory());
            }

            document.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
