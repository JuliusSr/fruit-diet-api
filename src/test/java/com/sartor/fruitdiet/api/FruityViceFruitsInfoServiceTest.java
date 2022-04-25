package com.sartor.fruitdiet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalData;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;
import com.sartor.fruitdiet.api.fruitsinfo.fruityvice.FruityViceFruitsInfoService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(FruityViceFruitsInfoService.class)
public class FruityViceFruitsInfoServiceTest {

    private static final String FRUIT_INFO_API_URL = "http://localhost:8080/api/fruit/";

    @Autowired
    private FruityViceFruitsInfoService fruitsInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @SneakyThrows
    public void getFruitByName_givenValidFruitName_shouldRetrieveFruitInfo() {
        Fruit bananaFruit = Fruit.builder()
                .name("Banana")
                .family("Musaceae")
                .order("Zingiberales")
                .genus("Musa")
                .nutritionalData(NutritionalData.builder()
                        .carbohydrates(22.0)
                        .protein(1.0)
                        .fat(0.2)
                        .calories(96)
                        .sugar(17.2)
                        .build()
                )
                .build();
        String bananaResponse = "{\n" +
                "    \"genus\": \"Musa\",\n" +
                "    \"name\": \"Banana\",\n" +
                "    \"id\": 1,\n" +
                "    \"family\": \"Musaceae\",\n" +
                "    \"order\": \"Zingiberales\",\n" +
                "    \"nutritions\": {\n" +
                "        \"carbohydrates\": 22,\n" +
                "        \"protein\": 1,\n" +
                "        \"fat\": 0.2,\n" +
                "        \"calories\": 96,\n" +
                "        \"sugar\": 17.2\n" +
                "    }\n" +
                "}";

        mockServer
                .expect(requestTo(FRUIT_INFO_API_URL + "banana"))
                .andRespond(withSuccess(bananaResponse, MediaType.APPLICATION_JSON));

        Fruit retrievedFruit = fruitsInfoService.getFruitByName("banana");

        assertEquals(bananaFruit, retrievedFruit);
    }

    @Test(expected = FruitInfoRetrievalException.class)
    @SneakyThrows
    public void getFruitByName_onApiError_shouldThrowFruitRetrievalException() {
        mockServer
                .expect(requestTo(FRUIT_INFO_API_URL + "forbiddenFruit"))
                .andRespond(withBadRequest().body("{\"error\": \"The fruit was not found\"}"));

        fruitsInfoService.getFruitByName("forbiddenFruit");
    }

    @Test
    @SneakyThrows
    public void getFruitsByFamily_givenValidInput_shouldReturnListOfFruitsInFamily() {
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
        mockServer
                .expect(requestTo(FRUIT_INFO_API_URL + "family/Durian"))
                .andRespond(withSuccess("[{\"id\":60,\"name\":\"Durian\",\"family\":\"Malvaceae\",\"genus\":\"Durio\",\"order\":\"Malvales\",\"nutritions\":{\"carbohydrates\":27.1,\"protein\":1.5,\"fat\":5.3,\"calories\":147,\"sugar\":6.75}}]", MediaType.APPLICATION_JSON));

        List<Fruit> retrievedFruitList = fruitsInfoService.getFruitsByFamily("Durian");

        assertEquals(1, retrievedFruitList.size());
        assertEquals(durianFruit, retrievedFruitList.get(0));
    }

    @Test(expected = FruitInfoRetrievalException.class)
    @SneakyThrows
    public void getFruitsByFamily_onApiError_shouldThrowFruitRetrievalException() {
        mockServer
                .expect(requestTo(FRUIT_INFO_API_URL + "family/someFruitFamily"))
                .andRespond(withBadRequest().body("{\"error\": \"The fruit was not found\"}"));

        fruitsInfoService.getFruitsByFamily("someFruitFamily");
    }

    @Test
    @SneakyThrows
    public void getFruitsByNutritionalValue_givenValidInput_shouldReturnListOfFruitsSatisfyingConstraints() {
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

        mockServer
                .expect(requestTo(FRUIT_INFO_API_URL + "calories?min=100.0&max=1000.0"))
                .andRespond(withSuccess("[{\"id\":60,\"name\":\"Durian\",\"family\":\"Malvaceae\",\"genus\":\"Durio\",\"order\":\"Malvales\",\"nutritions\":{\"carbohydrates\":27.1,\"protein\":1.5,\"fat\":5.3,\"calories\":147,\"sugar\":6.75}}]", MediaType.APPLICATION_JSON));

        List<Fruit> retrievedFruitList = fruitsInfoService.getFruitsByNutritionalValue(NutritionalValue.calories, 100.0, null);

        assertEquals(1, retrievedFruitList.size());
        assertEquals(durianFruit, retrievedFruitList.get(0));
    }

    @Test(expected = FruitInfoRetrievalException.class)
    @SneakyThrows
    public void getFruitsByNutritionalValue_onApiError_shouldThrowFruitRetrievalException() {
        mockServer
                .expect(requestTo(FRUIT_INFO_API_URL + "calories?min=0.0&max=0.0"))
                .andRespond(withBadRequest().body("{\"error\": \"The fruit was not found\"}"));

        fruitsInfoService.getFruitsByNutritionalValue(NutritionalValue.calories, 0.0, 0.0);
    }

}
