package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactMessage, UUID> {
}
