package com.project.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    private int id;
    private String name;
    private Double price;
    private String description;
    private String creationDate;
    private List<Category> categories;
}
