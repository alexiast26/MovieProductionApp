package movie_app.movie_service.service;

import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.domain.Movie;
import movie_app.movie_service.domain.dao.IMovieDAO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final IMovieDAO movieDAO;
    private final RestTemplate restTemplate;

    public MovieService(IMovieDAO movieDAO, RestTemplate restTemplate) {
        this.movieDAO = movieDAO;
        this.restTemplate = restTemplate;
    }

    public List<Movie> getAllMovies(String sortBy) {
        List<Movie> movies = movieDAO.getAllMovies();

        if (sortBy == null || sortBy.trim().isEmpty()) {
            return movies;
        }

        if (sortBy.equalsIgnoreCase("category_year")) {
            return movies.stream()
                    .sorted(Comparator.comparing(Movie::getCategory)
                            .thenComparing(Movie::getReleaseYear, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }

        return movies;
    }

    public Optional<Movie> getMovieById(int id) {
        return movieDAO.getMovieById(id);
    }

    public Movie addMovie(Movie movie) {
        return movieDAO.saveMovie(movie);
    }

    public Movie updateMovie(Integer id, Movie movieUpdates) {
        Optional<Movie> existingMovieOpt = movieDAO.getMovieById(id);
        if (existingMovieOpt.isPresent()) {
            Movie existingMovie = existingMovieOpt.get();

            existingMovie.setName(movieUpdates.getName());
            existingMovie.setDuration(movieUpdates.getDuration());
            existingMovie.setReleaseYear(movieUpdates.getReleaseYear());
            existingMovie.setGenre(movieUpdates.getGenre());
            existingMovie.setCategory(movieUpdates.getCategory());

            return movieDAO.saveMovie(existingMovie);
        }
        return null;
    }

    public boolean deleteMovieById(int id) {
        if (movieDAO.getMovieById(id).isPresent()) {
            movieDAO.deleteMovieById(id);
            return true;
        }
        return false;
    }

    public void addActorToMovie(Integer movieId, Integer actorId) {
        validateMovie(movieId);
        //validateStaff("actors", actorId);
        movieDAO.addActorToMovie(movieId, actorId);
    }

    public void removeActorFromMovie(Integer movieId, Integer actorId) {
        movieDAO.removeActorFromMovie(movieId, actorId);
    }

    public void addDirectorToMovie(Integer movieId, Integer directorId) {
        validateMovie(movieId);
        //validateStaff("directors", directorId);
        movieDAO.addDirectorToMovie(movieId, directorId);
    }

    public void removeDirectorFromMovie(Integer movieId, Integer directorId) {
        movieDAO.removeDirectorFromMovie(movieId, directorId);
    }

    public void addScreenwriterToMovie(Integer movieId, Integer screenwriterId) {
        validateMovie(movieId);
        //validateStaff("screenwriters", screenwriterId);
        movieDAO.addScreenwriterToMovie(movieId, screenwriterId);
    }

    public void removeScreenwriterFromMovie(Integer movieId, Integer screenwriterId) {
        movieDAO.removeScreenwriterFromMovie(movieId, screenwriterId);
    }

    public void addImageToMovie(Integer movieId, String imageUrl) {
        validateMovie(movieId);
        movieDAO.addImageToMovie(movieId, imageUrl);
    }

    public void removeImageFromMovie(Integer movieId, String imageUrl) {
        movieDAO.removeImageFromMovie(movieId, imageUrl);
    }

    private void validateMovie(Integer movieId) {
        if (movieDAO.getMovieById(movieId).isEmpty()) {
            throw new RuntimeException("Movie with id " + movieId + " not found");
        }
    }

    private void validateStaff(String staff, Integer staffId) {
        String staffName = "http://staff-service:8083/staff/" + staff + "/" + staffId;
        try{
            restTemplate.getForObject(staffName, String.class);
        }catch(HttpClientErrorException.NotFound ex){
            throw new RuntimeException("Staff with id " + staffId + " not found");
        }catch (Exception ex){
            throw new RuntimeException("Error while validating staff with id " + staffId);
        }
    }

    public List<Movie> getMoviesByActorId(Integer actorId) {
        return movieDAO.getMoviesByActorId(actorId);
    }

    public List<Movie> getMoviesByDirectorId(Integer directorId) {
        return movieDAO.getMoviesByDirectorId(directorId);
    }

    public List<Movie> getMoviesByScreenwriterId(Integer screenwriterId) {
        return movieDAO.getMoviesByScreenwriterId(screenwriterId);
    }

    public List<Movie> searchMovies(Integer releaseYear, Genre genre, Category category) {
        return movieDAO.searchMovies(releaseYear, genre, category);
    }
}
