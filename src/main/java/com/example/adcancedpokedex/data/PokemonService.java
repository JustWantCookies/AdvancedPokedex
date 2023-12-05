package com.example.adcancedpokedex.data;

import java.util.ArrayList;
import java.util.List;

public class PokemonService {

    public List<Pokemon> loadAllPokemonDetails() {
        List<Pokemon> pokemons = new ArrayList<>();
        List<Pokemon> allPokemon = PokemonApi.loadAllPokemon();



    }
}
