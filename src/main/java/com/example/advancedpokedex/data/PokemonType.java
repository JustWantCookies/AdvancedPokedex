package com.example.advancedpokedex.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"slot"})
public class PokemonType {
    @JsonProperty("type")
    private TypeReference type;

    public TypeReference getType() {
        return type;
    }

    public void setType(TypeReference type) {
        this.type = type;
    }
}
