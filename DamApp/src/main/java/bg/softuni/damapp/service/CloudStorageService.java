package bg.softuni.damapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudStorageService {
    String uploadImage(MultipartFile file) throws IOException;
    void deleteImages(List<String> imageUrls) throws IOException;
}
