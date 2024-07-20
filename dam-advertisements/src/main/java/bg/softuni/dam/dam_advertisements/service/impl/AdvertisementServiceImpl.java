package bg.softuni.dam.dam_advertisements.service.impl;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;
import bg.softuni.dam.dam_advertisements.model.entity.Advertisement;
import bg.softuni.dam.dam_advertisements.model.entity.Image;
import bg.softuni.dam.dam_advertisements.repository.AdvertisementRepository;
import bg.softuni.dam.dam_advertisements.service.AdvertisementService;
import bg.softuni.dam.dam_advertisements.service.exception.AccessDeniedException;
import bg.softuni.dam.dam_advertisements.service.exception.ObjectNotFoundException;
import bg.softuni.dam.dam_advertisements.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final RestTemplate restTemplate;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository, RestTemplate restTemplate) {
        this.advertisementRepository = advertisementRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public AdvertisementDTO createAd(CreateAdDTO createAdDTO) {
        Advertisement advertisement = advertisementRepository.save(map(createAdDTO));
        return map(advertisement);
    }

    @Override
    public AdvertisementDTO getAdById(Long id) {
        return advertisementRepository
                .findById(id)
                .map(AdvertisementServiceImpl::map)
                .orElseThrow(ObjectNotFoundException::new);
    }

    @Override
    public List<AdvertisementDTO> getAllAds() {
        return advertisementRepository
                .findAll()
                .stream()
                .map(AdvertisementServiceImpl::map)
                .toList();
    }

    @Override
    public List<AdvertisementDTO> getAdvertisementsByOwnerId(Long ownerId) {
        return advertisementRepository
                .findByOwnerId(ownerId)
                .stream()
                .map(AdvertisementServiceImpl::map)
                .toList();
    }

    @Override
    public void reserveAdvertisement(Long id, Long userId) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(id);
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();
            if (advertisement.getOwnerId().equals(userId)) {
                advertisement.setReserved(true);
                advertisementRepository.save(advertisement);
            } else {
                throw new AccessDeniedException("Вие не сте собственикът на тази обява!");
            }
        } else {
            throw new ResourceNotFoundException("Обявата не е намерена.");
        }
    }

    @Override
    public void unreserveAdvertisement(Long id, Long userId) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(id);
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();
            if (advertisement.getOwnerId().equals(userId)) {
                advertisement.setReserved(false);
                advertisementRepository.save(advertisement);
            } else {
                throw new AccessDeniedException("Вие не сте собственикът на тази обява!");
            }
        } else {
            throw new ResourceNotFoundException("Обявата не е намерена.");
        }
    }

    @Override
    public void deleteAdvertisement(Long id, Long userId) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(id);
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();
            if (advertisement.getOwnerId().equals(userId) || isAdmin(userId)) {
                // TODO: deleting photos from the cloud
                List<String> imageUrls = advertisement
                        .getImageUrls()
                        .stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList());
                if (!imageUrls.isEmpty()) {
                    try {
                        restTemplate.delete("http://localhost:8080/cloud-storage/delete-images?imageUrls={imageUrls}", String.join(",", imageUrls));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                advertisementRepository.delete(advertisement);
            } else {
                throw new AccessDeniedException("Нямате право да изтриете тази обява.");
            }
        } else {
            throw new ResourceNotFoundException("Обявата не е намерена.");
        }
    }

    private boolean isAdmin(Long userId) {
        // TODO: checking if the user is an admin
        return false;
    }

    private static AdvertisementDTO map(Advertisement advertisement) {
        List<String> imageUrls = advertisement.getImageUrls().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return new AdvertisementDTO(
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCategory(),
                advertisement.getQuantity(),
                advertisement.getLocation(),
                advertisement.isReserved(),
                advertisement.getType(),
                imageUrls,
                advertisement.getContactPhone(),
                advertisement.getPublishedAt(),
                advertisement.getOwnerId()
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
        advertisement.setContactPhone(createAdDTO.contactPhone());
        advertisement.setPublishedAt(createAdDTO.publishedAt());
        advertisement.setOwnerId(createAdDTO.ownerId());

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
