package com.sartor.fruitdiet.api.fruitsinfo.fruityvice;

import com.sartor.fruitdiet.api.data.Fruit;
import com.sartor.fruitdiet.api.data.NutritionalValue;
import com.sartor.fruitdiet.api.fruitsinfo.FruitsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FruityViceFruitsInfoService implements FruitsInfoService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${fruityvice.url}/api/fruit/")
    private String fruitApiURL;

    public Fruit getFruitByName(String name) {
        return null; //TODO
    }

    public List<Fruit> getFruitsByNutritionalValue(NutritionalValue nutritionalValue, Double min, Double max) {
        return null; //TODO
    }

    public List<Fruit> getFruitsByFamily(String family) {
        return null; //TODO
    }

}
