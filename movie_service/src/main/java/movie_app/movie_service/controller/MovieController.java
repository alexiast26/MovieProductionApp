package movie_app.movie_service.controller;

import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.domain.Movie;
import movie_app.movie_service.domain.dto.MovieDTO;
import movie_app.movie_service.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies(@RequestParam(required = false) String sortBy) {
        List<Movie> movies = movieService.getAllMovies(sortBy);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody MovieDTO movieDto) {
        Movie movieToSave = movieDto.toDomain();
        Movie savedMovie = movieService.addMovie(movieToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody MovieDTO movieDto) {
        Movie movieToUpdate = movieDto.toDomain();
        Movie updatedMovie = movieService.updateMovie(id, movieToUpdate);
        if (updatedMovie != null) {
            return ResponseEntity.ok(updatedMovie);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{movieId}/actors/{actorId}")
    public ResponseEntity<Movie> addActor(@PathVariable Integer movieId, @PathVariable Integer actorId) {
        movieService.addActorToMovie(movieId, actorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}/actors/{actorId}")
    public ResponseEntity<Movie> removeActor(@PathVariable Integer movieId, @PathVariable Integer actorId) {
        movieService.removeActorFromMovie(movieId, actorId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{movieId}/directors/{directorId}")
    public ResponseEntity<Movie> addDirector(@PathVariable Integer movieId, @PathVariable Integer directorId) {
        movieService.addDirectorToMovie(movieId, directorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}/directors/{directorId}")
    public ResponseEntity<Movie> removeDirector(@PathVariable Integer movieId, @PathVariable Integer directorId) {
        movieService.removeDirectorFromMovie(movieId, directorId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{movieId}/screenwriters/{screenwriterId}")
    public ResponseEntity<Movie> addScreenwriter(@PathVariable Integer movieId, @PathVariable Integer screenwriterId) {
        movieService.addScreenwriterToMovie(movieId, screenwriterId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}/screenwriters/{screenwriterId}")
    public ResponseEntity<Movie> removeScreenwriter(@PathVariable Integer movieId, @PathVariable Integer screenwriterId) {
        movieService.removeScreenwriterFromMovie(movieId, screenwriterId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{movieId}/images")
    public ResponseEntity<Void> addImageToMovie(@PathVariable Integer movieId, @RequestBody String imageUrl) {
        movieService.addImageToMovie(movieId, imageUrl);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}/images")
    public ResponseEntity<Void> removeImageFromMovie(@PathVariable Integer movieId, @RequestBody String imageUrl) {
        movieService.removeImageFromMovie(movieId, imageUrl);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actor/{actorId}")
    public ResponseEntity<List<Movie>> getMoviesByActorId(@PathVariable Integer actorId) {
        List<Movie> movies = movieService.getMoviesByActorId(actorId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/director/{directorId}")
    public ResponseEntity<List<Movie>> getMoviesByDirectorId(@PathVariable Integer directorId) {
        List<Movie> movies = movieService.getMoviesByDirectorId(directorId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/screenwriter/{screenwriterId}")
    public ResponseEntity<List<Movie>> getMoviesByScreenwriterId(@PathVariable Integer screenwriterId) {
        List<Movie> movies = movieService.getMoviesByScreenwriterId(screenwriterId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies(
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Category category) {

        List<Movie> movies = movieService.searchMovies(releaseYear, genre, category);

        return ResponseEntity.ok(movies);
    }


}
