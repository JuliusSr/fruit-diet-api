package com.sartor.fruitdiet.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fruit {

    private String name;
    private String family;
    private String genus;
    private String order;
    private NutritionalData nutritionalData;

}
