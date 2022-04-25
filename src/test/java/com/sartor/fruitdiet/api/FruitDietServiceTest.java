package com.sartor.fruitdiet.api;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.data.SortingOrder;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;
import com.sartor.fruitdiet.api.fruitsinfo.FruitsInfoService;
import com.sartor.fruitdiet.api.service.FruitDietService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FruitDietServiceTest {

    @MockBean
    private FruitsInfoService fruitsInfoService;

    @Autowired
    private FruitDietService fruitDietService;

    @Test
    public void sortFruitList_shouldSort() {
        List<Fruit> fruitList = List.of(Fruits.BANANA, Fruits.DURIAN, Fruits.WATERMELON, Fruits.RASPBERRY);

        List<Fruit> sortedByCaloriesAsc = FruitDietService.sortFruitList(
                fruitList,
                NutritionalValue.calories,
                SortingOrder.asc
        );
        assertEquals(Fruits.WATERMELON, sortedByCaloriesAsc.get(0));
        assertEquals(Fruits.RASPBERRY, sortedByCaloriesAsc.get(1));
        assertEquals(Fruits.BANANA, sortedByCaloriesAsc.get(2));
        assertEquals(Fruits.DURIAN, sortedByCaloriesAsc.get(3));

        List<Fruit> sortedByCaloriesDesc = FruitDietService.sortFruitList(
                fruitList,
                NutritionalValue.calories,
                SortingOrder.desc
        );
        assertEquals(Fruits.DURIAN, sortedByCaloriesDesc.get(0));
        assertEquals(Fruits.BANANA, sortedByCaloriesDesc.get(1));
        assertEquals(Fruits.RASPBERRY, sortedByCaloriesDesc.get(2));
        assertEquals(Fruits.WATERMELON, sortedByCaloriesDesc.get(3));

        List<Fruit> sortedByProteinAsc = FruitDietService.sortFruitList(
                fruitList,
                NutritionalValue.protein,
                SortingOrder.asc
        );
        assertEquals(Fruits.WATERMELON, sortedByProteinAsc.get(0));
        assertEquals(Fruits.BANANA, sortedByProteinAsc.get(1));
        assertEquals(Fruits.RASPBERRY, sortedByProteinAsc.get(2));
        assertEquals(Fruits.DURIAN, sortedByProteinAsc.get(3));
    }

    @Test
    public void getNutritionalDataAverage_shouldComputeAverage() {
        List<Fruit> rosaceaeFruits = List.of(Fruits.APPLE, Fruits.PEAR);
        NutritionalData rosaceaeAverageNutritionalData = FruitDietService.getNutritionalDataAverage(rosaceaeFruits);
        assertEquals(13.2, rosaceaeAverageNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(0.35, rosaceaeAverageNutritionalData.getProtein(), 0.001);
        assertEquals(0.25, rosaceaeAverageNutritionalData.getFat(), 0.001);
        assertEquals((Integer) 54, rosaceaeAverageNutritionalData.getCalories());
        assertEquals(10.15, rosaceaeAverageNutritionalData.getSugar(), 0.001);
    }

    @Test
    @SneakyThrows
    public void filterFruits_givenNutritionalValueMax_shouldRetrieveDataAndPreserveOrder() {
        when(fruitsInfoService.getFruitsByNutritionalValue(NutritionalValue.calories,null,60.0))
                .thenReturn(List.of(Fruits.WATERMELON, Fruits.PEAR, Fruits.APPLE, Fruits.RASPBERRY));

        List<Fruit> fruitsWithLessThan60Calories = fruitDietService.filterFruits(
                NutritionalValue.calories,
                null,
                60.0,
                null
        );

        assertEquals(Fruits.WATERMELON, fruitsWithLessThan60Calories.get(0));
        assertEquals(Fruits.PEAR, fruitsWithLessThan60Calories.get(1));
        assertEquals(Fruits.APPLE, fruitsWithLessThan60Calories.get(2));
        assertEquals(Fruits.RASPBERRY, fruitsWithLessThan60Calories.get(3));
    }

    @Test
    @SneakyThrows
    public void filterFruits_givenNutritionalValueRangeAndOrder_shouldRetrieveAndSortByNutritionalValue() {
        when(fruitsInfoService.getFruitsByNutritionalValue(NutritionalValue.calories,90.0,150.0))
                .thenReturn(List.of(Fruits.BANANA, Fruits.DURIAN));

        List<Fruit> fruitsSortedByCaloriesBetween90And150Desc = fruitDietService.filterFruits(
                NutritionalValue.calories,
                90.0,
                150.0,
                SortingOrder.desc
        );

        assertEquals(Fruits.DURIAN, fruitsSortedByCaloriesBetween90And150Desc.get(0));
        assertEquals(Fruits.BANANA, fruitsSortedByCaloriesBetween90And150Desc.get(1));
    }

    @Test
    @SneakyThrows
    public void filterFruits_givenNutritionalValueAndOrder_shouldRetrieveAndSortByNutritionalValue() {
        when(fruitsInfoService.getFruitsByNutritionalValue(NutritionalValue.calories,null,null))
                .thenReturn(List.of(Fruits.DURIAN, Fruits.BANANA, Fruits.RASPBERRY));

        List<Fruit> fruitsSortedByCaloriesAsc = fruitDietService.filterFruits(
                NutritionalValue.calories,
                null,
                null,
                SortingOrder.asc
        );

        assertEquals(Fruits.RASPBERRY, fruitsSortedByCaloriesAsc.get(0));
        assertEquals(Fruits.BANANA, fruitsSortedByCaloriesAsc.get(1));
        assertEquals(Fruits.DURIAN, fruitsSortedByCaloriesAsc.get(2));
    }

    @Test(expected = FruitInfoRetrievalException.class)
    @SneakyThrows
    public void filterFruits_onRetrievalErrorError_shouldThrowFruitInfoRetrievalException() {
        when(fruitsInfoService.getFruitsByNutritionalValue(NutritionalValue.calories,0.0,0.0))
                .thenThrow(new FruitInfoRetrievalException());

        fruitDietService.filterFruits(NutritionalValue.calories, 0.0, 0.0, null);
    }

    @Test
    @SneakyThrows
    public void getNutritionalDataDifference_givenValidInput_shouldReturnDifference() {
        when(fruitsInfoService.getFruitByName("apple")).thenReturn(Fruits.APPLE);
        when(fruitsInfoService.getFruitByName("pear")).thenReturn(Fruits.PEAR);
        NutritionalData pearVsAppleNutritionalData = fruitDietService.getNutritionalDataDifference(
                "apple",
                "pear"
        );
        assertEquals(3.6, pearVsAppleNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(0.1, pearVsAppleNutritionalData.getProtein(), 0.001);
        assertEquals(-0.3, pearVsAppleNutritionalData.getFat(), 0.001);
        assertEquals(5, pearVsAppleNutritionalData.getCalories(), 0.001);
        assertEquals(-0.3, pearVsAppleNutritionalData.getSugar(), 0.001);
    }

    @Test(expected = FruitInfoRetrievalException.class)
    @SneakyThrows
    public void getNutritionalDataDifference_onRetrievalErrorError_shouldThrowFruitInfoRetrievalException() {
        when(fruitsInfoService.getFruitByName("apple")).thenThrow(new FruitInfoRetrievalException());
        fruitDietService.getNutritionalDataDifference("apple","pear");
    }

    @Test
    @SneakyThrows
    public void getFruitFamilyNutritionalDataAverage_givenValidFamily_shouldReturnAverage() {
        when(fruitsInfoService.getFruitsByFamily("Rosaceae"))
                .thenReturn(List.of(Fruits.APPLE, Fruits.PEAR));

        NutritionalData rosaceaeAverageNutritionalData = fruitDietService.getFruitFamilyNutritionalDataAverage("Rosaceae");
        assertEquals(13.2, rosaceaeAverageNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(0.35, rosaceaeAverageNutritionalData.getProtein(), 0.001);
        assertEquals(0.25, rosaceaeAverageNutritionalData.getFat(), 0.001);
        assertEquals((Integer) 54, rosaceaeAverageNutritionalData.getCalories());
        assertEquals(10.15, rosaceaeAverageNutritionalData.getSugar(), 0.001);
    }

    @Test(expected = FruitInfoRetrievalException.class)
    @SneakyThrows
    public void getFruitFamilyNutritionalDataAverage_onRetrievalErrorError_shouldThrowFruitInfoRetrievalException() {
        when(fruitsInfoService.getFruitsByFamily("someInvalidFamily")).thenThrow(new FruitInfoRetrievalException());
        fruitDietService.getFruitFamilyNutritionalDataAverage("someInvalidFamily");
    }

}
