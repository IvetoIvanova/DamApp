package bg.softuni.dam.dam_advertisements.web;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;
import bg.softuni.dam.dam_advertisements.model.enums.Category;
import bg.softuni.dam.dam_advertisements.service.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);
    private final AdvertisementService advertisementService;

    public AdsController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> getById(@PathVariable("id") UUID id) {
        return ResponseEntity
                .ok(advertisementService.getAdById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AdvertisementDTO> deleteById(@PathVariable("id") UUID id, @RequestParam("userId") UUID userId) {
        advertisementService.deleteAdvertisement(id, userId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDTO>> getAllAds() {
        return ResponseEntity.ok(
                advertisementService.getAllAds()
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<AdvertisementDTO>> getAdsByCategory(@PathVariable("category") String category) {
        LOGGER.info("Received category: " + category);
        return ResponseEntity.ok(advertisementService.getAdsByCategory(Category.valueOf(category)));
    }

    @GetMapping("/user/{ownerId}")
    public ResponseEntity<List<AdvertisementDTO>> getAdvertisementsByOwnerId(@PathVariable("ownerId") UUID ownerId) {
        List<AdvertisementDTO> ads = advertisementService.getAdvertisementsByOwnerId(ownerId);
        return ResponseEntity.ok(ads);
    }

    @PostMapping("/reserve/{id}")
    public ResponseEntity<Void> reserveAdvertisement(@PathVariable("id") UUID id, @RequestParam("userId") UUID userId) {
        advertisementService.reserveAdvertisement(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unreserve/{id}")
    public ResponseEntity<Void> unreserveAdvertisement(@PathVariable("id") UUID id, @RequestParam("userId") UUID userId) {
        advertisementService.unreserveAdvertisement(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable UUID id, @RequestParam("userId") UUID userId) {
        advertisementService.deleteAdvertisement(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<AdvertisementDTO> createAdvertisement(@RequestBody CreateAdDTO createAdDTO) {
        LOGGER.info("Going to create an ad {}", createAdDTO);

        AdvertisementDTO advertisementDTO = advertisementService.createAd(createAdDTO);

        return ResponseEntity.
                created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(advertisementDTO.id())
                                .toUri()
                ).body(advertisementDTO);
    }

}
