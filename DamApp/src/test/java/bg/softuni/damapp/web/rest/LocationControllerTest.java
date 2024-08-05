package bg.softuni.damapp.web.rest;

import bg.softuni.damapp.config.TestConfig;
import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.model.dto.LocationDTO;
import bg.softuni.damapp.service.LocationService;
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

import static org.mockito.Mockito.when;

@WebMvcTest(LocationController.class)
@Import({TestConfig.class, TestSecurityConfig.class})
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @Test
    public void getLocations_ShouldReturnListOfLocations() throws Exception {
        LocationDTO location1 = new LocationDTO("New York");
        LocationDTO location2 = new LocationDTO("San Francisco");
        List<LocationDTO> locations = List.of(location1, location2);

        when(locationService.getLocations("query")).thenReturn(locations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations")
                        .param("query", "query")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("New York"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("San Francisco"));
    }
}
