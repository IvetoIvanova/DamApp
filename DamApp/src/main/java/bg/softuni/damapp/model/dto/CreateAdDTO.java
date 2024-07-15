package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static bg.softuni.damapp.util.Constants.NOT_EMPTY;

public class CreateAdDTO {
    @NotBlank(message = NOT_EMPTY)
    @Size(min = 2, max = 100, message = "Заглавието трябва да е между 2 и 100 знака.")
    private String title;
    @NotBlank(message = NOT_EMPTY)
    @Size(min = 10, max = 1000, message = "Описанието трябва да е между 10 и 1000 знака.")
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
    @NotNull(message = NOT_EMPTY)
    private List<String> imageUrls = new ArrayList<>();
    private List<MultipartFile> images = new ArrayList<>();

    public CreateAdDTO() {
    }

    public CreateAdDTO(String title, String description, Category category, Integer quantity, String location, Boolean reserved, AdType type, List<String> imageUrls, List<MultipartFile> images) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.location = location;
        this.reserved = reserved;
        this.type = type;
        this.imageUrls = imageUrls;
        this.images=images;
    }

    public CreateAdDTO(String title, String description, Category category, Integer quantity, String location, Boolean reserved, AdType type, List<String> imageUrls) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.location = location;
        this.reserved = reserved;
        this.type = type;
        this.imageUrls = imageUrls;
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
}
