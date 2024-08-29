package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.dto.UserRegisterDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.model.user.DamUserDetails;
import bg.softuni.damapp.repository.RoleRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static bg.softuni.damapp.util.Constants.PASSWORD_REGEX;
import static bg.softuni.damapp.util.Constants.PASSWORD_STRENGTH;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        User user = map(userRegisterDTO);
        UserRole userRole = roleRepository.findByRole(UserRoleEnum.USER);
        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Потребителят не е намерен."));
        return convertToDTO(user);
    }

    @Override
    public Optional<DamUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.getPrincipal() instanceof DamUserDetails damUserDetails) {
            return Optional.of(damUserDetails);
        }
        return Optional.empty();
    }

    @Override
    public void updateEmail(UUID userId, String newEmail) {
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Потребител с такъв имейл вече съществува в базата.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Потребителят не е намерен."));
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Потребителят не е намерен."));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Текущата парола не е вярна.");
        }

        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException(PASSWORD_STRENGTH);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Потребителят не е намерен."));
        userRepository.delete(user);
    }

    @Override
    public void disableUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Потребителят не е намерен."));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Потребителят не е намерен."));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID ownerId) {
        return userRepository.findById(ownerId);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    private User map(UserRegisterDTO userRegisterDTO) {
        User mappedEntity = modelMapper.map(userRegisterDTO, User.class);

        mappedEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        return mappedEntity;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoles(user.getRoles()
                .stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toList()));
        userDTO.setActive(user.isActive());
        return userDTO;
    }
}
