package bg.softuni.dam.dam_advertisements.service;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;

import java.util.List;

public interface AdvertisementService {

    AdvertisementDTO createAd(CreateAdDTO createAdDTO);

    void deleteAd(Long adId);

    AdvertisementDTO getAdById(Long id);

    List<AdvertisementDTO> getAllAds();

    List<AdvertisementDTO> getAdvertisementsByOwnerId(Long ownerId);

//    boolean deleteAdvertisement(Long id, Long userId);
}
