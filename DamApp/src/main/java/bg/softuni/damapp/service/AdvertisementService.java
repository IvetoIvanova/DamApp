package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.CreateAdDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdvertisementService {
    void createAd(CreateAdDTO createAdDTO);

    List<AdSummaryDTO> getAllAds();
//    Page<AdSummaryDTO> findAll(Pageable pageable);
}