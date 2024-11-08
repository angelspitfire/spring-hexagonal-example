package com.hexagonal.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class Campaign {

    private String campaignId;
    private String name;
    private String description;

}
