package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;
import bg.softuni.damapp.service.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementServiceImpl.class);
    private final RestClient advertisementRestClient;

    public AdvertisementServiceImpl(
            @Qualifier("adsRestClient") RestClient advertisementRestClient
    ) {
        this.advertisementRestClient = advertisementRestClient;
    }

    @Override
    public void createAd(CreateAdDTO createAdDTO) {
        LOGGER.info("Creating new advertisement...");

        CreateAdDTO adDTO = new CreateAdDTO(
                createAdDTO.getTitle(), createAdDTO.getDescription(),
                createAdDTO.getCategory(), createAdDTO.getQuantity(),
                createAdDTO.getLocation(), createAdDTO.getReserved(),
                createAdDTO.getType(), createAdDTO.getImageUrls()
        );

        advertisementRestClient
                .post()
                .uri("http://localhost:8081/ads")
                .body(adDTO)
                .retrieve();
    }

    @Override
    public List<AdSummaryDTO> getAllAds() {
        LOGGER.info("Get all advertisements...");

        return advertisementRestClient
                .get()
                .uri("http://localhost:8081/ads")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
