package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.UserRegisterDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.repository.RoleRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        UserRole role;
        if (userRepository.count() == 0) {
            // Първият регистриран става АДМИН
            role = roleRepository.findByRole(UserRoleEnum.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
        } else {
            // Всички следващи потребители ще бъдат с роля USER
            role = roleRepository.findByRole(UserRoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException("User role not found"));
        }

        List<UserRole> roles = Collections.singletonList(role);
        user.setRoles(roles);

        userRepository.save(user);
    }
}

