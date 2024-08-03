package bg.softuni.damapp.service;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;

import java.util.List;
import java.util.UUID;

public interface AdvertisementService {
    void createAd(CreateAdDTO createAdDTO) throws UnauthorizedException;

    AdDetailsDTO getAdDetails(UUID id) throws UnauthorizedException;

    List<AdSummaryDTO> getAllAds();

    List<AdDetailsDTO> getMyAds(UUID ownerId) throws UnauthorizedException;

    List<AdSummaryDTO> getAdsByCategory(String category);

    void reserveAdvertisement(UUID id, UUID userId);

    void unreserveAdvertisement(UUID adId, UUID userId);

    void deleteAdvertisement(UUID adId, UUID userId);

    boolean isOwnerOfAd(UUID userId, UUID advertisementId);

    AdSummaryDTO getAdvertisementById(UUID advertisementId);

    void addFavorite(UUID userId, UUID advertisementId);

    List<AdSummaryDTO> getFavoriteAdvertisements(UUID userId);
}
