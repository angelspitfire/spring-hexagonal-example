package com.hexagonal.challenge.infraestructure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageImpl implements FileStorageService {

    private final String storageLocation;

    public LocalStorageImpl(@Value("${file.storage.location}") String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store null or empty file");
        }

        try {
            var storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
            Files.createDirectories(storagePath);

            var originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IllegalArgumentException("Original filename is null or empty");
            }

            var fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            var fileName = UUID.randomUUID() + fileExtension;

            var targetLocation = storagePath.resolve(fileName);
            file.transferTo(targetLocation.toFile());

            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}