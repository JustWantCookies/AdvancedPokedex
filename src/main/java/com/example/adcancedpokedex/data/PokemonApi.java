package com.example.adcancedpokedex.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokemonApi {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Pokemon getPokemon(String pokemonName) {
        try {
            String json = RequestHandler.sendGetRequest(BASE_URL + pokemonName);
            return objectMapper.readValue(json, Pokemon.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Pokemon> loadAllPokemon() throws IOException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        while (nextUrl != null) {
            String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");
            nextUrl = rootNode.path("next").asText(null);

            for (JsonNode resultNode : resultsNode) {
                String pokemonUrl = resultNode.path("url").asText();
                Pokemon pokemon = getPokemonDetails(pokemonUrl);
                allPokemon.add(pokemon);
            }
        }
        return allPokemon;
    }

    public static Pokemon getPokemonDetails(String url) throws IOException {
        String jsonResponse = RequestHandler.sendGetRequest(url);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        String name = rootNode.path("name").asText();

        List<Pokemon.Stat> stats = new ArrayList<>();
        for(JsonNode statNode : rootNode.path("stats")) {
            String statName = statNode.path("stat").path("name").asText();
            int baseStat = statNode.path("base_stat").asInt();
            stats.add(new Pokemon.Stat(statName, baseStat));
        }

        List<String> types = new ArrayList<>();
        for(JsonNode typeNode : rootNode.path("types")) {
            String typeName = typeNode.path("type").path("name").asText();
            types.add(typeName);
        }

        String imageUrl = rootNode.path("sprites").path("front_default").asText();

        return new Pokemon(name, stats, types, imageUrl);
    }
}
