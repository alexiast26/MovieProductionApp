package movie_app.movie_service.infrastructure.repository;

import movie_app.movie_service.domain.Category;
import movie_app.movie_service.domain.Genre;
import movie_app.movie_service.infrastructure.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaMovieRepository extends JpaRepository<MovieEntity, Integer> {
    @Query("SELECT m FROM MovieEntity m JOIN m.actorIds a WHERE a = :actorId")
    List<MovieEntity> findMoviesByActorId(@Param("actorId") Integer actorId);

    @Query("SELECT m FROM MovieEntity m JOIN m.directorIds d WHERE d = :directorId")
    List<MovieEntity> findMoviesByDirectorId(@Param("directorId") Integer directorId);

    @Query("SELECT m FROM MovieEntity m JOIN m.screenwriterIds s WHERE s = :screenwriterId")
    List<MovieEntity> findMoviesByScreenwriterId(@Param("screenwriterId") Integer screenwriterId);

    @Query("SELECT m FROM MovieEntity m WHERE " +
            "(:releaseYear IS NULL OR m.releaseYear = :releaseYear) AND " +
            "(:genre IS NULL OR m.genre = :genre) AND " +
            "(:category IS NULL OR m.category = :category)")
    List<MovieEntity> searchMoviesByFilters(
            @Param("releaseYear") Integer releaseYear,
            @Param("genre") Genre genre,
            @Param("category") Category category
    );
}
