package movie_app.report_service.controller;

import movie_app.report_service.domain.dto.MovieStatisticsDTO;
import movie_app.report_service.service.ReportingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportingService reportingService;

    public ReportController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/users/csv")
    public ResponseEntity<String> exportUserCsvReport() {
        String csv = reportingService.generateUserCsvReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @GetMapping("/movies")
    public ResponseEntity<byte[]> exportMovies(@RequestParam(defaultValue = "JSON") String format) {
        try{
            byte[] reportData = reportingService.downloadExport(format);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movie_report." + (format.toLowerCase()));

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(reportData);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/movies/stats")
    public ResponseEntity<MovieStatisticsDTO> getMovieStatistics() {
        MovieStatisticsDTO stats = reportingService.generateMovieStatistics();

        return ResponseEntity.ok(stats);
    }
}
