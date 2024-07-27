package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.model.user.DamUserDetails;
import bg.softuni.damapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DamUserDetailsServiceTest {

    private static final String TEST_EMAIL = "user@example.com";
    private static final String NOT_EXISTENT_EMAIL = "noone@example.com";

    private DamUserDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new DamUserDetailsService(
                mockUserRepository
        );
    }

    @Test
    void testLoadUserByUsername_success() {
        User testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("TopSecret123!!");
        testUser.setFirstName("First");
        testUser.setLastName("Last");
        testUser.setRoles(List.of(
                new UserRole().setRole(UserRoleEnum.ADMIN),
                new UserRole().setRole(UserRoleEnum.USER)
        ));

        when(mockUserRepository.findByEmail(TEST_EMAIL))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = toTest.loadUserByUsername(TEST_EMAIL);

        Assertions.assertInstanceOf(DamUserDetails.class, userDetails);

        DamUserDetails damUserDetails = (DamUserDetails) userDetails;

        Assertions.assertEquals(TEST_EMAIL, userDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUser.getFirstName(), damUserDetails.getFirstName());
        Assertions.assertEquals(testUser.getLastName(), damUserDetails.getLastName());
        Assertions.assertEquals(testUser.getFirstName() + " " + testUser.getLastName(),
                damUserDetails.getFullName());

        var expectedRoles = testUser.getRoles().stream().map(UserRole::getRole).map(r -> "ROLE_" + r).toList();
        var actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername(NOT_EXISTENT_EMAIL)
        );
    }
}
