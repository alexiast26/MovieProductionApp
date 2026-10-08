package movie_app.notification_service.controller;

import movie_app.notification_service.domain.dto.NotificationRequest;
import movie_app.notification_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> triggerNotification(@RequestBody NotificationRequest request) {
        try {

            notificationService.dispatchAlerts(
                    request.getUserId(),
                    request.getEmail(),
                    request.getPhone()
            );
            return ResponseEntity.ok("Notifications Sent Successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error when sending: " + e.getMessage());
        }
    }
}