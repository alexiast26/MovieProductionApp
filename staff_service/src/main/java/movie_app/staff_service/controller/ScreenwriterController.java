package movie_app.staff_service.controller;

import movie_app.staff_service.domain.Screenwriter;
import movie_app.staff_service.domain.dto.RequestDTO;
import movie_app.staff_service.service.ScreenwriterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff/screenwriters")
public class ScreenwriterController {

    private final ScreenwriterService screenwriterService;

    public ScreenwriterController(ScreenwriterService screenwriterService) {
        this.screenwriterService = screenwriterService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Screenwriter>> getAllScreenwriters() {
        return ResponseEntity.ok(screenwriterService.getAllScreenwriters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screenwriter> getScreenwriterById(@PathVariable Integer id) {
        Optional<Screenwriter> screenwriter = screenwriterService.getScreenwriterById(id);
        return screenwriter.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Screenwriter>> searchScreenwriters(@RequestParam String name) {
        List<Screenwriter> screenwriters = screenwriterService.searchScreenwritersByName(name);
        return ResponseEntity.ok(screenwriters);
    }

    @PostMapping
    public ResponseEntity<Screenwriter> addScreenwriter(@RequestBody RequestDTO screenwriter) {
        Screenwriter saved = screenwriterService.addScreenwriter(new Screenwriter(screenwriter.getName(), screenwriter.getAge(), screenwriter.getGender(), screenwriter.getImage_url()));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Screenwriter> updateScreenwriter(@PathVariable Integer id, @RequestBody RequestDTO screenwriter) {
        Screenwriter updated = screenwriterService.updateScreenwriter(id, new Screenwriter(screenwriter.getName(), screenwriter.getAge(), screenwriter.getGender(), screenwriter.getImage_url()));
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreenwriter(@PathVariable Integer id) {
        if (screenwriterService.deleteScreenwriter(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}