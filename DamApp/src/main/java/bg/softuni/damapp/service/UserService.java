package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.dto.UserRegisterDTO;
import bg.softuni.damapp.model.user.DamUserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void registerUser(UserRegisterDTO userRegister);

    boolean isEmailUnique(String email);

    UserDTO findByEmail(String email);

    Optional<DamUserDetails> getCurrentUser();

    void updateEmail(UUID userId, String newEmail);

    void updatePassword(UUID userId, String currentPassword, String newPassword);

    void deleteUser(String email);
}
