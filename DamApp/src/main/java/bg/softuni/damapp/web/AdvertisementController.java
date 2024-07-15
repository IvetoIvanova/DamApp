package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.CreateAdDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.CloudStorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/advertisement-add")
public class AdvertisementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementController.class);
    private final AdvertisementService advertisementService;
    private final CloudStorageService cloudStorageService;

    public AdvertisementController(AdvertisementService advertisementService, CloudStorageService cloudStorageService) {
        this.advertisementService = advertisementService;
        this.cloudStorageService = cloudStorageService;
    }

    @GetMapping
    public String showAddAdvertisementForm(Model model) {

        if (!model.containsAttribute("advertisementData")) {
            model.addAttribute("advertisementData", new CreateAdDTO());
        }

        return "advertisement-add";
    }

    @PostMapping
    public String createAd(
            @Valid CreateAdDTO createAdDTO,
            @RequestParam("images") MultipartFile[] images,
            BindingResult bindingResult,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("advertisementData", createAdDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.advertisementData", bindingResult);
            return "redirect:/advertisement-add";
        }

        try {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = cloudStorageService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            createAdDTO.setImageUrls(imageUrls);
            createAdDTO.setReserved(false);
        } catch (IOException e) {
            LOGGER.error("An error occurred while uploading the image", e);
            return "redirect:/advertisement-add";
        }

        advertisementService.createAd(createAdDTO);
        return "redirect:/list-advertisements";
    }

}
