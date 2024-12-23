package com.hexagonal.challenge.infraestructure.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "brands")
public class BrandEntity {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;
}