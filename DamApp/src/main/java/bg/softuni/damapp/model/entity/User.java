package bg.softuni.damapp.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;
//    @OneToMany(mappedBy = "addedBy")
//    private List<Advertisement> addedAdvertisements;
    @ManyToMany
    private List<Advertisement> favouriteAdvertisements;

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
//        this.addedAdvertisements = new ArrayList<>();
        this.favouriteAdvertisements = new ArrayList<>();
        this.roles = new ArrayList<>();
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

//    public List<Advertisement> getAddedAdvertisements() {
//        return addedAdvertisements;
//    }
//
//    public void setAddedAdvertisements(List<Advertisement> addedAdvertisements) {
//        this.addedAdvertisements = addedAdvertisements;
//    }

    public List<Advertisement> getFavouriteAdvertisements() {
        return favouriteAdvertisements;
    }

    public void setFavouriteAdvertisements(List<Advertisement> favouriteAdvertisements) {
        this.favouriteAdvertisements = favouriteAdvertisements;
    }
}

