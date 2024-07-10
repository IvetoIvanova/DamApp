package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(UserRoleEnum userRole);
}
