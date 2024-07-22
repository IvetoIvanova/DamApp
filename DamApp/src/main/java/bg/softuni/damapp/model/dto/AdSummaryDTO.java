package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AdSummaryDTO(
        UUID id,
        String title,
        Category category,
        String location,
        Boolean reserved,
        AdType type,
        List<String> imageUrls,
        LocalDateTime publishedAt
) {
}
