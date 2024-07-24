package bg.softuni.damapp.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

    @Column(nullable = false, length = 500)
    private String content;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @JoinColumn(nullable = false)
    @ManyToOne
    private User sender;
    @JoinColumn(nullable = false)
    @ManyToOne
    private User recipient;
    @JoinColumn(nullable = false)
    private UUID advertisementId;
    @Column(nullable = false)
    private boolean isRead;
    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public UUID getAdvertisement() {
        return advertisementId;
    }

    public void setAdvertisement(UUID advertisement) {
        this.advertisementId = advertisement;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }
}
