package com.example.advancedpokedex.data;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokemonApi {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches detailed information for a specific Pokemon by its name using the external API.
     *
     * @param pokemonName The name of the Pokemon for which to retrieve details.
     * @return A Pokemon object containing detailed information.
     * @throws NoInternetException  if there is no internet connection or the API is unreachable.
     */
    public static Pokemon getPokemon(String pokemonName) throws NoInternetException {
        try {
            String json = RequestHandler.sendGetRequest(BASE_URL + pokemonName);
            return objectMapper.readValue(json, Pokemon.class);
        } catch (IOException | NoInternetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches detailed information for a specific Pokemon using its API URL.
     *
     * @param url The API URL of the Pokemon for which to retrieve details.
     * @return A Pokemon object containing detailed information.
     * @throws IOException          if an I/O error occurs during the HTTP request.
     * @throws NoInternetException  if there is no internet connection or the API is unreachable.
     */
    public static Pokemon getPokemonDetails(String url) throws IOException, NoInternetException {
        String jsonResponse = RequestHandler.sendGetRequest(url);
        return objectMapper.readValue(jsonResponse, Pokemon.class);
    }
}
