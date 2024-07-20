package bg.softuni.damapp.web.rest;

import bg.softuni.damapp.service.CloudStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cloud-storage")
public class CloudStorageController {

    private final CloudStorageService cloudStorageService;

    public CloudStorageController(CloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @DeleteMapping("/delete-images")
    public ResponseEntity<Void> deleteImages(@RequestBody List<String> imageUrls) throws IOException {
        cloudStorageService.deleteImages(imageUrls);
        return ResponseEntity.noContent().build();
    }
}
