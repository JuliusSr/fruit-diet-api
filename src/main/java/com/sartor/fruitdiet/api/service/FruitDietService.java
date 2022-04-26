package com.sartor.fruitdiet.api.service;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.data.SortingOrder;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;
import com.sartor.fruitdiet.api.fruitsinfo.FruitsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FruitDietService {

    @Autowired
    private FruitsInfoService fruitsInfoService;

    public List<Fruit> filterFruits(
            NutritionalValue nutritionalValue,
            Double min,
            Double max,
            SortingOrder order
    ) throws FruitInfoRetrievalException {
        List<Fruit> fruitList = fruitsInfoService.getFruitsByNutritionalValue(nutritionalValue, min, max);

        if (order != null) {
            return sortFruitList(fruitList, nutritionalValue, order);
        }

        return fruitList;
    }

    public static List<Fruit> sortFruitList(List<Fruit> fruitList, NutritionalValue nutritionalValue, SortingOrder order) {
        Comparator<Double> keyComparator = (order == SortingOrder.asc)
                ? Comparator.naturalOrder()
                : Comparator.reverseOrder();
        return fruitList.stream()
                .sorted(Comparator.comparing(
                        f -> f.getNutritionalData().getNutritionalValue(nutritionalValue),
                        keyComparator))
                .collect(Collectors.toList());
    }

    public NutritionalData getNutritionalDataDifference(
            String baselineFruitName,
            String comparedFruitName
    ) throws FruitInfoRetrievalException {
        CompletableFuture<Fruit> baselineFruitFuture = fruitsInfoService.getFruitByNameAsync(baselineFruitName);
        CompletableFuture<Fruit> comparedFruitFuture = fruitsInfoService.getFruitByNameAsync(comparedFruitName);

        Fruit baselineFruit = baselineFruitFuture.join();
        Fruit comparedFruit = comparedFruitFuture.join();

        return comparedFruit
                .getNutritionalData()
                .differenceFrom(baselineFruit.getNutritionalData());
    }

    public NutritionalData getFruitFamilyNutritionalDataAverage(
            String family
    ) throws FruitInfoRetrievalException {
        List<Fruit> fruitsInFamilyList = fruitsInfoService.getFruitsByFamily(family);
        return getNutritionalDataAverage(fruitsInFamilyList);
    }

    public static NutritionalData getNutritionalDataAverage(List<Fruit> fruits) {
        return fruits.stream()
                .map(Fruit::getNutritionalData)
                .reduce(new NutritionalData(), NutritionalData::combinedWith)
                .dividedBy(fruits.size());
    }

}
