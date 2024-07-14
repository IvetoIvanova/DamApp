package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getLocations(String query);
}
