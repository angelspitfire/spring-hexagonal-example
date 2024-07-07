package ai.smartassets.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Campaign {

    private String campaignId;
    private String name;
    private String description;

}
