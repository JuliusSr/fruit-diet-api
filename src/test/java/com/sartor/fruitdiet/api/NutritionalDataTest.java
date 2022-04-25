package com.sartor.fruitdiet.api;

import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NutritionalDataTest {

    @Test
    public void getNutritionalValue_givenNutritionalValue_shouldReturnCorrespondingValue() {
        assertEquals(22.0, Fruits.BANANA.getNutritionalData().getNutritionalValue(NutritionalValue.carbohydrates), 0.001);
        assertEquals(1.0, Fruits.BANANA.getNutritionalData().getNutritionalValue(NutritionalValue.protein), 0.001);
        assertEquals(0.2, Fruits.BANANA.getNutritionalData().getNutritionalValue(NutritionalValue.fat), 0.001);
        assertEquals(96, Fruits.BANANA.getNutritionalData().getNutritionalValue(NutritionalValue.calories), 0.001);
        assertEquals(17.2, Fruits.BANANA.getNutritionalData().getNutritionalValue(NutritionalValue.sugar), 0.001);
    }

    @Test
    public void differenceFrom_shouldComputeDifference() {
        NutritionalData pearVsAppleNutritionalData = Fruits.PEAR.getNutritionalData().differenceFrom(Fruits.APPLE.getNutritionalData());
        assertEquals(3.6, pearVsAppleNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(0.1, pearVsAppleNutritionalData.getProtein(), 0.001);
        assertEquals(-0.3, pearVsAppleNutritionalData.getFat(), 0.001);
        assertEquals(5, pearVsAppleNutritionalData.getCalories(), 0.001);
        assertEquals(-0.3, pearVsAppleNutritionalData.getSugar(), 0.001);

        NutritionalData appleVsPearNutritionalData = Fruits.APPLE.getNutritionalData().differenceFrom(Fruits.PEAR.getNutritionalData());
        assertEquals(-3.6, appleVsPearNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(-0.1, appleVsPearNutritionalData.getProtein(), 0.001);
        assertEquals(0.3, appleVsPearNutritionalData.getFat(), 0.001);
        assertEquals(-5, appleVsPearNutritionalData.getCalories(), 0.001);
        assertEquals(0.3, appleVsPearNutritionalData.getSugar(), 0.001);
    }

    @Test
    public void combinedWith_shouldComputeSumOfNutritionalData() {
        NutritionalData pearAndAppleNutritionalData = Fruits.PEAR.getNutritionalData().combinedWith(Fruits.APPLE.getNutritionalData());
        assertEquals(26.4, pearAndAppleNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(0.7, pearAndAppleNutritionalData.getProtein(), 0.001);
        assertEquals(0.5, pearAndAppleNutritionalData.getFat(), 0.001);
        assertEquals(109, pearAndAppleNutritionalData.getCalories(), 0.001);
        assertEquals(20.3, pearAndAppleNutritionalData.getSugar(), 0.001);
    }

    @Test
    public void dividedBy_shouldDivideAllNutritionalData() {
        NutritionalData aThirdOfDurianNutritionalData = Fruits.DURIAN.getNutritionalData().dividedBy(3);
        assertEquals(9.03, aThirdOfDurianNutritionalData.getCarbohydrates(), 0.001);
        assertEquals(0.5, aThirdOfDurianNutritionalData.getProtein(), 0.001);
        assertEquals(1.77, aThirdOfDurianNutritionalData.getFat(), 0.001);
        assertEquals(49, aThirdOfDurianNutritionalData.getCalories(), 0.001);
        assertEquals(2.25, aThirdOfDurianNutritionalData.getSugar(), 0.001);
    }

}
