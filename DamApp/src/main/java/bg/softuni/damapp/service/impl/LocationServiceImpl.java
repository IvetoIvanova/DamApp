package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.LocationDTO;
import bg.softuni.damapp.model.entity.GeoNamesResponse;
import bg.softuni.damapp.service.LocationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Value("${geonames.username}")
    private String geoNamesUsername;

    private final RestTemplate restTemplate;

    public LocationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<LocationDTO> getLocations(String query) {
        String url = "http://api.geonames.org/searchJSON?formatted=true&name_startsWith="
                + query
                + "&country=BG&maxRows=6&lang=bg&username="
                + geoNamesUsername
                + "&country=BG&featureClass=P";
        GeoNamesResponse response = restTemplate.getForObject(url, GeoNamesResponse.class);

        if (response == null || response.getGeonames() == null) {
            return new ArrayList<>();
        }

        return response.getGeonames().stream()
                .map(geoName -> new LocationDTO(geoName.getName()))
                .filter(locationDTO -> isModernName(locationDTO.getName()))
                .collect(Collectors.toList());
    }

    private boolean isModernName(String name) {
        return name != null && name.matches("^[А-Яа-яA-Za-z\\s]+$");
    }
}
