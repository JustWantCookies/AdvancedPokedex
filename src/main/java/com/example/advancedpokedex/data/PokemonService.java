package com.example.advancedpokedex.data;

import com.example.advancedpokedex.exceptions.BackendRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonService {

    //Maybe change Limit
    //LIMIT TO x else restructure to download
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=10";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches a list of Pokemon from an external API.
     *
     * @return A List of Pokemon objects with basic information (name and API URL).
     * @throws BackendRequestException if an I/O error occurs during the HTTP request or if there is no internet connection or the API is unreachable.
     */

    public List<Pokemon> getPokemons() throws BackendRequestException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        try {
            String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");

            for (JsonNode resultNode : resultsNode) {
                String pokemonUrl = resultNode.path("url").asText();
                Pokemon pokemon = new Pokemon(resultNode.path("name").asText(), pokemonUrl);
                allPokemon.add(pokemon);
            }
        } catch (IOException e) {
            throw new BackendRequestException(e.getMessage());
        }
        return allPokemon;
    }


    /**
     * Fetches detailed information for a list of Pokemon from an external API.
     *
     * @return A List of Pokemon objects containing detailed information.
     * @throws BackendRequestException if an I/O error occurs during the HTTP request or if there is no internet connection or the API is unreachable.
     */
    public List<Pokemon> getPokemonsDetalis() throws BackendRequestException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        try {
            String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");

            for (JsonNode resultNode : resultsNode) {
                String pokemonUrl = resultNode.path("url").asText();
                Pokemon pokemon = PokemonApi.getPokemonDetails(pokemonUrl);
                allPokemon.add(pokemon);
            }
        } catch (IOException e) {
            throw new BackendRequestException(e.getMessage());
        }
        return allPokemon;
    }

    /**
     * Fetches detailed information for a specific Pokemon using its API URL.
     *
     * @param url The API URL of the Pokemon for which to retrieve details.
     * @return A Pokemon object containing detailed information.
     * @throws BackendRequestException if an I/O error occurs during the HTTP request or if there is no internet connection or the API is unreachable.
     */
    public Pokemon getPokemonDetail(String url) throws BackendRequestException {
        return PokemonApi.getPokemonDetails(url);
    }

}
