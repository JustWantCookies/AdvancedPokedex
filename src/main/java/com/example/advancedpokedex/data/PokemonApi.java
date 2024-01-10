package com.example.advancedpokedex.data;

import java.io.IOException;

import com.example.advancedpokedex.exceptions.BackendRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokemonApi {

    private PokemonApi(){

    }

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches detailed information for a specific Pokemon by its name using the external API.
     *
     * @param pokemonName The name of the Pokemon for which to retrieve details.
     * @return A Pokemon object containing detailed information.
     * @throws BackendRequestException if there is no internet connection or the API is unreachable.
     */
    public static Pokemon getPokemon(String pokemonName) throws BackendRequestException {
        try {
            String json = RequestHandler.sendGetRequest(BASE_URL + pokemonName);
            return objectMapper.readValue(json, Pokemon.class);
        } catch (IOException e) {
            throw new BackendRequestException(e.getMessage());
        }
    }

    /**
     * Fetches detailed information for a specific Pokemon using its API URL.
     *
     * @param url The API URL of the Pokemon for which to retrieve details.
     * @return A Pokemon object containing detailed information.
     * @throws BackendRequestException  if an I/O error occurs during the HTTP request or if there is no internet connection or the API is unreachable.
     */
    public static Pokemon getPokemonDetails(String url) throws BackendRequestException {
        try {
            String jsonResponse = RequestHandler.sendGetRequest(url);
            return objectMapper.readValue(jsonResponse, Pokemon.class);
        } catch (IOException e) {
            throw new BackendRequestException(e.getMessage());
        }
    }
}
