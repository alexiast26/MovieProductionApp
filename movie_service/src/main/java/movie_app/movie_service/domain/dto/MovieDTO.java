package movie_app.movie_service.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.domain.Movie;

@Data
@NoArgsConstructor
public class MovieDTO {
    private String name;
    private int duration;
    private int releaseYear;
    private Genre genre;
    private Category category;

    public Movie toDomain(){
        Movie movie = new Movie();
        movie.setName(name);
        movie.setDuration(duration);
        movie.setReleaseYear(releaseYear);
        movie.setGenre(genre);
        movie.setCategory(category);
        return movie;
    }
}
