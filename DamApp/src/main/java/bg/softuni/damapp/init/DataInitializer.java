package bg.softuni.damapp.init;

import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.repository.RoleRepository;
import bg.softuni.damapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${admin_password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeAdmin();
    }

    private void initializeAdmin() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFirstName("Admin");
            admin.setLastName("Admin");

            UserRole adminRole = roleRepository.findByRole(UserRoleEnum.ADMIN);
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
        }
    }

    private void initializeRoles() {
        if (roleRepository.findAll().isEmpty()) {
            UserRole adminRole = new UserRole(UserRoleEnum.ADMIN);
            UserRole userRole = new UserRole(UserRoleEnum.USER);
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }
}
