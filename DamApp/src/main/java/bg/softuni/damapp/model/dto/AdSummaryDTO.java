package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;

import java.util.List;

public record AdSummaryDTO(
        Long id,
        String title,
        Category category,
        String location,
        Boolean reserved,
        AdType type,
        List<String> imageUrls
) {
//    TODO:Date creationDate;
}
