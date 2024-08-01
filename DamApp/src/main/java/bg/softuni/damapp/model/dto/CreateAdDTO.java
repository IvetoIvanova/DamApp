package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import bg.softuni.damapp.validation.annotations.NoImage;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateAdDTO {
    @NotBlank(message = "{validation.not_empty}")
    @Size(min = 2, max = 50, message = "{validation.title.size}")
    private String title;
    @NotBlank(message = "{validation.not_empty}")
    @Size(min = 10, max = 255, message = "{validation.description.size}")
    private String description;
    @NotNull(message = "{validation.not_empty}")
    private Category category;
    @NotNull(message = "{validation.not_empty}")
    @Min(value = 1, message = "{validation.quantity.min}")
    private Integer quantity;
    @NotNull(message = "{validation.not_empty}")
    private String location;
    private Boolean reserved = false;
    @NotNull(message = "{validation.not_empty}")
    private AdType type;
    @NotBlank(message = "{validation.not_empty}")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$",
            message = "{validation.phone.format_invalid}")
    @NotBlank(message = "{validation.not_empty}")
    private String contactPhone;
    private LocalDateTime publishedAt;
    private List<String> imageUrls = new ArrayList<>();
    @NoImage
    private List<MultipartFile> images = new ArrayList<>();
    private UUID ownerId;

    public CreateAdDTO() {
        this.publishedAt = LocalDateTime.now();
    }

    public CreateAdDTO(String title, String description, Category category, Integer quantity,
                       String location, Boolean reserved, AdType type, List<String> imageUrls,
                       List<MultipartFile> images, String contactPhone, LocalDateTime publishedAt) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.location = location;
        this.reserved = reserved;
        this.type = type;
        this.imageUrls = imageUrls;
        this.images = images;
        this.contactPhone = contactPhone;
        this.publishedAt = publishedAt;
    }

    public CreateAdDTO(String title, String description, Category category, Integer quantity,
                       String location, Boolean reserved, AdType type, List<String> imageUrls,
                       String contactPhone, LocalDateTime publishedAt, UUID ownerId) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.location = location;
        this.reserved = reserved;
        this.type = type;
        this.imageUrls = imageUrls;
        this.contactPhone = contactPhone;
        this.publishedAt = publishedAt;
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}
