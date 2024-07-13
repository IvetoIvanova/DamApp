package bg.softuni.dam.dam_advertisements.repository;

import bg.softuni.dam.dam_advertisements.model.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
