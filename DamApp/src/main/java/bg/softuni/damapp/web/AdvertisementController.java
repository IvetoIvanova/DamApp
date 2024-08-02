package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.CreateAdDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.CloudStorageService;
import bg.softuni.damapp.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/advertisement-add")
public class AdvertisementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementController.class);
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final CloudStorageService cloudStorageService;

    public AdvertisementController(UserService userService, AdvertisementService advertisementService, CloudStorageService cloudStorageService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.cloudStorageService = cloudStorageService;
    }

    @ModelAttribute("advertisementData")
    public CreateAdDTO createAdDTO() {
        return new CreateAdDTO();
    }

    @GetMapping
    public String showAddAdvertisementForm(Model model) {
        return "advertisement-add";
    }

    @PostMapping
    public String createAd(@RequestParam("images") MultipartFile[] images,
                           @Valid CreateAdDTO createAdDTO,
                           BindingResult bindingResult,
                           RedirectAttributes rAtt,
                           Principal principal) throws UnauthorizedException {


        LOGGER.info("CreateAdDTO: {}", createAdDTO);
        LOGGER.info("BindingResult has errors: {}", bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.advertisementData", bindingResult);
            rAtt.addFlashAttribute("advertisementData", createAdDTO);
            return "redirect:/advertisement-add";
        }

        try {
            List<String> imageUrls = new ArrayList<>();
            if (images != null) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageUrl = cloudStorageService.uploadImage(image);
                        imageUrls.add(imageUrl);
                    }
                }
            }

            String email = principal.getName();
            UserDTO userDTO = userService.findByEmail(email);

            createAdDTO.setOwnerId(userDTO.getId());
            createAdDTO.setImageUrls(imageUrls);
            createAdDTO.setReserved(false);
        } catch (IOException e) {
            LOGGER.error("An error occurred while uploading the image", e);
            return "redirect:/advertisement-add";
        }

        advertisementService.createAd(createAdDTO);
        return "redirect:/list-advertisements";
    }

    @GetMapping("/{id}")
    public String adDetails(@PathVariable("id") UUID advertisementId,
                            Model model,
                            @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedException {
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        UUID currentUserId = userByEmail.getId();

        boolean isOwner = advertisementService.isOwnerOfAd(currentUserId, advertisementId);
        model.addAttribute("isOwner", isOwner);

        UUID ownerId = advertisementService.getAdDetails(advertisementId).ownerId();
        model.addAttribute("adDetails", advertisementService.getAdDetails(advertisementId));
        model.addAttribute("author", userService.findById(ownerId));

        return "advertisement-details";
    }

//    @DeleteMapping("/{id}")
//    public String deleteAd(@PathVariable("id") Long id) {
//
//        advertisementService.deleteAd(id);
//
//        return "redirect:/list-advertisements";
//    }

}
