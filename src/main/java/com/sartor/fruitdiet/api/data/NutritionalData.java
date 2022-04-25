package com.sartor.fruitdiet.api.data;

import com.sartor.fruitdiet.api.utils.ArithmeticUtils;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NutritionalData {

    private Double carbohydrates = 0.0;
    private Double protein = 0.0;
    private Double fat = 0.0;
    private Integer calories = 0;
    private Double sugar = 0.0;

    public Double getNutritionalValue(NutritionalValue nutritionalValue) {
        Double value = 0.0;
        switch (nutritionalValue) {
            case carbohydrates: value = carbohydrates; break;
            case protein: value = protein; break;
            case fat: value = fat; break;
            case calories: value = calories.doubleValue(); break;
            case sugar: value = sugar; break;
        }
        return value;
    }

    public NutritionalData differenceFrom(NutritionalData baseline) {
        return NutritionalData.builder()
                .carbohydrates(ArithmeticUtils.subtract(carbohydrates, baseline.getCarbohydrates()))
                .protein(ArithmeticUtils.subtract(protein, baseline.getProtein()))
                .fat(ArithmeticUtils.subtract(fat, baseline.getFat()))
                .calories(calories - baseline.getCalories())
                .sugar(ArithmeticUtils.subtract(sugar, baseline.getSugar()))
                .build();
    }

    public NutritionalData combinedWith(NutritionalData other) {
        return NutritionalData.builder()
                .carbohydrates(ArithmeticUtils.sum(carbohydrates, other.getCarbohydrates()))
                .protein(ArithmeticUtils.sum(protein, other.getProtein()))
                .fat(ArithmeticUtils.sum(fat, other.getFat()))
                .calories(calories + other.getCalories())
                .sugar(ArithmeticUtils.sum(sugar, other.getSugar()))
                .build();
    }

    public NutritionalData dividedBy(Integer dividend) {
        return NutritionalData.builder()
                .carbohydrates(ArithmeticUtils.divide(carbohydrates, dividend))
                .protein(ArithmeticUtils.divide(protein, dividend))
                .fat(ArithmeticUtils.divide(fat, dividend))
                .calories(calories / dividend)
                .sugar(ArithmeticUtils.divide(sugar, dividend))
                .build();
    }

}
