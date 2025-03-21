package bg.softuni.damapp.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contacts")
public class ContactMessage extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @OneToMany(mappedBy = "contactMessage", fetch = FetchType.EAGER)
    private List<ReplyContactMessage> replies;

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

    public List<ReplyContactMessage> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyContactMessage> replies) {
        this.replies = replies;
    }
}
