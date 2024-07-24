package bg.softuni.damapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "conversations")
public class Conversation extends BaseEntity {
    @Column(name = "sender_id", nullable = false)
    private UUID senderId;
    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;
    @Column(name = "advertisement_id", nullable = false)
    private UUID advertisementId;

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public UUID getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(UUID advertisementId) {
        this.advertisementId = advertisementId;
    }
}
