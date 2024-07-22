package bg.softuni.dam.dam_advertisements.repository;

import bg.softuni.dam.dam_advertisements.model.entity.Advertisement;
import bg.softuni.dam.dam_advertisements.model.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByOwnerId(Long ownerId);

    List<Advertisement> findByCategory(Category category);
}
