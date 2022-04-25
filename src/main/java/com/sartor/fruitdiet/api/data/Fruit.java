package com.sartor.fruitdiet.api.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Fruit {

    private String name;
    private String family;
    private String genus;
    private String order;
    private NutritionalData nutritionalData;

}
