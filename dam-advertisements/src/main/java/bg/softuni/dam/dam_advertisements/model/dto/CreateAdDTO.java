package bg.softuni.dam.dam_advertisements.model.dto;

import bg.softuni.dam.dam_advertisements.model.enums.AdType;
import bg.softuni.dam.dam_advertisements.model.enums.Category;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateAdDTO(
        String title,
        String description,
        Category category,
        Integer quantity,
        String location,
        Boolean reserved,
        AdType type,
        List<String> imageUrls,
        String contactPhone,
        LocalDateTime publishedAt,
        UUID ownerId
) {
    public CreateAdDTO {
        publishedAt = LocalDateTime.now();
    }
}
