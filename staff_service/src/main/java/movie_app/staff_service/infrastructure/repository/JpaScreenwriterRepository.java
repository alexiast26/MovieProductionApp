package movie_app.staff_service.infrastructure.repository;

import movie_app.staff_service.infrastructure.ScreenwriterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface JpaScreenwriterRepository extends JpaRepository<ScreenwriterEntity, Integer> {
    List<ScreenwriterEntity> findByNameContainingIgnoreCase(String name);
}
