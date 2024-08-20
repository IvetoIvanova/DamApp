package bg.softuni.damapp.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "replies")
public class ReplyContactMessage extends BaseEntity {

    @Column(name = "reply_message", nullable = false)
    private String replyMessage;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "contact_message_id", nullable = false)
    private ContactMessage contactMessage;


    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ContactMessage getContactMessage() {
        return contactMessage;
    }

    public void setContactMessage(ContactMessage contactMessage) {
        this.contactMessage = contactMessage;
    }
}
