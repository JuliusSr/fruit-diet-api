package com.sartor.fruitdiet.api;

import com.sartor.fruitdiet.api.controller.FruitDietController;
import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;
import com.sartor.fruitdiet.api.service.FruitDietService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FruitDietController.class)
public class FruitDietControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FruitDietService fruitDietService;

    @Test
    @SneakyThrows
    public void filter_whenNutritionalValueNotProvided_shouldReturnClientError() {
        mockMvc.perform(get("/diet/fruits/filter"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/diet/fruits/filter/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void filter_givenInvalidNutritionalValue_shouldReturnClientError() {
        mockMvc.perform(get("/diet/fruits/filter/someInvalidValue"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/diet/fruits/filter/CALORIES"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void filter_givenInvalidOrder_shouldReturnClientError() {
        mockMvc.perform(get("/diet/fruits/filter/calories?order=hello"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void filter_givenInvalidMinOrMaxValue_shouldReturnClientError() {
        mockMvc.perform(get("/diet/fruits/filter/calories?min=someString"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/diet/fruits/filter/calories?max=someString"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void filter_givenValidInput_shouldReturnListOfFruits() {
        Fruit durianFruit = Fruit.builder()
                .name("Durian")
                .family("Malvaceae")
                .order("Malvales")
                .genus("Durio")
                .nutritionalData(NutritionalData.builder()
                        .carbohydrates(27.1)
                        .protein(1.5)
                        .fat(5.3)
                        .calories(147)
                        .sugar(6.75)
                        .build()
                )
                .build();

        when(fruitDietService.filterFruits(NutritionalValue.calories,100.0,null,null))
                .thenReturn(Collections.singletonList(durianFruit));

        mockMvc.perform(get("/diet/fruits/filter/calories?min=100"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"name\":\"Durian\",\"family\":\"Malvaceae\",\"genus\":\"Durio\",\"order\":\"Malvales\",\"nutritionalData\":{\"carbohydrates\":27.1,\"protein\":1.5,\"fat\":5.3,\"calories\":147,\"sugar\":6.75}}]"));
    }

    @Test
    @SneakyThrows
    public void fliter_onErrorRetrievingFruitInfoData_shouldReturnError() {
        when(fruitDietService.filterFruits(NutritionalValue.calories,100.0,null,null))
                .thenThrow(new FruitInfoRetrievalException());

        mockMvc.perform(get("/diet/fruits/filter/calories?min=100"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error:\":\"Could not retrieve data\"}"));
    }

    @Test
    @SneakyThrows
    public void comparison_givenTwoFruitNames_shouldReturnNutritionalDataDifference() {
        NutritionalData pearVsAppleNutritionalDataDifference = NutritionalData.builder()
                .carbohydrates(3.6)
                .protein(0.1)
                .fat(-0.3)
                .calories(5)
                .sugar(-0.3)
                .build();

        when(fruitDietService.getNutritionalDataDifference("apple","pear"))
                .thenReturn(pearVsAppleNutritionalDataDifference);

        mockMvc.perform(get("/diet/fruits/comparison?baselineFruit=apple&comparedFruit=pear"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"carbohydrates\":3.6,\"protein\":0.1,\"fat\":-0.3,\"calories\":5,\"sugar\":-0.3}"));
    }

    @Test
    @SneakyThrows
    public void comparison_whenMandatoryInputNotProvided_shouldReturnError() {
        mockMvc.perform(get("/diet/fruits/comparison"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/diet/fruits/comparison?comparedFruit=pear"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/diet/fruits/comparison?baselineFruit=apple"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void comparison_onErrorRetrievingFruitInfoData_shouldReturnError() {
        when(fruitDietService.getNutritionalDataDifference("apple","pear"))
                .thenThrow(new FruitInfoRetrievalException());

        mockMvc.perform(get("/diet/fruits/comparison?baselineFruit=apple&comparedFruit=pear"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error:\":\"Could not retrieve data\"}"));
    }

    @Test
    @SneakyThrows
    public void familyAverage_givenValidFamilyName_shouldReturnClientError() {
        NutritionalData rosaceaeNutritionalDataAverage = NutritionalData.builder()
                .carbohydrates(8.88)
                .protein(0.66)
                .fat(-0.32)
                .calories(39)
                .sugar(-6.75)
                .build();

        when(fruitDietService.getFruitFamilyNutritionalDataAverage("Rosaceae"))
                .thenReturn(rosaceaeNutritionalDataAverage);

        mockMvc.perform(get("/diet/fruits/average/family/Rosaceae"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"carbohydrates\":8.88,\"protein\":0.66,\"fat\":-0.32,\"calories\":39,\"sugar\":-6.75}"));
    }

    @Test
    @SneakyThrows
    public void familyAverage_whenFamilyNameNotProvided_shouldReturnClientError() {
        mockMvc.perform(get("/diet/fruits/average/family"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/diet/fruits/average/family/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void familyAverage_onErrorRetrievingFruitInfoData_shouldReturnError() {
        when(fruitDietService.getFruitFamilyNutritionalDataAverage("Rosaceae"))
                .thenThrow(new FruitInfoRetrievalException());

        mockMvc.perform(get("/diet/fruits/average/family/Rosaceae"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error:\":\"Could not retrieve data\"}"));
    }

}
