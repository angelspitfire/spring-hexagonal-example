package ai.smartassets.challenge.aplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignCreationDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 1, max = 500, message = "Description must be between 1 and 500 characters")
    private String description;
}