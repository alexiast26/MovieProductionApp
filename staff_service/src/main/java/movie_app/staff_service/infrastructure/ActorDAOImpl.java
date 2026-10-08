package movie_app.staff_service.infrastructure;

import movie_app.staff_service.domain.Actor;
import movie_app.staff_service.domain.dao.IActorDAO;
import movie_app.staff_service.infrastructure.repository.JpaActorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Component
public class ActorDAOImpl implements IActorDAO {
    private final JpaActorRepository jpaActorRepository;

    public ActorDAOImpl(JpaActorRepository jpaActorRepository) {
        this.jpaActorRepository = jpaActorRepository;
    }

    @Override
    public List<Actor> getAllActors() {
        return jpaActorRepository.findAll().stream().map(ActorEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Actor> getActorById(Integer id) {
        return jpaActorRepository.findById(id).map(ActorEntity::toDomain);
    }

    @Override
    public List<Actor> getActorByName(String name) {
        return jpaActorRepository.findByNameContainingIgnoreCase(name).stream().map(ActorEntity::toDomain).toList();
    }

    @Override
    public Actor saveActor(Actor actor) {
        ActorEntity entityToSave = new ActorEntity(actor);
        ActorEntity savedEntity = jpaActorRepository.save(entityToSave);
        return savedEntity.toDomain();
    }

    @Override
    public void deleteActorById(Integer id) {
        jpaActorRepository.deleteById(id);
    }
}
