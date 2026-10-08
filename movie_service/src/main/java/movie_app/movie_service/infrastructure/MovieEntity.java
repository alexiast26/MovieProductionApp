package movie_app.movie_service.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.domain.Movie;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "movie")
@Getter
@Setter
public class MovieEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private int duration;
    private int releaseYear;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "images", joinColumns = @JoinColumn(name = "movieId"))
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private List<String> imageUrls;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "movie_director", joinColumns = @JoinColumn(name = "movieId"))
    @Column(name = "directorId")
    private List<Integer> directorIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "movie_screenwriter", joinColumns = @JoinColumn(name = "movieId"))
    @Column(name = "screenwriterId")
    private List<Integer> screenwriterIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "movie_actor", joinColumns = @JoinColumn(name = "movieId"))
    @Column(name = "actorId")
    private List<Integer> actorIds;

    public MovieEntity() {}

    public MovieEntity(String name, int duration, int releaseYear, Genre genre, Category category, List<String> imageUrls, List<Integer> directorIds, List<Integer> screenwriterIds, List<Integer> actorIds) {
        this.id = null;
        this.name = name;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.category = category;
        this.imageUrls = imageUrls;
        this.directorIds = directorIds;
        this.screenwriterIds = screenwriterIds;
        this.actorIds = actorIds;
    }

    public MovieEntity(Movie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.duration = movie.getDuration();
        this.releaseYear = movie.getReleaseYear();
        this.genre = movie.getGenre();
        this.category = movie.getCategory();
        this.imageUrls = movie.getImageUrls();
        this.directorIds = movie.getDirectorIds();
        this.screenwriterIds = movie.getScreenwriterIds();
        this.actorIds = movie.getActorIds();
    }

    public Movie toDomain() {
        return new Movie(
                this.id,
                this.name,
                this.duration,
                this.releaseYear,
                this.genre,
                this.category,
                this.imageUrls,
                this.directorIds,
                this.screenwriterIds,
                this.actorIds
        );
    }
}