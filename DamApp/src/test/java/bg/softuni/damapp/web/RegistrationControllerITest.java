package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import bg.softuni.damapp.validation.validators.UniqueEmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(RegistrationController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class RegistrationControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UniqueEmailValidator uniqueEmailValidator;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("registerData"));
    }

    @Test
    public void testDoRegister_ValidInput() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(true);
        when(userService.isEmailUnique(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("firstName", "First")
                        .param("lastName", "Last")
                        .param("email", "test@example.com")
                        .param("password", "Password123!")
                        .param("confirmPassword", "Password123!")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    public void testFailedRegistrationWithErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("firstName", "")
                        .param("lastName", "Last")
                        .param("email", "")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .with(csrf())) // Добавяне на CSRF токен
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.registerData"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("registerData"));
    }
}
