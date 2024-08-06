package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.model.user.DamUserDetails;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(HomeController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHomePageWithUserDetails() throws Exception {
        DamUserDetails damUserDetails = Mockito.mock(DamUserDetails.class);
        Mockito.when(damUserDetails.getFullName()).thenReturn("First Last");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(damUserDetails, null);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attribute("welcomeMessage", "First Last"));
    }

    @Test
    public void testHomePageWithoutUserDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("welcomeMessage"));
    }

    @Test
    public void testAboutUsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about-us"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("about-us"));
    }

    @Test
    public void testTermsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/terms"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("terms"));
    }
}