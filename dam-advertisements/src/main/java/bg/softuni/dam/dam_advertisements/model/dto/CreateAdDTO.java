package bg.softuni.dam.dam_advertisements.model.dto;

import bg.softuni.dam.dam_advertisements.model.enums.AdType;
import bg.softuni.dam.dam_advertisements.model.enums.Category;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateAdDTO(
        @NotNull(message = "Това поле е задължително.")
        @Size(min = 2, max = 50, message = "Заглавието трябва да е между 2 и 50 знака.")
        String title,
        @NotNull(message = "Това поле е задължително.")
        @Size(min = 10, max = 255, message = "Описанието трябва да е между 10 и 255 знака.")
        String description,
        @NotNull(message = "Това поле е задължително.") Category category,
        @NotNull(message = "Това поле е задължително.")
        @Min(value = 1, message = "Минималното количество е 1.")
        Integer quantity,
        @NotNull(message = "Това поле е задължително.") String location,
        @NotNull(message = "Това поле е задължително.") Boolean reserved,
        @NotNull(message = "Това поле е задължително.") AdType type,
        @NotNull(message = "Това поле е задължително.") List<String> imageUrls,
        @NotBlank(message = "Това поле е задължително.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Форматът на телефонния номер е невалиден. " +
                "Трябва да съдържа само цифри, интервали, скоби, тирета и точки, и да бъде между 7 и 25 знака.")
        @NotBlank(message = "Това поле е задължително.") String contactPhone,
        @NotNull(message = "Това поле е задължително.")
        LocalDateTime publishedAt,
        UUID ownerId
) {
    public CreateAdDTO {
        publishedAt = LocalDateTime.now();
    }
}
