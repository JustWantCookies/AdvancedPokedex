package com.example.advancedpokedex.data;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {

    private String name;
    private Sprites sprites;
    private List<PokemonType> types;
    private List<PokemonStat> stats;

    private String detailURL;

    public String getDetailURL() {
        return detailURL;
    }

    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Sprites getSprites() {
        return sprites;
    }
    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<PokemonType> getTypes() {
        return types;
    }
    public void setTypes(List<PokemonType> types) {
        this.types = types;
    }

    public List<PokemonStat> getStats() {
        return stats;
    }
    public void setStats(List<PokemonStat> stats) {
        this.stats = stats;
    }

    public void setDetails(Pokemon pokemon){
        this.types = pokemon.types;
        this.stats = pokemon.stats;
        this.sprites = pokemon.sprites;
    }
    public Pokemon(){

    }
    public Pokemon(String name, String url){
        this.name = name;
        this.detailURL = url;
    }

    public Pokemon (String name, Sprites sprites, List<PokemonType> types, List<PokemonStat> stats){
        this.name = name;
        this.sprites = sprites;
        this.types = types;
        this.stats = stats;
    }
}
