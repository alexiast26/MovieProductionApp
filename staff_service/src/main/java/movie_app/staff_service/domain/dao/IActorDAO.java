package movie_app.staff_service.domain.dao;

import movie_app.staff_service.domain.Actor;

import java.util.*;

public interface IActorDAO {
    List<Actor> getAllActors();
    Optional<Actor> getActorById(Integer id);
    List<Actor> getActorByName(String name);
    Actor saveActor(Actor actor);
    void deleteActorById(Integer id);
}
