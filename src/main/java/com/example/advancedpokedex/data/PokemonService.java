package com.example.advancedpokedex.data;

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
     * @throws IOException          if an I/O error occurs during the HTTP request.
     * @throws NoInternetException  if there is no internet connection or the API is unreachable.
     */
    /*
    public List<Pokemon> getPokemons() throws IOException, NoInternetException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        //while (nextUrl != null) {
            String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");
            //nextUrl = rootNode.path("next").asText(null);

            for (JsonNode resultNode : resultsNode) {
                String pokemonUrl = resultNode.path("url").asText();
                //Takes much too long
                //Pokemon pokemon = PokemonApi.getPokemonDetails(pokemonUrl);
                Pokemon pokemon = new Pokemon(resultNode.path("name").asText(), pokemonUrl);
                allPokemon.add(pokemon);
            }
        //}
        return allPokemon;
    }
    */

    /**
     * Fetches detailed information for a list of Pokemon from an external API.
     *
     * @return A List of Pokemon objects containing detailed information.
     * @throws IOException          if an I/O error occurs during the HTTP request.
     * @throws NoInternetException  if there is no internet connection or the API is unreachable.
     */
    public List<Pokemon> getPokemonsDetalis() throws IOException, NoInternetException {
        List<Pokemon> allPokemon = new ArrayList<>();
        String nextUrl = BASE_URL;

        String jsonResponse = RequestHandler.sendGetRequest(nextUrl);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("results");

        for (JsonNode resultNode : resultsNode) {
            String pokemonUrl = resultNode.path("url").asText();
            Pokemon pokemon = PokemonApi.getPokemonDetails(pokemonUrl);
            allPokemon.add(pokemon);
        }
        return allPokemon;
    }

    /**
     * Fetches detailed information for a specific Pokemon using its API URL.
     *
     * @param url The API URL of the Pokemon for which to retrieve details.
     * @return A Pokemon object containing detailed information.
     * @throws IOException          if an I/O error occurs during the HTTP request.
     * @throws NoInternetException  if there is no internet connection or the API is unreachable.
     */
    public Pokemon getPokemonDetail(String url) throws IOException, NoInternetException {
        return PokemonApi.getPokemonDetails(url);
    }

}
