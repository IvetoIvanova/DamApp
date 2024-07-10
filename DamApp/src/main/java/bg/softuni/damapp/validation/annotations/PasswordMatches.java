package bg.softuni.damapp.validation.annotations;

import bg.softuni.damapp.validation.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static bg.softuni.damapp.util.Constants.PASSWORD_MISMATCH;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
    String message() default PASSWORD_MISMATCH;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
