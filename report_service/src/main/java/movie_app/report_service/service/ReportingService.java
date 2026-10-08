package movie_app.report_service.service;

import movie_app.report_service.domain.Movie;
import movie_app.report_service.domain.User;
import movie_app.report_service.domain.dto.MovieStatisticsDTO;
import movie_app.report_service.service.strategy.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ReportingService {
    private final RestTemplate restTemplate;

    public ReportingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //MOVIE FETCH
    private List<Movie> fetchAllMovies() {
        String url = "http://movie-service:8082/api/movies";
        try{
            Movie[] response = restTemplate.getForObject(url, Movie[].class);
            if (response != null) {
                return Arrays.asList(response);
            }
        }catch (Exception e){
            System.out.println("Error fetching movies" + e.getMessage());
        }
        return Collections.emptyList();
    }

    //USER FETCH
    private List<User> fetchAllUsers() {
        String url = "http://user-service:8080/api/users";
        try {
            User[] response = restTemplate.getForObject(url, User[].class);
            if (response != null) {
                return Arrays.asList(response);
            }
        }catch (Exception e){
            System.out.println("Error fetching users" + e.getMessage());
        }
        return Collections.emptyList();
    }


    public byte[] downloadExport(String format){
        List<Movie> movies = fetchAllMovies();
        MovieExportStrategy strategy;
        switch(format.toUpperCase()){
            case "CSV":
                strategy = new CsvExport();
                break;
            case "JSON":
                strategy = new JsonExport();
                break;
            case "XML":
                strategy = new XmlExport();
                break;
            case "WORD":
                strategy = new WordExport();
                break;
            default:
                throw new IllegalArgumentException("Format not supported: " + format);
        }

        return strategy.generateReport(movies);
    }

    //CSV users
    public String generateUserCsvReport() {
        List<User> users = fetchAllUsers();
        StringBuilder csvBuilder = new StringBuilder("ID,Username,Email,Phone,Role\n");
        for (User user : users) {
            csvBuilder.append(user.getId()).append(",")
                    .append(user.getUsername()).append(",")
                    .append(user.getEmail()).append(",")
                    .append(user.getPhone()).append(",")
                    .append(user.getRole()).append("\n");
        }
        return csvBuilder.toString();
    }


    public MovieStatisticsDTO generateMovieStatistics() {
        List<Movie> movies = fetchAllMovies();
        if (movies.isEmpty()) {
            return new MovieStatisticsDTO(0, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        }

        int totalMovies = movies.size();
        Map<String, Long> moviesByGenre = movies.stream()
                .filter(m->m.getCategory() != null)
                .collect(Collectors.groupingBy(Movie::getCategory, Collectors.counting()));

        Map<Integer, Long> moviesByYear = movies.stream()
                .collect(Collectors.groupingBy(Movie::getReleaseYear, Collectors.counting()));

        Map<String, Integer> actorsPerMovie = movies.stream()
                .collect(Collectors.toMap(Movie::getName, movie -> movie.getActorsId() != null ? movie.getActorsId().size() : 0, (existing, next) -> existing)
        );

        return new MovieStatisticsDTO(totalMovies, moviesByGenre, moviesByYear, actorsPerMovie);
    }

}
