package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.exception.RoleAlreadyExistsException;
import bg.softuni.damapp.exception.RoleDoesNotExistsException;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.repository.RoleRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addRoleToUser(User user, UserRoleEnum roleName) {
        UserRole role = roleRepository.findByRole(roleName);
        if (user.getRoles().stream().anyMatch(r -> r.getRole().equals(roleName))) {
            //            String errorMessage = messageSource.getMessage("role_error", null, LocaleContextHolder.getLocale());
            throw new RoleAlreadyExistsException("User already has this role");
        } else {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void removeRoleFromUser(User user, UserRoleEnum roleName) {
        UserRole roleToRemove = user.getRoles().stream()
                .filter(r -> r.getRole().equals(roleName))
                .findFirst()
                .orElse(null);

        if (roleToRemove != null) {
            user.getRoles().remove(roleToRemove);
            userRepository.save(user);
        } else {
            throw new RoleDoesNotExistsException("User does not have this role.");
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserRole> findAllRolesOfUser() {
        return roleRepository.findAll();
    }
}
