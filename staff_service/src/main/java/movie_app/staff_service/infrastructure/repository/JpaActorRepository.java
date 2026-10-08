package movie_app.staff_service.infrastructure.repository;

import movie_app.staff_service.infrastructure.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaActorRepository extends JpaRepository<ActorEntity, Integer> {
    List<ActorEntity> findByNameContainingIgnoreCase(String name);
}
