package bg.softuni.damapp.validation.validators;

import bg.softuni.damapp.model.dto.UserRegisterDTO;
import bg.softuni.damapp.validation.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDTO> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegisterDTO userRegisterDTO, ConstraintValidatorContext constraintValidatorContext) {
        return userRegisterDTO.getPassword() != null &&
                userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword());
    }
}

