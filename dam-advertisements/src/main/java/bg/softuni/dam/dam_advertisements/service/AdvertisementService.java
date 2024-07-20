package bg.softuni.dam.dam_advertisements.service;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;

import java.util.List;

public interface AdvertisementService {

    AdvertisementDTO createAd(CreateAdDTO createAdDTO);

    AdvertisementDTO getAdById(Long id);

    List<AdvertisementDTO> getAllAds();

    List<AdvertisementDTO> getAdvertisementsByOwnerId(Long ownerId);

    void reserveAdvertisement(Long id, Long userId);

    void unreserveAdvertisement(Long id, Long userId);

    void deleteAdvertisement(Long id, Long userId);

//    boolean deleteAdvertisement(Long id, Long userId);
}
