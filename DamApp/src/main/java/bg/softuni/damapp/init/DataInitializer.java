package bg.softuni.damapp.init;

import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            UserRole adminRole = new UserRole(UserRoleEnum.ADMIN);
            UserRole userRole = new UserRole(UserRoleEnum.USER);
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
        }
    }
}
