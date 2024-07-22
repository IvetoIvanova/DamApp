package bg.softuni.dam.dam_advertisements.model.dto;

import bg.softuni.dam.dam_advertisements.model.enums.AdType;
import bg.softuni.dam.dam_advertisements.model.enums.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AdvertisementDTO(
        UUID id,
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
}
