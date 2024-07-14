package bg.softuni.damapp.web.rest;

import bg.softuni.damapp.model.dto.LocationDTO;
import bg.softuni.damapp.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/api/locations")
    public ResponseEntity<List<LocationDTO>> getLocations(@RequestParam String query) {
        List<LocationDTO> locations = locationService.getLocations(query);
        return ResponseEntity.ok(locations);
    }
}
