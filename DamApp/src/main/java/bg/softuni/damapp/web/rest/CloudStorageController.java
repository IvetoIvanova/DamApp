package bg.softuni.damapp.web.rest;

import bg.softuni.damapp.service.CloudStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cloud-storage")
public class CloudStorageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudStorageController.class);

    private final CloudStorageService cloudStorageService;

    public CloudStorageController(CloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @DeleteMapping("/delete-images")
    public ResponseEntity<Void> deleteImages(@RequestBody List<String> imageUrls) throws IOException {
        LOGGER.info("Получени URL-ове за изтриване: {}", imageUrls);
        cloudStorageService.deleteImages(imageUrls);
        return ResponseEntity.noContent().build();
    }
}
