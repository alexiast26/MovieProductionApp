package movie_app.staff_service.infrastructure.repository;

import movie_app.staff_service.infrastructure.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaDirectorRepository extends JpaRepository<DirectorEntity, Integer> {
    List<DirectorEntity> findByNameContainingIgnoreCase(String name);
}
