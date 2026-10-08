package movie_app.movie_service.infrastructure;

import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.domain.Movie;
import movie_app.movie_service.domain.dao.IMovieDAO;
import movie_app.movie_service.infrastructure.repository.JpaMovieRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovieDAOImpl implements IMovieDAO {
    private final JpaMovieRepository jpaMovieRepository;

    public MovieDAOImpl(JpaMovieRepository jpaMovieRepository) {
        this.jpaMovieRepository = jpaMovieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return jpaMovieRepository.findAll().stream().map(MovieEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> getMovieById(Integer id) {
        return jpaMovieRepository.findById(id).map(MovieEntity::toDomain);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        MovieEntity movieEntitytoSave = new MovieEntity(movie);
        return jpaMovieRepository.save(movieEntitytoSave).toDomain();
    }

    @Override
    public void deleteMovieById(Integer id) {
        jpaMovieRepository.deleteById(id);
    }

    @Override
    public List<Movie> searchMovies(Integer releaseYear, Genre genre, Category category) {
        return jpaMovieRepository.searchMoviesByFilters(releaseYear, genre, category)
                .stream()
                .map(MovieEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void addActorToMovie(Integer movieId, Integer actorId) {
        jpaMovieRepository.findById(movieId).ifPresent(movieEntity -> {
            if (!movieEntity.getActorIds().contains(actorId)) {
                movieEntity.getActorIds().add(actorId);
                jpaMovieRepository.save(movieEntity);
            }
        });
    }

    @Override
    public void removeActorFromMovie(Integer movieId, Integer actorId) {
        jpaMovieRepository.findById(movieId).ifPresent(movieEntity -> {
            movieEntity.getActorIds().remove(actorId);
            jpaMovieRepository.save(movieEntity);
        });
    }

    @Override
    public void addDirectorToMovie(Integer movieId, Integer directorId) {
        jpaMovieRepository.findById(movieId).ifPresent(movie -> {
            if (!movie.getDirectorIds().contains(directorId)) {
                movie.getDirectorIds().add(directorId);
                jpaMovieRepository.save(movie);
            }
        });
    }

    @Override
    public void removeDirectorFromMovie(Integer movieId, Integer directorId) {
        jpaMovieRepository.findById(movieId).ifPresent(movie -> {
            movie.getDirectorIds().remove(directorId);
            jpaMovieRepository.save(movie);
        });
    }

    @Override
    public void addScreenwriterToMovie(Integer movieId, Integer screenwriterId) {
        jpaMovieRepository.findById(movieId).ifPresent(movie -> {
            if (!movie.getScreenwriterIds().contains(screenwriterId)) {
                movie.getScreenwriterIds().add(screenwriterId);
                jpaMovieRepository.save(movie);
            }
        });
    }

    @Override
    public void removeScreenwriterFromMovie(Integer movieId, Integer screenwriterId) {
        jpaMovieRepository.findById(movieId).ifPresent(movie -> {
            movie.getScreenwriterIds().remove(screenwriterId);
            jpaMovieRepository.save(movie);
        });
    }

    @Override
    public void addImageToMovie(Integer movieId, String imageUrl) {
        jpaMovieRepository.findById(movieId).ifPresent(movie -> {
            if (!movie.getImageUrls().contains(imageUrl)) {
                movie.getImageUrls().add(imageUrl);
                jpaMovieRepository.save(movie);
            }
        });
    }

    @Override
    public void removeImageFromMovie(Integer movieId, String imageUrl) {
        jpaMovieRepository.findById(movieId).ifPresent(movie -> {
            movie.getImageUrls().remove(imageUrl);
            jpaMovieRepository.save(movie);
        });
    }

    @Override
    public List<Movie> getMoviesByActorId(Integer actorId) {
        return jpaMovieRepository.findMoviesByActorId(actorId)
                .stream()
                .map(MovieEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getMoviesByDirectorId(Integer directorId) {
        return jpaMovieRepository.findMoviesByDirectorId(directorId)
                .stream()
                .map(MovieEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getMoviesByScreenwriterId(Integer screenwriterId) {
        return jpaMovieRepository.findMoviesByScreenwriterId(screenwriterId)
                .stream()
                .map(MovieEntity::toDomain)
                .collect(Collectors.toList());
    }
}
