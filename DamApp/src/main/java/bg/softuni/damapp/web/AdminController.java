package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.RoleAlreadyExistsException;
import bg.softuni.damapp.exception.RoleDoesNotExistsException;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.RoleService;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final RoleService roleService;

    public AdminController(UserService userService, AdvertisementService advertisementService, RoleService roleService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllUsers();
        List<UserRole> allRoles = roleService.findAllRolesOfUser();
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addRoleToUser(@PathVariable UUID userId,
                                @RequestParam("role") String roleName,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<User> user = userService.findById(userId);
            user.ifPresent(value -> roleService.addRoleToUser(value, UserRoleEnum.valueOf(roleName)));
            return "redirect:/admin";
        } catch (RoleAlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/users/{userId}/remove-role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String removeRoleFromUser(@PathVariable UUID userId,
                                     @RequestParam("role") String roleName,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<User> user = userService.findById(userId);
            user.ifPresent(u -> roleService.removeRoleFromUser(u, UserRoleEnum.valueOf(roleName)));
            return "redirect:/admin";
        } catch (RoleDoesNotExistsException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/users/{userId}/disable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String disableUser(@PathVariable UUID userId,
                              RedirectAttributes redirectAttributes) {
        userService.disableUser(userId);
        String successMessage = "Потребителят е деактивиран.";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admin";
    }

    @PostMapping("/users/{userId}/enable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String enableUser(@PathVariable UUID userId,
                             RedirectAttributes redirectAttributes) {
        userService.enableUser(userId);
        String successMessage = "Потребителят е активиран.";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admin";
    }

//    @DeleteMapping("/delete-ad/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String deleteAd(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//        UserDTO user = userService.findByEmail(email);
//
//        advertisementService.deleteAdvertisement(id, user.getId());
//        return "list-advertisements";
//    }

}
