package movie_app.staff_service.service;

import movie_app.staff_service.domain.Actor;
import movie_app.staff_service.domain.dao.IActorDAO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    private final IActorDAO actorDAO;

    public ActorService(IActorDAO actorDAO) {
        this.actorDAO = actorDAO;
    }

    public List<Actor> getAllActors() {
        return actorDAO.getAllActors();
    }

    public Optional<Actor> getActorById(Integer id) {
        return actorDAO.getActorById(id);
    }

    public List<Actor> searchActorsByName(String name) {
        return actorDAO.getActorByName(name);
    }

    public Actor addActor(Actor actor) {
        return actorDAO.saveActor(actor);
    }

    public Actor updateActor(Integer id, Actor actor) {
        Optional<Actor> actorOptional = actorDAO.getActorById(id);
        if (actorOptional.isPresent()) {
            actor.setId(id);
            return actorDAO.saveActor(actor);
        }
        throw new RuntimeException("Actor with id " + id + " not found");
    }

    public boolean deleteActorById(Integer id) {
        if (actorDAO.getActorById(id).isPresent()) {
            actorDAO.deleteActorById(id);
            return true;
        }
        return false;
    }
}
