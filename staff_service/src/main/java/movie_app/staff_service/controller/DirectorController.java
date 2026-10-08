package movie_app.staff_service.controller;

import movie_app.staff_service.domain.Director;
import movie_app.staff_service.domain.dto.RequestDTO;
import movie_app.staff_service.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Director>> getAllDirectors() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable Integer id) {
        Optional<Director> director = directorService.getDirectorById(id);
        return director.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Director>> searchDirectors(@RequestParam String name) {
        List<Director> directors = directorService.searchDirectorsByName(name);
        return ResponseEntity.ok(directors);
    }

    @PostMapping
    public ResponseEntity<Director> addDirector(@RequestBody RequestDTO director) {
        Director savedDirector = directorService.addDirector(new Director(director.getName(), director.getAge(), director.getGender(), director.getImage_url()));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDirector);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Director> updateDirector(@PathVariable Integer id, @RequestBody RequestDTO director) {
        Director updated = directorService.updateDirector(id, new Director(director.getName(), director.getAge(), director.getGender(), director.getImage_url()));
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable Integer id) {
        if (directorService.deleteDirector(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}