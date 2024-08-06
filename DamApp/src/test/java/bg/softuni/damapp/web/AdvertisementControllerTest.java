package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.CreateAdDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.CloudStorageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdvertisementControllerTest {

    @InjectMocks
    private AdvertisementController advertisementController;

    @Mock
    private UserService userService;

    @Mock
    private AdvertisementService advertisementService;

    @Mock
    private CloudStorageService cloudStorageService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowAddAdvertisementForm() {
        String viewName = advertisementController.showAddAdvertisementForm(mock(Model.class));

        assertEquals("advertisement-add", viewName);
    }

    @Test
    public void testCreateAdSuccess() throws IOException {
        CreateAdDTO createAdDTO = new CreateAdDTO();
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://example.com/image.jpg");

        when(cloudStorageService.uploadImage(any())).thenReturn("http://example.com/image.jpg");
        when(principal.getName()).thenReturn("user@example.com");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);

        String viewName = advertisementController.createAd(new MultipartFile[] {}, createAdDTO, bindingResult, redirectAttributes, principal);

        assertEquals("redirect:/list-advertisements", viewName);
        verify(advertisementService).createAd(createAdDTO);
    }


    @Test
    public void testCreateAdWithValidationErrors() {
        CreateAdDTO createAdDTO = new CreateAdDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = advertisementController.createAd(new MultipartFile[] {}, createAdDTO, bindingResult, redirectAttributes, principal);

        assertEquals("redirect:/advertisement-add", viewName);
    }
}