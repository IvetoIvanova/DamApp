package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdvertisementService advertisementService;

    public AdminController(UserService userService, AdvertisementService advertisementService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllUsers();
        List<UserRole> allRoles = userService.findAllRolesOfUser();
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @PostMapping("/users/{username}/add-role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addRoleToUser(@PathVariable String username, @RequestParam UserRole role) {
        userService.addRoleToUser(username, role);
        return "redirect:/admin";
    }

    @PostMapping("/users/{username}/remove-role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String removeRoleFromUser(@PathVariable String username, @RequestParam UserRoleEnum role) {
        userService.removeRoleFromUser(username, role);
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUser(@PathVariable UUID id) {
        Optional<User> user = userService.findById(id);
        user.ifPresent(value -> userService.deleteUser(value.getEmail()));
        return "redirect:/admin";
    }

    @DeleteMapping("/delete-ad/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAd(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDTO user = userService.findByEmail(email);

        advertisementService.deleteAdvertisement(id, user.getId());
        return "list-advertisements";
    }

}
