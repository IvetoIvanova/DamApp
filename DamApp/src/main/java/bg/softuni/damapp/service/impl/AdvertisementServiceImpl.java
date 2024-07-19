package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

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
                .uri("http://localhost:8081/ads")
                .body(adDTO)
                .retrieve();
    }

    @Override
    public AdDetailsDTO getAdDetails(Long id) {
        return advertisementRestClient
                .get()
                .uri("http://localhost:8081/ads/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AdDetailsDTO.class);
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

    @Override
    public List<AdDetailsDTO> getMyAds(Long ownerId) {
        LOGGER.info("Get my ads...");

        return advertisementRestClient
                .get()
                .uri("http://localhost:8081/ads/user/" + ownerId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void reserveAdvertisement(Long adId, Long userId) {
        advertisementRestClient
                .post()
                .uri("http://localhost:8081/ads/reserve/{id}?userId={userId}", adId, userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void unreserveAdvertisement(Long adId, Long userId) {
        advertisementRestClient
                .post()
                .uri("http://localhost:8081/ads/unreserve/{id}?userId={userId}", adId, userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

//    @Override
//    public void deleteAd(long adId) {
////        advertisementRepository.deleteById(adId);
//    }

}
