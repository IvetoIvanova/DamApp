package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.service.CloudStorageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudStorageServiceImpl implements CloudStorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudStorageServiceImpl.class);
    private final Cloudinary cloudinary;

    public CloudStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    @Override
    public void deleteImages(List<String> imageUrls) throws IOException {
        LOGGER.info("Entering deleteImages method. Image URLs: {}", imageUrls);

        if (imageUrls == null || imageUrls.isEmpty()) {
            LOGGER.info("The imageUrls list is empty or null :(");
            return;
        }

        for (String imageUrl : imageUrls) {
            try {
                LOGGER.info("Processing image URL: {}", imageUrl);
                String publicId = extractPublicIdFromUrl(imageUrl);
                LOGGER.info("Extracted publicId: {}", publicId);
                cloudinary.uploader().destroy(publicId, new HashMap<>());
                LOGGER.info("Image with publicId {} deleted successfully", publicId);
            } catch (IOException e) {
                LOGGER.error("Error deleting image with publicId {}", extractPublicIdFromUrl(imageUrl), e);
            }
        }
//        for (String imageUrl : imageUrls) {
//            try {
//                String publicId = extractPublicIdFromUrl(imageUrl);
//                Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//                System.out.println("Delete result: " + result);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        LOGGER.info("Exiting deleteImages method");
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String filename = parts[parts.length - 1];
        return filename.substring(0, filename.lastIndexOf('.'));
    }
}
