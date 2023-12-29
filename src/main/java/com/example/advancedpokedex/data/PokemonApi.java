package com.example.advancedpokedex.data;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokemonApi {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Pokemon getPokemon(String pokemonName){
        try {
            String json = RequestHandler.sendGetRequest(BASE_URL + pokemonName);
            return objectMapper.readValue(json, Pokemon.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Pokemon getPokemonDetails(String url) throws IOException {
        String jsonResponse = RequestHandler.sendGetRequest(url);
        return objectMapper.readValue(jsonResponse, Pokemon.class);
    }
}
