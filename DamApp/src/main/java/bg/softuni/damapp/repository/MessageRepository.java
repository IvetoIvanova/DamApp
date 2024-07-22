package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    int deleteByCreatedDateBefore(LocalDate date);
}
