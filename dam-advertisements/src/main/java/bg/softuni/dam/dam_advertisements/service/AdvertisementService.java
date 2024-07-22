package bg.softuni.dam.dam_advertisements.service;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;
import bg.softuni.dam.dam_advertisements.model.enums.Category;

import java.util.List;
import java.util.UUID;

public interface AdvertisementService {

    AdvertisementDTO createAd(CreateAdDTO createAdDTO);

    AdvertisementDTO getAdById(UUID id);

    List<AdvertisementDTO> getAllAds();

    List<AdvertisementDTO> getAdvertisementsByOwnerId(UUID ownerId);

    List<AdvertisementDTO> getAdsByCategory(Category category);

    void reserveAdvertisement(UUID id, UUID userId);

    void unreserveAdvertisement(UUID id, UUID userId);

    void deleteAdvertisement(UUID id, UUID userId);

//    boolean deleteAdvertisement(UUID id, UUID userId);
}
