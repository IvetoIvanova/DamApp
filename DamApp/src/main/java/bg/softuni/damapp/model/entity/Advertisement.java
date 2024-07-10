package bg.softuni.damapp.model.entity;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advertisements")
public class Advertisement extends BaseEntity {

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private boolean reserved = false;
    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private AdType type;
    @ManyToOne
    private User addedBy;
    @OneToMany(mappedBy = "advertisement")
    private List<Image> images;

    public Advertisement() {
        this.images = new ArrayList<>();
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

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String city) {
        this.location = city;
    }
}

