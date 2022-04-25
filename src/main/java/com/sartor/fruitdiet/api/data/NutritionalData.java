package com.sartor.fruitdiet.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionalData {

    private Double carbohydrates;
    private Double protein;
    private Double fat;
    private Integer calories;
    private Double sugar;

}
