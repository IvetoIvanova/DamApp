package bg.softuni.damapp.web.rest;

import bg.softuni.damapp.config.TestConfig;
import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.service.CloudStorageService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CloudStorageController.class)
@Import({TestConfig.class, TestSecurityConfig.class})
public class CloudStorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudStorageService cloudStorageService;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @Test
    public void deleteImages_ShouldReturnNoContent() throws Exception {
        List<String> imageUrls = List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg");

        doNothing().when(cloudStorageService).deleteImages(imageUrls);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cloud-storage/delete-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteImages_ShouldHandleIOException() throws Exception {
        List<String> imageUrls = List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg");

        doNothing().when(cloudStorageService).deleteImages(imageUrls);

        mockMvc.perform(delete("/cloud-storage/delete-images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]"))
                .andExpect(status().isNoContent());
    }
}
