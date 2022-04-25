package com.sartor.fruitdiet.api.service;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.data.SortingOrder;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;
import com.sartor.fruitdiet.api.fruitsinfo.FruitsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return null; //TODO
    }

    public NutritionalData getNutritionalDataDifference(String baselineFruitName, String comparedFruitName) throws FruitInfoRetrievalException {
        return null; //TODO
    }

    public NutritionalData getFruitFamilyNutritionalDataAverage(String family) throws FruitInfoRetrievalException {
        return null; //TODO
    }

}
