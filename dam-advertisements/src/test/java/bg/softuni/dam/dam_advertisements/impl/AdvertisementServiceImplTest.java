package bg.softuni.dam.dam_advertisements.impl;

import bg.softuni.dam.dam_advertisements.model.dto.AdvertisementDTO;
import bg.softuni.dam.dam_advertisements.model.dto.CreateAdDTO;
import bg.softuni.dam.dam_advertisements.model.entity.Advertisement;
import bg.softuni.dam.dam_advertisements.model.entity.Image;
import bg.softuni.dam.dam_advertisements.model.enums.AdType;
import bg.softuni.dam.dam_advertisements.model.enums.Category;
import bg.softuni.dam.dam_advertisements.repository.AdvertisementRepository;
import bg.softuni.dam.dam_advertisements.service.exception.AccessDeniedException;
import bg.softuni.dam.dam_advertisements.service.exception.ObjectNotFoundException;
import bg.softuni.dam.dam_advertisements.service.exception.ResourceNotFoundException;
import bg.softuni.dam.dam_advertisements.service.impl.AdvertisementServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceImplTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AdvertisementServiceImpl advertisementService;

    private Advertisement advertisement;
    private AdvertisementDTO advertisementDTO;
    private CreateAdDTO createAdDTO;
    private UUID otherUserId;

    @BeforeEach
    void setUp() {
        otherUserId = UUID.randomUUID();

        UUID id = UUID.randomUUID();
        UUID ownerID = UUID.randomUUID();

        advertisement = new Advertisement();
        advertisement.setId(id);
        advertisement.setTitle("Title");
        advertisement.setDescription("Description");
        advertisement.setCategory(Category.ПЛОДОВЕ);
        advertisement.setQuantity(1000);
        advertisement.setLocation("Location");
        advertisement.setReserved(false);
        advertisement.setType(AdType.ПОДАРЯВА);
        advertisement.setContactPhone("+359887899885");
        advertisement.setPublishedAt(LocalDateTime.now());
        advertisement.setOwnerId(ownerID);
        Image image = new Image();
        image.setUrl("http://example.com/image.jpg");
        advertisement.setImageUrls(Collections.singletonList(image));


        advertisementDTO = new AdvertisementDTO(
                advertisement.getId(),
                "Title",
                "Description",
                Category.ПЛОДОВЕ,
                1000,
                "Location",
                false,
                AdType.ПОДАРЯВА,
                Collections.singletonList("http://example.com/image.jpg"),
                "+359887899885",
                LocalDateTime.now(),
                ownerID);

        createAdDTO = new CreateAdDTO(
                "Title",
                "Description",
                Category.ПЛОДОВЕ,
                1000,
                "Location",
                false,
                AdType.ПОДАРЯВА,
                List.of("http://example.com/image.jpg"),
                "+359887899885",
                LocalDateTime.now(),
                ownerID);
    }

    private LocalDateTime roundToMinute(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
    }

    private void assertAdvertisementDTOsEqual(AdvertisementDTO expected, AdvertisementDTO actual) {
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.title(), actual.title());
        assertEquals(expected.description(), actual.description());
        assertEquals(expected.category(), actual.category());
        assertEquals(expected.quantity(), actual.quantity());
        assertEquals(expected.location(), actual.location());
        assertEquals(expected.reserved(), actual.reserved());
        assertEquals(expected.type(), actual.type());
        assertEquals(expected.imageUrls(), actual.imageUrls());
        assertEquals(expected.contactPhone(), actual.contactPhone());
        assertEquals(roundToMinute(expected.publishedAt()), roundToMinute(actual.publishedAt()));
        assertEquals(expected.ownerId(), actual.ownerId());
    }

    @Test
    void createAd_shouldReturnAdvertisementDTO() {
        when(advertisementRepository.save(any(Advertisement.class))).thenAnswer(invocation -> {
            Advertisement ad = invocation.getArgument(0);
            ad.setId(UUID.randomUUID());
            return ad;
        });

        AdvertisementDTO result = advertisementService.createAd(createAdDTO);

        assertNotNull(result);

        assertEquals(createAdDTO.title(), result.title());
        assertEquals(createAdDTO.description(), result.description());
        assertEquals(createAdDTO.category(), result.category());
        assertEquals(createAdDTO.quantity(), result.quantity());
        assertEquals(createAdDTO.location(), result.location());
        assertEquals(createAdDTO.reserved(), result.reserved());
        assertEquals(createAdDTO.type(), result.type());
        assertEquals(createAdDTO.imageUrls(), result.imageUrls());
        assertEquals(createAdDTO.contactPhone(), result.contactPhone());
        assertEquals(createAdDTO.publishedAt(), result.publishedAt());
        assertEquals(createAdDTO.ownerId(), result.ownerId());

        verify(advertisementRepository, times(1)).save(any(Advertisement.class));
    }

    @Test
    void getAdById_shouldReturnAdvertisementDTO_whenAdvertisementExists() {
        UUID id = advertisement.getId();

        when(advertisementRepository.findById(id)).thenReturn(Optional.of(advertisement));

        AdvertisementDTO result = advertisementService.getAdById(id);

        assertNotNull(result);
        assertEquals(advertisementDTO.id(), result.id());
        assertAdvertisementDTOsEqual(advertisementDTO, result);

        verify(advertisementRepository, times(1)).findById(id);
    }

    @Test
    void getAdById_shouldThrowException_whenAdvertisementDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(advertisementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> advertisementService.getAdById(id));
        verify(advertisementRepository, times(1)).findById(id);
    }

    @Test
    void getAllAds_shouldReturnListOfAdvertisementDTOs() {
        when(advertisementRepository.findAll()).thenReturn(Collections.singletonList(advertisement));

        List<AdvertisementDTO> result = advertisementService.getAllAds();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertAdvertisementDTOsEqual(advertisementDTO, result.get(0));
        verify(advertisementRepository, times(1)).findAll();
    }

    @Test
    void getAdvertisementsByOwnerId_shouldReturnListOfAdvertisementDTOs() {
        UUID ownerId = advertisementDTO.ownerId();
        when(advertisementRepository.findByOwnerId(ownerId)).thenReturn(Collections.singletonList(advertisement));

        List<AdvertisementDTO> result = advertisementService.getAdvertisementsByOwnerId(ownerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertAdvertisementDTOsEqual(advertisementDTO, result.get(0));
        verify(advertisementRepository, times(1)).findByOwnerId(ownerId);
    }

    @Test
    void getAdsByCategory_shouldReturnListOfAdvertisementDTOs() {
        Category category = advertisementDTO.category();
        when(advertisementRepository.findByCategory(category)).thenReturn(Collections.singletonList(advertisement));

        List<AdvertisementDTO> result = advertisementService.getAdsByCategory(category);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertAdvertisementDTOsEqual(advertisementDTO, result.get(0));
        verify(advertisementRepository, times(1)).findByCategory(category);
    }

    @Test
    void reserveAdvertisement_shouldSetReservedTrue_whenOwnerIdMatches() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.of(advertisement));

        advertisementService.reserveAdvertisement(advertisement.getId(), advertisement.getOwnerId());

        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, times(1)).save(advertisement);
        assertTrue(advertisement.isReserved());
    }

    @Test
    void reserveAdvertisement_shouldThrowAccessDeniedException_whenOwnerIdDoesNotMatch() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.of(advertisement));

        assertThrows(AccessDeniedException.class, () -> advertisementService.reserveAdvertisement(advertisement.getId(), otherUserId));
        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, never()).save(any(Advertisement.class));
    }

    @Test
    void reserveAdvertisement_shouldThrowResourceNotFoundException_whenAdvertisementDoesNotExist() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> advertisementService.reserveAdvertisement(advertisement.getId(), advertisement.getOwnerId()));
        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, never()).save(any(Advertisement.class));
    }

    @Test
    void unreserveAdvertisement_shouldSetReservedFalse_whenOwnerIdMatches() {
        advertisement.setReserved(true);
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.of(advertisement));

        advertisementService.unreserveAdvertisement(advertisement.getId(), advertisement.getOwnerId());

        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, times(1)).save(advertisement);
        assertFalse(advertisement.isReserved());
    }

    @Test
    void unreserveAdvertisement_shouldThrowAccessDeniedException_whenOwnerIdDoesNotMatch() {
        advertisement.setReserved(true);
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.of(advertisement));

        assertThrows(AccessDeniedException.class, () -> advertisementService.unreserveAdvertisement(advertisement.getId(), otherUserId));
        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, never()).save(any(Advertisement.class));
    }

    @Test
    void unreserveAdvertisement_shouldThrowResourceNotFoundException_whenAdvertisementDoesNotExist() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> advertisementService.unreserveAdvertisement(advertisement.getId(), advertisement.getOwnerId()));
        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, never()).save(any(Advertisement.class));
    }

    @Test
    void deleteAdvertisement_shouldDelete_whenOwnerIdMatches() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.of(advertisement));
        doNothing().when(restTemplate).delete(any(String.class), any(String.class));

        advertisementService.deleteAdvertisement(advertisement.getId(), advertisement.getOwnerId());

        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, times(1)).delete(advertisement);
        verify(restTemplate, times(1)).delete(any(String.class), any(String.class));
    }

    @Test
    void deleteAdvertisement_shouldThrowAccessDeniedException_whenOwnerIdDoesNotMatch() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.of(advertisement));

        assertThrows(AccessDeniedException.class, () -> advertisementService.deleteAdvertisement(advertisement.getId(), otherUserId));
        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, never()).delete(any(Advertisement.class));
        verify(restTemplate, never()).delete(any(String.class), any(String.class));
    }

    @Test
    void deleteAdvertisement_shouldThrowResourceNotFoundException_whenAdvertisementDoesNotExist() {
        when(advertisementRepository.findById(advertisement.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> advertisementService.deleteAdvertisement(advertisement.getId(), advertisement.getOwnerId()));
        verify(advertisementRepository, times(1)).findById(advertisement.getId());
        verify(advertisementRepository, never()).delete(any(Advertisement.class));
        verify(restTemplate, never()).delete(any(String.class), any(String.class));
    }
}