package com.sartor.fruitdiet.api.fruitsinfo;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalValue;

import java.util.List;

public interface FruitsInfoService {

    Fruit getFruitByName(String name);
    List<Fruit> getFruitsByFamily(String family);
    List<Fruit> getFruitsByNutritionalValue(NutritionalValue nutritionalValue, Double min, Double max);

}
