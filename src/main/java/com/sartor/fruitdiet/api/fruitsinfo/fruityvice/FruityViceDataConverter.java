package com.sartor.fruitdiet.api.fruitsinfo.fruityvice;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.fruitsinfo.fruityvice.dto.FruityViceFruit;
import com.sartor.fruitdiet.api.fruitsinfo.fruityvice.dto.FruityViceNutritions;

import java.util.List;
import java.util.stream.Collectors;

public class FruityViceDataConverter {

    public static List<Fruit> convertFruitList(List<FruityViceFruit> fruitList) {
        return fruitList.stream()
                .map(FruityViceDataConverter::convertFruit)
                .collect(Collectors.toList());
    }

    public static Fruit convertFruit(FruityViceFruit fruit) {
        return Fruit.builder()
                .name(fruit.getName())
                .family(fruit.getFamily())
                .genus(fruit.getGenus())
                .order(fruit.getOrder())
                .nutritionalData(convertNutritions(fruit.getNutritions()))
                .build();
    }

    public static NutritionalData convertNutritions(FruityViceNutritions nutritions) {
        return NutritionalData.builder()
                .carbohydrates(nutritions.getCarbohydrates())
                .protein(nutritions.getProtein())
                .fat(nutritions.getFat())
                .calories(nutritions.getCalories())
                .sugar(nutritions.getSugar())
                .build();
    }

}
