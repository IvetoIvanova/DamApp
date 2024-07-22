package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;

import java.util.List;

public interface AdvertisementService {
    void createAd(CreateAdDTO createAdDTO);

    AdDetailsDTO getAdDetails(Long id);

    List<AdSummaryDTO> getAllAds();

    List<AdDetailsDTO> getMyAds(Long ownerId);

    List<AdSummaryDTO> getAdsByCategory(String category);

    void reserveAdvertisement(Long id, Long userId);

    void unreserveAdvertisement(Long adId, Long userId);

    void deleteAdvertisement(Long adId, Long userId);

    //    Page<AdSummaryDTO> findAll(Pageable pageable);
}
