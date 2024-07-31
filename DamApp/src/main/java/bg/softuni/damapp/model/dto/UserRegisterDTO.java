package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.validation.annotations.PasswordMatches;
import bg.softuni.damapp.validation.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static bg.softuni.damapp.util.Constants.*;

@PasswordMatches
public class UserRegisterDTO {
    @NotBlank(message = "{validation.not_empty}")
    private String firstName;
    @NotBlank(message = "{validation.not_empty}")
    private String lastName;
    @NotBlank(message = "{validation.not_empty}")
    @Email(message = "{validation.invalid_email}")
    @UniqueEmail
    private String email;
    @NotBlank(message = "{validation.not_empty}")
    @Size(min = 8, message = "{validation.password_size}")
    @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password_strength}")
    private String password;
    @NotBlank(message = "{validation.not_empty}")
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

