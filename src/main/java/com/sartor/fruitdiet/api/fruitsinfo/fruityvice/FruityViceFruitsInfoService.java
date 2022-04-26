package com.sartor.fruitdiet.api.fruitsinfo.fruityvice;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.exceptions.FruitInfoRetrievalException;
import com.sartor.fruitdiet.api.fruitsinfo.FruitsInfoService;
import com.sartor.fruitdiet.api.fruitsinfo.fruityvice.dto.FruityViceFruit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FruityViceFruitsInfoService implements FruitsInfoService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${fruityvice.url}/api/fruit/")
    private String fruitApiURL;

    @Override
    public Fruit getFruitByName(
            String name
    ) throws FruitInfoRetrievalException {
        String endpoint = fruitApiURL + name;
        try {
            FruityViceFruit fruitResponse = restTemplate.getForObject(endpoint, FruityViceFruit.class);
            return FruityViceDataConverter.convertFruit(fruitResponse);
        } catch (Exception e) { //TODO avoid catching Exception
            throw new FruitInfoRetrievalException(e);
        }
    }

    @Async
    public CompletableFuture<Fruit> getFruitByNameAsync(
            String name
    ) throws FruitInfoRetrievalException {
        return CompletableFuture.completedFuture(getFruitByName(name));
    }

    @Override
    public List<Fruit> getFruitsByNutritionalValue(
            NutritionalValue nutritionalValue,
            Double min,
            Double max
    ) throws FruitInfoRetrievalException {
        String endpoint = fruitApiURL + "{nutritionalValue}?min={min}&max={max}";

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("nutritionalValue", nutritionalValue.toString());
        uriVariables.put("min", min != null ? min : 0.0);
        uriVariables.put("max", max != null ? max : 1000.0);

        try {
            ResponseEntity<List<FruityViceFruit>> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<FruityViceFruit>>() {},
                    uriVariables
            );
            return FruityViceDataConverter.convertFruitList(response.getBody());
        } catch (Exception e) { //TODO avoid catching Exception
            throw new FruitInfoRetrievalException(e);
        }
    }

    @Override
    public List<Fruit> getFruitsByFamily(
            String family
    ) throws FruitInfoRetrievalException {
        String endpoint = fruitApiURL + "family/" + family;

        try {
            ResponseEntity<List<FruityViceFruit>> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<FruityViceFruit>>() {}
            );
            return FruityViceDataConverter.convertFruitList(response.getBody());
        } catch (Exception e) { //TODO avoid catching Exception
            throw new FruitInfoRetrievalException(e);
        }
    }

}
