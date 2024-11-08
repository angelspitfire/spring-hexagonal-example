package com.hexagonal.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class Brand {

    private String brandId;
    private String name;
    private String description;

}
