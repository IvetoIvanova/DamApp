package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.CloudStorageService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AdvertisementController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class AdvertisementControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private CloudStorageService cloudStorageService;

    @MockBean
    private RedirectAttributes redirectAttributes;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private MessageService messageService;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowAddAdvertisementForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/advertisement-add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("advertisement-add"));
    }


    @Test
    public void testCreateAdWithValidationErrors() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/advertisement-add")
                        .file(file)
                        .param("title", "")
                        .param("description", "This is a test ad")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/advertisement-add"));
    }

    @Test
    public void testCreateAdIOException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        when(cloudStorageService.uploadImage(any(MockMultipartFile.class))).thenThrow(new IOException());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/advertisement-add")
                        .file(file)
                        .param("title", "Test Ad")
                        .param("description", "This is a test ad")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/advertisement-add"));
    }

}
