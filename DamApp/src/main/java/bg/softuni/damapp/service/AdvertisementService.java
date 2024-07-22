package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;

import java.util.List;
import java.util.UUID;

public interface AdvertisementService {
    void createAd(CreateAdDTO createAdDTO);

    AdDetailsDTO getAdDetails(UUID id);

    List<AdSummaryDTO> getAllAds();

    List<AdDetailsDTO> getMyAds(UUID ownerId);

    List<AdSummaryDTO> getAdsByCategory(String category);

    void reserveAdvertisement(UUID id, UUID userId);

    void unreserveAdvertisement(UUID adId, UUID userId);

    void deleteAdvertisement(UUID adId, UUID userId);

    //    Page<AdSummaryDTO> findAll(Pageable pageable);
}
