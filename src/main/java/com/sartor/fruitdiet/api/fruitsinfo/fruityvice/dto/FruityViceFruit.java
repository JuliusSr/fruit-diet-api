package com.sartor.fruitdiet.api.fruitsinfo.fruityvice.dto;

import lombok.Data;

@Data
public class FruityViceFruit {

    private Integer id;
    private String name;
    private String family;
    private String genus;
    private String order;
    private FruityViceNutritions nutritions;

}
