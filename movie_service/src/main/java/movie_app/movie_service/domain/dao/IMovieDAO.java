package movie_app.movie_service.domain.dao;

import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.domain.Movie;
import java.util.List;
import java.util.Optional;

public interface IMovieDAO {
    List<Movie> getAllMovies();
    Optional<Movie> getMovieById(Integer id);
    Movie saveMovie(Movie movie);
    void deleteMovieById(Integer movieId);
    List<Movie> searchMovies(Integer releaseYear, Genre genre, Category category);

    void addActorToMovie(Integer movieId, Integer actorId);
    void removeActorFromMovie(Integer movieId, Integer actorId);

    void addDirectorToMovie(Integer movieId, Integer directorId);
    void removeDirectorFromMovie(Integer movieId, Integer directorId);

    void addScreenwriterToMovie(Integer movieId, Integer screenwriterId);
    void removeScreenwriterFromMovie(Integer movieId, Integer screenwriterId);

    void addImageToMovie(Integer movieId, String imageUrl);
    void removeImageFromMovie(Integer movieId, String imageUrl);

    List<Movie> getMoviesByActorId(Integer actorId);
    List<Movie> getMoviesByDirectorId(Integer directorId);
    List<Movie> getMoviesByScreenwriterId(Integer screenwriterId);
}
