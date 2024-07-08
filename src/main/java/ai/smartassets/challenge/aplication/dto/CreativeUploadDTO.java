package ai.smartassets.challenge.aplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data @AllArgsConstructor
public class CreativeUploadDTO {
    private String name;
    private String description;
    private MultipartFile file;
}