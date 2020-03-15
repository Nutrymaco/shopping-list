package com.example.ShoppingList.model;

import lombok.Data;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Purchase {
    @Id
    Integer id;

    private String name;

    private String description;

    private Integer count;

}
