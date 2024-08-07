package bg.softuni.damapp.interceptor;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserInterceptorTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @Test
    public void testInterceptorWithValidUser() throws Exception {
        String username = "test@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());

        when(userService.findByEmail(username)).thenReturn(userDTO);
        when(messageService.getUnreadMessageCount(userDTO.getId())).thenReturn(5);

        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint")
                        .principal(() -> username))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Test response"));
    }
}
