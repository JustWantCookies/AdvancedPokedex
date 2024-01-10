package com.example.advancedpokedex.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"slot"})
public class PokemonType {
    @JsonProperty("type")
    private TypeDetail type;

    public TypeDetail getType() {
        return type;
    }

    public void setType(TypeDetail type) {
        this.type = type;
    }
}
