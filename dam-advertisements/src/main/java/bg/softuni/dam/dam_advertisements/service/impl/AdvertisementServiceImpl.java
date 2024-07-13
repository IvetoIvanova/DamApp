package bg.softuni.dam.dam_advertisements.service.impl;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;
import bg.softuni.dam.dam_advertisements.model.entity.Advertisement;
import bg.softuni.dam.dam_advertisements.model.entity.Image;
import bg.softuni.dam.dam_advertisements.repository.AdvertisementRepository;
import bg.softuni.dam.dam_advertisements.service.AdvertisementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public void createAd(CreateAdDTO createAdDTO) {
        advertisementRepository.save(map(createAdDTO));
    }

    @Override
    public List<AdvertisementDTO> getAllAds() {
        return advertisementRepository
                .findAll()
                .stream()
                .map(AdvertisementServiceImpl::map)
                .toList();
    }

    private static AdvertisementDTO map(Advertisement advertisement) {
        List<String> imageUrls = advertisement.getImageUrls().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return new AdvertisementDTO(
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCategory(),
                advertisement.getQuantity(),
                advertisement.getLocation(),
                advertisement.isReserved(),
                advertisement.getType(),
                imageUrls
        );
    }

    private static Advertisement map(CreateAdDTO createAdDTO) {

        Advertisement advertisement = new Advertisement();

        advertisement.setTitle(createAdDTO.title());
        advertisement.setDescription(createAdDTO.description());
        advertisement.setCategory(createAdDTO.category());
        advertisement.setQuantity(createAdDTO.quantity());
        advertisement.setLocation(createAdDTO.location());
        advertisement.setReserved(createAdDTO.reserved());
        advertisement.setType(createAdDTO.type());

        List<Image> images = createAdDTO.imageUrls().stream()
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    image.setAdvertisement(advertisement);
                    return image;
                })
                .collect(Collectors.toList());

        advertisement.setImageUrls(images);

        return advertisement;
    }
}
