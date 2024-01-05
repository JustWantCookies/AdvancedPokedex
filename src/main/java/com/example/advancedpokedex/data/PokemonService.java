package com.example.advancedpokedex.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonService {

    //Maybe change Limit
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=1300";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<Pokemon> getPokemons() throws IOException, NoInternetException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        while (nextUrl != null) {
            String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");
            nextUrl = rootNode.path("next").asText(null);

            for (JsonNode resultNode : resultsNode) {
                String pokemonUrl = resultNode.path("url").asText();
                //Takes much too long
                //Pokemon pokemon = PokemonApi.getPokemonDetails(pokemonUrl);
                Pokemon pokemon = new Pokemon(resultNode.path("name").asText(), pokemonUrl);
                allPokemon.add(pokemon);
            }
        }
        return allPokemon;
    }

    public Pokemon getPokemonDetail(String url) throws IOException, NoInternetException {
       return PokemonApi.getPokemonDetails(url);
    }

}
