package com.co.softworld.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Document(collection = "product")
public class Product {

    @Id
    private String id;
    @NotEmpty
    private String name;
    @NotNull
    private Double price;
    @NotNull
    private Category category;
    private String photo;
}
