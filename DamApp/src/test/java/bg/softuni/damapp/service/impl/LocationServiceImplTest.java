package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.LocationDTO;
import bg.softuni.damapp.model.entity.GeoName;
import bg.softuni.damapp.model.entity.GeoNamesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yaml")
public class LocationServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private LocationServiceImpl locationService;

    @Test
    public void shouldReturnEmptyListIfResponseIsNull() {
        given(restTemplate.getForObject(anyString(), any(Class.class))).willReturn(null);

        List<LocationDTO> locations = locationService.getLocations("Sofia");

        assertThat(locations).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListIfGeonamesListIsEmpty() {
        GeoNamesResponse response = new GeoNamesResponse();
        response.setGeonames(new ArrayList<>());
        given(restTemplate.getForObject(anyString(), any(Class.class))).willReturn(response);

        List<LocationDTO> locations = locationService.getLocations("Plovdiv");

        assertThat(locations).isEmpty();
    }

    @Test
    public void shouldReturnFilteredLocations() {
        List<GeoName> geonames = List.of(
                new GeoName().setName("Дрѣново"),
                new GeoName().setName("Кюстендѝлъ"),
                new GeoName().setName("Plovdiv"),
                new GeoName().setName("Велико Търново")
        );
        GeoNamesResponse response = new GeoNamesResponse();
        response.setGeonames(geonames);
        given(restTemplate.getForObject(anyString(), any(Class.class))).willReturn(response);

        List<LocationDTO> locations = locationService.getLocations("Plovdiv");

        assertThat(locations).hasSize(2);
        assertThat(locations.get(0).getName()).isEqualTo("Plovdiv");
        assertThat(locations.get(1).getName()).isEqualTo("Велико Търново");
    }


    @Test
    public void shouldCallRestTemplateWithCorrectUrl() {
        GeoNamesResponse mockResponse = new GeoNamesResponse();
        mockResponse.setGeonames(new ArrayList<>());
        given(restTemplate.getForObject(anyString(), eq(GeoNamesResponse.class)))
                .willReturn(mockResponse);

        locationService.getLocations("Sofia");

        verify(restTemplate).getForObject(
                "http://api.geonames.org/searchJSON?formatted=true&name_startsWith=Sofia" +
                        "&country=BG&maxRows=6&lang=bg&username=testUsername" +
                        "&country=BG&featureClass=P",
                GeoNamesResponse.class);
    }
}


