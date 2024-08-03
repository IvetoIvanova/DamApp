package bg.softuni.damapp.model.entity;

import bg.softuni.damapp.validation.annotations.UUIDSequence;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.*;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;
    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private boolean isActive;
    @ElementCollection
    @CollectionTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "advertisement_id")
    private Set<UUID> favouriteAdvertisementIds;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRole> roles;

    public User() {
        this.favouriteAdvertisementIds = new HashSet<>();
        this.roles = new ArrayList<>();
        this.isActive = true;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public User setRoles(List<UserRole> roles) {
        this.roles = roles;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<UUID> getFavouriteAdvertisementIds() {
        return favouriteAdvertisementIds;
    }

    public void setFavouriteAdvertisementIds(Set<UUID> favouriteAdvertisementIds) {
        this.favouriteAdvertisementIds = favouriteAdvertisementIds;
    }
}

