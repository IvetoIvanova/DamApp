package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static bg.softuni.damapp.util.Constants.NOT_EMPTY;

public class CreateAdDTO {
    @NotBlank(message = NOT_EMPTY)
    @Size(min = 2, max = 50, message = "Заглавието трябва да е между 2 и 50 знака.")
    private String title;
    @NotBlank(message = NOT_EMPTY)
    @Size(min = 10, max = 255, message = "Описанието трябва да е между 10 и 255 знака.")
    private String description;
    @NotNull(message = NOT_EMPTY)
    private Category category;
    @NotNull(message = NOT_EMPTY)
    @Min(value = 1, message = "Минималното количество е 1.")
    private Integer quantity;
    @NotNull(message = NOT_EMPTY)
    private String location;
    @NotNull(message = NOT_EMPTY)
    private Boolean reserved = false;
    @NotNull(message = NOT_EMPTY)
    private AdType type;
    @NotBlank(message = NOT_EMPTY)
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$",
            message = "Форматът на телефонния номер е невалиден. " +
                    "Трябва да съдържа само цифри," +
                    " интервали, скоби, тирета и точки, и да бъде между 7 и 25 знака.")
    @NotBlank(message = NOT_EMPTY)
    private String contactPhone;
    private LocalDateTime publishedAt;
    @NotNull(message = NOT_EMPTY)
    private List<String> imageUrls = new ArrayList<>();
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
