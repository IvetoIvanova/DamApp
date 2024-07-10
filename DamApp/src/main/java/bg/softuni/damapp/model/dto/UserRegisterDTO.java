package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.validation.annotations.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static bg.softuni.damapp.util.Constants.NOT_EMPTY;
import static bg.softuni.damapp.util.Constants.*;

@PasswordMatches
public class UserRegisterDTO {
    @NotBlank(message = NOT_EMPTY)
    private String firstName;
    @NotBlank(message = NOT_EMPTY)
    private String lastName;
    @NotBlank(message = NOT_EMPTY)
    @Email(message = INVALID_EMAIL)
    private String email;
    @NotBlank(message = NOT_EMPTY)
    @Size(min = 8, message = PASSWORD_SIZE)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_STRENGTH)
    private String password;
    @NotBlank(message = NOT_EMPTY)
    private String confirmPassword;

    public UserRegisterDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

}

