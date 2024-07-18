package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;

import java.time.LocalDateTime;
import java.util.List;

public record AdDetailsDTO(
        Long id,
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
        Long ownerId
) {
}
