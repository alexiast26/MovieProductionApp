package movie_app.notification_service.domain;

import java.time.LocalDateTime;

public class Notification {
    private Integer id;
    private Integer userId;
    private String message;
    private LocalDateTime timestamp;
    private String status;
    private Contact contactType;

    public Notification(Integer id, Integer userId, String message, LocalDateTime timestamp, String status, Contact contactType) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.contactType = contactType;
    }

    public Notification(Integer userId, String message, Contact contactType) {
        this.id = null;
        this.userId = userId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = "SENT";
        this.contactType = contactType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Contact getContactType() {
        return contactType;
    }

    public void setContactType(Contact contactType) {
        this.contactType = contactType;
    }
}
