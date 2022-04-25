package com.sartor.fruitdiet.api.controller;

import com.sartor.fruitdiet.api.service.FruitDietService;
import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.data.SortingOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diet/fruits")
public class FruitDietController {

    @Autowired
    private FruitDietService fruitDietService;

    @GetMapping("/filter/{nutritionalValue}")
    public List<Fruit> getFruitsByNutritionalValue(
            @PathVariable NutritionalValue nutritionalValue,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) SortingOrder order
    ) {
        return fruitDietService.filterFruits(nutritionalValue, min, max, order);
    }

    @GetMapping("/comparison")
    public NutritionalData getFruitNutrientsComparison(
            @RequestParam("baselineFruit") String baselineFruitName,
            @RequestParam("comparedFruit") String comparedFruitName
    ) {
        return fruitDietService.getNutritionalDataDifference(baselineFruitName, comparedFruitName);
    }

    @GetMapping("/average/family/{family}")
    public NutritionalData getFruitFamilyNutritionalAverage(@PathVariable String family) {
        return fruitDietService.getFruitFamilyNutritionalDataAverage(family);
    }

}
