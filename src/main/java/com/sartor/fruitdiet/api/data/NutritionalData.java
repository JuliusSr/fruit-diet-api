package com.sartor.fruitdiet.api.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NutritionalData {

    private Double carbohydrates;
    private Double protein;
    private Double fat;
    private Integer calories;
    private Double sugar;

}
