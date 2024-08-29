package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.dto.UserRegisterDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.user.DamUserDetails;
import bg.softuni.damapp.repository.RoleRepository;
import bg.softuni.damapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String TEST_EMAIL = "user@example.com";
    private static final String NOT_EXISTENT_EMAIL = "nonexistent@email.com";
    private UserServiceImpl toTest;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private Authentication mockAuthentication;
    @Mock
    private DamUserDetails mockDamUserDetails;
    @Mock
    private SecurityContext mockSecurityContext;

    @BeforeEach
    void setUp() {
        toTest = new UserServiceImpl(
                new ModelMapper(),
                mockPasswordEncoder,
                mockUserRepository,
                mockRoleRepository
        );
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    void testUserRegistration() {

        UserRegisterDTO userRegistrationDTO =
                new UserRegisterDTO()
                        .setFirstName("Some")
                        .setLastName("One")
                        .setPassword("SomeOne1!")
                        .setEmail(TEST_EMAIL);

        when(mockPasswordEncoder.encode(userRegistrationDTO.getPassword()))
                .thenReturn(userRegistrationDTO.getPassword() + userRegistrationDTO.getPassword());


        toTest.registerUser(userRegistrationDTO);

        verify(mockUserRepository).save(userCaptor.capture());

        User actualSavedEntity = userCaptor.getValue();

        assertNotNull(actualSavedEntity);
        Assertions.assertEquals(userRegistrationDTO.getFirstName(), actualSavedEntity.getFirstName());
        Assertions.assertEquals(userRegistrationDTO.getLastName(), actualSavedEntity.getLastName());
        Assertions.assertEquals(userRegistrationDTO.getPassword() + userRegistrationDTO.getPassword(),
                actualSavedEntity.getPassword());
        Assertions.assertEquals(userRegistrationDTO.getEmail(), actualSavedEntity.getEmail());
    }

    @Test
    public void testIsEmailUnique_UniqueEmail() {
        String uniqueEmail = "unique@email.com";
        when(mockUserRepository.findByEmail(uniqueEmail)).thenReturn(Optional.empty());

        boolean isUnique = toTest.isEmailUnique(uniqueEmail);

        assertTrue(isUnique);
        verify(mockUserRepository).findByEmail(uniqueEmail);
    }

    @Test
    public void testIsEmailUnique_NonUniqueEmail() {
        String nonUniqueEmail = "existing@email.com";
        when(mockUserRepository.findByEmail(nonUniqueEmail)).thenReturn(Optional.of(new User()));

        boolean isUnique = toTest.isEmailUnique(nonUniqueEmail);

        assertFalse(isUnique);
        verify(mockUserRepository).findByEmail(nonUniqueEmail);
    }

    @Test
    public void testFindByEmail_ExistingUser() {
        String existingEmail = TEST_EMAIL;
        User existingUser = new User();
        existingUser.setEmail(existingEmail);
        when(mockUserRepository.findByEmail(existingEmail)).thenReturn(Optional.of(existingUser));

        UserDTO userDTO = toTest.findByEmail(existingEmail);

        assertNotNull(userDTO);
        Assertions.assertEquals(existingEmail, userDTO.getEmail());
        verify(mockUserRepository).findByEmail(existingEmail);
    }

    @Test
    public void testFindByEmail_NonExistingUser() {
        String nonExistingEmail = NOT_EXISTENT_EMAIL;
        when(mockUserRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> toTest.findByEmail(nonExistingEmail));
    }

    @Test
    public void testUpdateEmail_Success() {
        UUID userId = UUID.randomUUID();
        String newEmail = TEST_EMAIL;

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(mockUserRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        toTest.updateEmail(userId, newEmail);

        verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertEquals(newEmail, savedUser.getEmail());
    }


    @Test
    public void testUpdateEmail_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> toTest.updateEmail(userId, TEST_EMAIL));
    }

    @Test
    public void testUpdateEmail_EmailAlreadyExists() {
        UUID userId = UUID.randomUUID();
        String newEmail = TEST_EMAIL;

        when(mockUserRepository.findByEmail(newEmail)).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> toTest.updateEmail(userId, newEmail));
    }

    @Test
    public void testUpdatePassword_Success() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setId(UUID.randomUUID());
        user.setFirstName("First");
        user.setLastName("Last");
        user.setPassword("encodedOldPassword");

        when(mockPasswordEncoder.matches("ОldPassword123@", "encodedOldPassword")).thenReturn(true);
        when(mockPasswordEncoder.encode("newPassword156!")).thenReturn("encodedNewPassword");

        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockUserRepository.save(user)).thenReturn(user);

        toTest.updatePassword(user.getId(), "ОldPassword123@", "newPassword156!");

        verify(mockPasswordEncoder).matches("ОldPassword123@", "encodedOldPassword");
        verify(mockPasswordEncoder).encode("newPassword156!");
        Assertions.assertEquals("encodedNewPassword", user.getPassword());
        verify(mockUserRepository).save(user);
    }

    @Test
    public void testUpdatePassword_IncorrectCurrentPassword() {
        UUID userId = UUID.randomUUID();
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";

        User user = new User();
        user.setId(userId);
        user.setPassword(mockPasswordEncoder.encode("correctPassword"));

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockPasswordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            toTest.updatePassword(userId, currentPassword, newPassword);
        });
    }

    @Test
    public void testUpdatePassword_InvalidNewPassword() {
        UUID userId = UUID.randomUUID();
        String currentPassword = "correctPassword123@";
        String newPassword = "invalidPassword";

        User user = new User();
        user.setId(userId);
        user.setPassword(mockPasswordEncoder.encode(currentPassword));

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockPasswordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            toTest.updatePassword(userId, currentPassword, newPassword);
        });

        Assertions.assertEquals("Паролата трябва да съдържа главна и малка буква от латинската азбука, цифра и специален символ(@$!%*#?&).", thrown.getMessage());
    }

    @Test
    public void testUpdatePassword_UserNotFound() {
        UUID userId = UUID.randomUUID();
        String currentPassword = "correctPassword";
        String newPassword = "newPassword";

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            toTest.updatePassword(userId, currentPassword, newPassword);
        });
    }

    @Test
    public void testDeleteUser_Success() {
        String email = TEST_EMAIL;
        User user = new User();
        user.setEmail(email);
        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        toTest.deleteUser(email);

        verify(mockUserRepository).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        String email = NOT_EXISTENT_EMAIL;
        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> toTest.deleteUser(email));
    }

    @Test
    public void testDisableUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setActive(true);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        toTest.disableUser(userId);

        verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertFalse(savedUser.isActive());
    }

    @Test
    public void testDisableUser_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> toTest.disableUser(userId));
    }

    @Test
    public void testEnableUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setActive(false);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        toTest.enableUser(userId);

        verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertTrue(savedUser.isActive());
    }

    @Test
    public void testEnableUser_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> toTest.enableUser(userId));
    }

    @Test
    public void testFindById_UserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = toTest.findById(userId);

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user, foundUser.get());
    }

    @Test
    public void testFindById_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = toTest.findById(userId);

        Assertions.assertFalse(foundUser.isPresent());
    }

    @Test
    public void testIsValidPassword_ValidPassword() {
        String validPassword = "StrongPassword123!";

        Assertions.assertTrue(toTest.isValidPassword(validPassword));
    }

    @Test
    public void testIsValidPassword_InvalidPassword() {
        String invalidPassword = "weakpassword";

        Assertions.assertFalse(toTest.isValidPassword(invalidPassword));
    }

    @Test
    public void testFindAllUsers_UsersExist() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setActive(true);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setActive(true);

        List<User> users = List.of(user1, user2);

        when(mockUserRepository.findAll()).thenReturn(users);

        List<UserDTO> foundUsers = toTest.findAllUsers();

        Assertions.assertEquals(2, foundUsers.size());

        Assertions.assertEquals(user1.getId(), foundUsers.get(0).getId());
        Assertions.assertEquals(user1.getEmail(), foundUsers.get(0).getEmail());
        Assertions.assertEquals(user1.getFirstName(), foundUsers.get(0).getFirstName());
        Assertions.assertEquals(user1.getLastName(), foundUsers.get(0).getLastName());
        Assertions.assertEquals(user1.isActive(), foundUsers.get(0).isActive());

        Assertions.assertEquals(user2.getId(), foundUsers.get(1).getId());
        Assertions.assertEquals(user2.getEmail(), foundUsers.get(1).getEmail());
        Assertions.assertEquals(user2.getFirstName(), foundUsers.get(1).getFirstName());
        Assertions.assertEquals(user2.getLastName(), foundUsers.get(1).getLastName());
        Assertions.assertEquals(user2.isActive(), foundUsers.get(1).isActive());
    }

    @Test
    public void testGetCurrentUser_UserAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockDamUserDetails);

        Optional<DamUserDetails> result = toTest.getCurrentUser();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(mockDamUserDetails, result.get());
    }

    @Test
    public void testGetCurrentUser_NoAuthentication() {
        when(mockSecurityContext.getAuthentication()).thenReturn(null);

        Optional<DamUserDetails> result = toTest.getCurrentUser();

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testGetCurrentUser_NotDamUserDetails() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(new Object());

        Optional<DamUserDetails> result = toTest.getCurrentUser();

        Assertions.assertFalse(result.isPresent());
    }
}
