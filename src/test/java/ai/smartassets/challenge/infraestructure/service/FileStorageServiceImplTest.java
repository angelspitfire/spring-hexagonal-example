package ai.smartassets.challenge.infraestructure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceImplTest {

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    private final String storageLocation = "src/test/resources/temp";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileStorageService = new FileStorageServiceImpl(storageLocation);
    }

    @Test
    void storeFile_Success() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
        String storedFilePath = fileStorageService.storeFile(multipartFile);
        assertTrue(Files.exists(Path.of(storedFilePath)));
    }

    @Test
    void storeFile_NullFile_ThrowsException() {
        Exception exception = assertThrows(RuntimeException.class, () -> fileStorageService.storeFile(null));
        assertEquals("Cannot store null file", exception.getMessage());
    }
}