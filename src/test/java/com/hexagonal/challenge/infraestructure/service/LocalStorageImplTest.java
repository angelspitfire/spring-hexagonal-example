package com.hexagonal.challenge.infraestructure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalStorageImplTest {

    @InjectMocks
    private LocalStorageImpl fileStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String storageLocation = tempDir.toString();
        fileStorageService = new LocalStorageImpl(storageLocation);
    }

    @Test
    void givenValidMultipartFile_whenStoreFile_thenFileIsStoredSuccessfully() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
        String storedFilePath = fileStorageService.storeFile(multipartFile);
        assertTrue(Files.exists(Path.of(storedFilePath)));
    }

    @Test
    void givenNullFile_whenStoreFile_thenExceptionIsThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageService.storeFile(null));
        assertEquals("Cannot store null file", exception.getMessage());
    }
}