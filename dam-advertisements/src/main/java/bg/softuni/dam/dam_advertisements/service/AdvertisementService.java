package bg.softuni.dam.dam_advertisements.service;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;

import java.util.List;

public interface AdvertisementService {

    void createAd(CreateAdDTO createAdDTO);

    List<AdvertisementDTO> getAllAds();
}
