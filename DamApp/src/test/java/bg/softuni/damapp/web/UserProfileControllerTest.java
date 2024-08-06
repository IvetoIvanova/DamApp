package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private ConversationService conversationService;

    @MockBean
    private MessageService messageService;

    @Test
    public void showProfile_ShouldReturnMyProfileView() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/my-profile")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/my-profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("userData", userDTO));
    }

    @Test
    public void logoutSuccess_ShouldReturnProfileDeletedView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/my-profile/profile-deleted"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profile-deleted"));
    }

    @Test
    public void updateEmail_ShouldRedirectToMyProfile() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        doNothing().when(userService).updateEmail(userDTO.getId(), "new@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/update-email")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER"))
                        .param("email", "new@example.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/my-profile?unreadMessageCount=0"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Имейлът е променен успешно."));
    }

    @Test
    public void updateEmail_ShouldRedirectToMyProfileWithError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        doThrow(new IllegalArgumentException("Invalid email")).when(userService).updateEmail(userDTO.getId(), "new@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/update-email")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER"))
                        .param("email", "new@example.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/my-profile?unreadMessageCount=0"))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", "Invalid email"));
    }

    @Test
    public void updatePassword_ShouldRedirectToMyProfile() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        doNothing().when(userService).updatePassword(userDTO.getId(), "currentPass", "newPass");

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/update-password")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER"))
                        .param("currentPassword", "currentPass")
                        .param("newPassword", "newPass"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/my-profile?unreadMessageCount=0"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Паролата е променена успешно."));
    }

    @Test
    public void updatePassword_ShouldRedirectToMyProfileWithError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        doThrow(new IllegalArgumentException("Invalid password")).when(userService).updatePassword(userDTO.getId(), "currentPass", "newPass");

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/update-password")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER"))
                        .param("currentPassword", "currentPass")
                        .param("newPassword", "newPass"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/my-profile?unreadMessageCount=0"))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", "Invalid password"));
    }

    @Test
    public void deleteAccount_ShouldReturnProfileDeletedView() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        when(advertisementService.getMyAds(userDTO.getId())).thenReturn(Collections.emptyList());
        when(messageService.getUnreadMessageCount(userDTO.getId())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/delete-account")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER"))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profile-deleted"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("error"))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", nullValue()))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", nullValue()));
    }

    @Test
    public void deleteAccount_ShouldRedirectToMyProfileWithError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        doThrow(new IllegalArgumentException("Unable to delete account")).when(userService).deleteUser(userDTO.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/delete-account")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@example.com").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/my-profile?unreadMessageCount=0"))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", "Unable to delete account"));
    }
}
