package com.movie_app.users_service.service.observer;

import com.movie_app.users_service.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationObserverImpl implements UserObserver {
    private final RestTemplate restTemplate;
    private final String notificationUrl = "http://notification-service:8084/api/notifications";

    public NotificationObserverImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void onUserUpdated(User user) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("userId", user.getId());
            request.put("email", user.getEmail());
            request.put("phone", user.getPhone());
            request.put("message", "You account has been updated.");

            restTemplate.postForObject(notificationUrl, request, String.class);
            System.out.println("[OBSERVER] Notification sent " + notificationUrl);

        } catch(Exception e) {
            System.err.println("[OBSERVER] Failed to send notification: " + e.getMessage());
        }
    }
}
