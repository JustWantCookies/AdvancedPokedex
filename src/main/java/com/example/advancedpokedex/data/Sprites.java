package com.example.advancedpokedex.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "back_default", "back_female", "back_shiny", "back_shiny_female", "front_female", "front_shiny", "front_shiny_female", "other", "versions"})
public class Sprites {
    @JsonProperty("front_default")
    private String frontDefault;

    public String getFrontDefault(){
        return frontDefault;
    }
    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
