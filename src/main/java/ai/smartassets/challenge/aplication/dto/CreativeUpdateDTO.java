package ai.smartassets.challenge.aplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CreativeUpdateDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Creative URL is mandatory")
    private String creativeUrl;

    @NotBlank(message = "Brand ID is mandatory")
    private String brandId;
}