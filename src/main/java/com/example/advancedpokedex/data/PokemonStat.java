package com.example.advancedpokedex.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"effort"})
public class PokemonStat {
    @JsonProperty("stat")
    private StatReference statName;
    @JsonProperty("base_stat")
    private int baseStat;

    public StatReference getStatName() {
        return statName;
    }

    public void setStatName(StatReference statName) {
        this.statName = statName;
    }

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }
}
