package com.sartor.fruitdiet.api.fruitsinfo;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FruitsInfoService {

    Fruit getFruitByName(String name) throws FruitInfoRetrievalException;
    CompletableFuture<Fruit> getFruitByNameAsync(String name) throws FruitInfoRetrievalException;
    List<Fruit> getFruitsByFamily(String family) throws FruitInfoRetrievalException;
    List<Fruit> getFruitsByNutritionalValue(NutritionalValue nutritionalValue, Double min, Double max) throws FruitInfoRetrievalException;

}
