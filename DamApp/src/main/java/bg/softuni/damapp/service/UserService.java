package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.UserRegisterDTO;

public interface UserService {
    void registerUser(UserRegisterDTO userRegister);

    boolean isEmailUnique(String email);
}
