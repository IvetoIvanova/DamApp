package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.exception.RoleAlreadyExistsException;
import bg.softuni.damapp.exception.RoleDoesNotExistsException;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    private RoleServiceImpl toTest;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private RoleRepository mockRoleRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private User mockUser;

    @Mock
    private UserRole mockRole;

    @BeforeEach
    void setUp() {
        toTest = new RoleServiceImpl(
                mockRoleRepository,
                mockUserRepository
        );
    }

    @Test
    public void testAddRoleToUser_RoleAlreadyExists() {
        UserRoleEnum roleName = UserRoleEnum.USER;
        when(mockRoleRepository.findByRole(roleName)).thenReturn(mockRole);
        when(mockUser.getRoles()).thenReturn(List.of(mockRole));
        when(mockRole.getRole()).thenReturn(roleName);

        Assertions.assertThrows(RoleAlreadyExistsException.class, () -> {
            toTest.addRoleToUser(mockUser, roleName);
        });
    }

    @Test
    public void testAddRoleToUser_Success() {
        UserRoleEnum roleName = UserRoleEnum.USER;
        UserRole newRole = new UserRole();
        newRole.setRole(roleName);

        List<UserRole> roles = new ArrayList<>();
        when(mockRoleRepository.findByRole(roleName)).thenReturn(newRole);
        when(mockUser.getRoles()).thenReturn(roles);
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        toTest.addRoleToUser(mockUser, roleName);

        verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertTrue(savedUser.getRoles().contains(newRole));

        verify(mockRoleRepository).findByRole(roleName);
    }

    @Test
    public void testRemoveRoleFromUser_RoleExists() {
        UserRoleEnum roleName = UserRoleEnum.USER;
        UserRole existingRole = new UserRole();
        existingRole.setRole(roleName);

        List<UserRole> roles = new ArrayList<>();
        roles.add(existingRole);

        when(mockUser.getRoles()).thenReturn(roles);
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        toTest.removeRoleFromUser(mockUser, roleName);

        verify(mockUserRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertFalse(savedUser.getRoles().contains(existingRole), "Role should be removed from user");

        verify(mockRoleRepository, never()).findByRole(roleName);
    }

    @Test
    public void testRemoveRoleFromUser_RoleDoesNotExist() {
        UserRoleEnum roleName = UserRoleEnum.USER;

        when(mockUser.getRoles()).thenReturn(new ArrayList<>());

        Assertions.assertThrows(RoleDoesNotExistsException.class, () -> {
            toTest.removeRoleFromUser(mockUser, roleName);
        });
    }

    @Test
    public void testFindAllRolesOfUser() {
        List<UserRole> roles = List.of(new UserRole(), new UserRole());

        when(mockRoleRepository.findAll()).thenReturn(roles);

        List<UserRole> foundRoles = toTest.findAllRolesOfUser();

        Assertions.assertEquals(roles, foundRoles);
    }
}
