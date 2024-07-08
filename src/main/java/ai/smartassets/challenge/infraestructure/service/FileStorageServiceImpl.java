package ai.smartassets.challenge.infraestructure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String storageLocation;

    public FileStorageServiceImpl(@Value("${file.storage.location}") String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null) {
            throw new RuntimeException("Cannot store null file");
        }

        try {
            Path storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
            Files.createDirectories(storagePath); // Ensure the directory exists

            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID() + fileExtension;

            Path targetLocation = storagePath.resolve(fileName);
            file.transferTo(targetLocation.toFile());

            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
