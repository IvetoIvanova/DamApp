package bg.softuni.damapp.validation.annotations;

import bg.softuni.damapp.validation.validators.NoImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoImageValidator.class)
public @interface NoImage {
    String message() default "{validation.no_image}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
