package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE :advertisementId MEMBER OF u.favouriteAdvertisementIds")
    List<User> findAllByFavoriteAdvertisementId(@Param("advertisementId") UUID advertisementId);
}
