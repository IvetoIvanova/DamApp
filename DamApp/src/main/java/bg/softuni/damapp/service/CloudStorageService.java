package bg.softuni.damapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudStorageService {
    String uploadImage(MultipartFile file) throws IOException;
}
