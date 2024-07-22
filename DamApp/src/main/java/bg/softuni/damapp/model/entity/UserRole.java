package bg.softuni.damapp.model.entity;

import bg.softuni.damapp.model.enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "roles")
public class UserRole {


    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "BINARY(16)",
            unique = true,
            updatable = false,
            nullable = false)
    private UUID id;

    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public UserRole() {
    }

    public UserRole(UserRoleEnum userRole) {
        super();

        this.role = userRole;
    }

    public UUID getId() {
        return id;
    }

    public UserRole setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public UserRole setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }
}
