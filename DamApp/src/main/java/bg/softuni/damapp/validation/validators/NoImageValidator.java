package bg.softuni.damapp.validation.validators;

import bg.softuni.damapp.validation.annotations.NoImage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NoImageValidator implements ConstraintValidator<NoImage, List<MultipartFile>> {
    private String message;
    private final static int MAX_IMAGES = 6;

    @Override
    public void initialize(NoImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {

        if (files.size() > MAX_IMAGES) {
            context.buildConstraintViolationWithTemplate("{validation.max_images}")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
