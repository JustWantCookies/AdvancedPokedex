package com.example.advancedpokedex.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonService {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Pokemon> loadAllPokemon() throws IOException, NoInternetException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        while (nextUrl != null) {
            String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");
            nextUrl = rootNode.path("next").asText(null);

            for (JsonNode resultNode : resultsNode) {
                String pokemonUrl = resultNode.path("url").asText();
                Pokemon pokemon = PokemonApi.getPokemonDetails(pokemonUrl);
                allPokemon.add(pokemon);
            }
        }
        return allPokemon;
    }
}
