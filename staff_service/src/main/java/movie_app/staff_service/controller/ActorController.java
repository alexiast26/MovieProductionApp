package movie_app.staff_service.controller;

import movie_app.staff_service.domain.Actor;
import movie_app.staff_service.domain.dto.RequestDTO;
import movie_app.staff_service.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/staff/actors")
public class ActorController {
    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Actor>> getAllActors() {
        List<Actor> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Integer id) {
        Optional<Actor> actor = actorService.getActorById(id);
        return actor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Actor> addActor(@RequestBody RequestDTO actor) {
        Actor savedActor = actorService.addActor(new Actor(actor.getName(), actor.getAge(), actor.getGender(), actor.getImage_url()));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedActor);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Actor>> searchActors(@RequestParam String name) {
        List<Actor> actors = actorService.searchActorsByName(name);
        return ResponseEntity.ok(actors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable Integer id, @RequestBody RequestDTO actor) {
        Actor updateActor = actorService.updateActor(id, new Actor(actor.getName(), actor.getAge(), actor.getGender(), actor.getImage_url()));
        if (updateActor != null) {
            return ResponseEntity.ok(updateActor);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        if (actorService.deleteActorById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
