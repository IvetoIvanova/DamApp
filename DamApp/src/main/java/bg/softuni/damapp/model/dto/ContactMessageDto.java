package bg.softuni.damapp.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ContactMessageDto {
    private UUID id;
    private String name;
    private String email;
    private String message;
    private LocalDateTime timestamp;
    private List<ReplyContactMessageDto> replies;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<ReplyContactMessageDto> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyContactMessageDto> replies) {
        this.replies = replies;
    }
}
