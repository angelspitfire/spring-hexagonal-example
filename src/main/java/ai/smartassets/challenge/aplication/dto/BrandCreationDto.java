package ai.smartassets.challenge.aplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandCreationDto {

    @NotBlank(message = "Brand name must not be blank")
    @Size(min = 1, max = 255, message = "Brand name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Brand description must not be blank")
    @Size(min = 1, max = 500, message = "Brand description must be between 1 and 500 characters")
    private String description;
}
