package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementServiceImpl.class);
    private final RestClient advertisementRestClient;
    private final UserRepository userRepository;

    public AdvertisementServiceImpl(
            @Qualifier("adsRestClient") RestClient advertisementRestClient, UserRepository userRepository
    ) {
        this.advertisementRestClient = advertisementRestClient;
        this.userRepository = userRepository;
    }

    @Override
    public void createAd(CreateAdDTO createAdDTO) {
        LOGGER.info("Creating new advertisement...");

        CreateAdDTO adDTO = new CreateAdDTO(
                createAdDTO.getTitle(), createAdDTO.getDescription(),
                createAdDTO.getCategory(), createAdDTO.getQuantity(),
                createAdDTO.getLocation(), createAdDTO.getReserved(),
                createAdDTO.getType(), createAdDTO.getImageUrls(),
                createAdDTO.getContactPhone(), createAdDTO.getPublishedAt(),
                createAdDTO.getOwnerId()
        );

        advertisementRestClient
                .post()
                .uri("/ads")
                .body(adDTO)
                .retrieve();
    }

    @Override
    public AdDetailsDTO getAdDetails(UUID id) {
        return advertisementRestClient
                .get()
                .uri("/ads/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AdDetailsDTO.class);
    }

    @Override
    public List<AdSummaryDTO> getAllAds() {
        LOGGER.info("Get all advertisements...");

        return advertisementRestClient
                .get()
                .uri("/ads")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<AdDetailsDTO> getMyAds(UUID ownerId) {
        LOGGER.info("Get my ads...");

        return advertisementRestClient
                .get()
                .uri("/ads/user/" + ownerId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<AdSummaryDTO> getAdsByCategory(String category) {
        LOGGER.info("Get advertisements by category: {}", category);

        String encodedCategory;
        encodedCategory = URLDecoder.decode(category, StandardCharsets.UTF_8);

        return advertisementRestClient
                .get()
                .uri("/ads/category/" + encodedCategory)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AdSummaryDTO>>() {
                });
    }

    @Override
    public void reserveAdvertisement(UUID adId, UUID userId) {
        advertisementRestClient
                .post()
                .uri("/ads/reserve/{id}?userId={userId}", adId, userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void unreserveAdvertisement(UUID adId, UUID userId) {
        advertisementRestClient
                .post()
                .uri("/ads/unreserve/{id}?userId={userId}", adId, userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void deleteAdvertisement(UUID adId, UUID userId) {

        advertisementRestClient
                .delete()
                .uri("/ads/delete/{id}?userId={userId}", adId, userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
