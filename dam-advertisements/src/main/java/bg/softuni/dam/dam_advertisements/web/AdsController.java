package bg.softuni.dam.dam_advertisements.web;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;
import bg.softuni.dam.dam_advertisements.service.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);
    private final AdvertisementService advertisementService;

    public AdsController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> findById(@PathVariable("id") Long id) {
//        advertisementService.getAdById(id);
        return ResponseEntity
                .ok(advertisementService.getAdById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> deleteById(@PathVariable("id") Long id) {
        advertisementService.deleteAd(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDTO>> getAllAds() {
        return ResponseEntity.ok(
                advertisementService.getAllAds()
        );
    }

    @PostMapping
    public ResponseEntity<AdvertisementDTO> createAdvertisement(@RequestBody CreateAdDTO createAdDTO) {
        LOGGER.info("Going to create an ad {}", createAdDTO);

        advertisementService.createAd(createAdDTO);
        return ResponseEntity.ok().build();
    }

}
