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

        Path storagePath = Paths.get(storageLocation);
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create storage directory", e);
            }
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + fileExtension;

        Path targetLocation = storagePath.resolve(fileName);
        try {
            file.transferTo(targetLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName, e);
        }

        return targetLocation.toString();
    }
}
