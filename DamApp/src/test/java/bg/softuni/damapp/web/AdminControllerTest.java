package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.RoleAlreadyExistsException;
import bg.softuni.damapp.exception.RoleDoesNotExistsException;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.RoleService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private MessageService messageService;

    @Test
    public void testAdminDashboard() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());
        when(roleService.findAllRolesOfUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users", "allRoles"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", Collections.emptyList()))
                .andExpect(MockMvcResultMatchers.model().attribute("allRoles", Collections.emptyList()));
    }

    @Test
    public void addRoleToUser_ShouldRedirectOnSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        String roleName = "USER";

        User user = new User();
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{userId}/roles", userId)
                        .param("role", roleName)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        verify(roleService).addRoleToUser(user, UserRoleEnum.valueOf(roleName));
    }

    @Test
    public void addRoleToUser_ShouldRedirectWithErrorMessageWhenRoleExists() throws Exception {
        UUID userId = UUID.randomUUID();
        String roleName = "USER";

        when(userService.findById(userId)).thenReturn(Optional.of(new User()));
        doThrow(new RoleAlreadyExistsException("Role already exists")).when(roleService).addRoleToUser(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{userId}/roles", userId)
                        .param("role", roleName)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("errorMessage", "Role already exists"));
    }

    @Test
    public void removeRoleFromUser_ShouldRedirectOnSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        String roleName = "USER";

        User user = new User();
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{userId}/remove-role", userId)
                        .param("role", roleName)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        verify(roleService).removeRoleFromUser(user, UserRoleEnum.valueOf(roleName));
    }

    @Test
    public void removeRoleFromUser_ShouldRedirectWithErrorMessageWhenRoleDoesNotExist() throws Exception {
        UUID userId = UUID.randomUUID();
        String roleName = "USER";

        when(userService.findById(userId)).thenReturn(Optional.of(new User()));
        doThrow(new RoleDoesNotExistsException("Role does not exist")).when(roleService).removeRoleFromUser(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{userId}/remove-role", userId)
                        .param("role", roleName)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("errorMessage", "Role does not exist"));
    }

    @Test
    public void disableUser_ShouldRedirectWithSuccessMessage() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{userId}/disable", userId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", "Потребителят е деактивиран."));

        verify(userService).disableUser(userId);
    }

    @Test
    public void enableUser_ShouldRedirectWithSuccessMessage() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{userId}/enable", userId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", "Потребителят е активиран."));

        verify(userService).enableUser(userId);
    }
}
