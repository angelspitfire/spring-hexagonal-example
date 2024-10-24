package com.hexagonal.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Creative {

    private String creativeId;
    private String name;
    private String description;
    private String creativeUrl;
    private String campaignId;

}
