package com.example.advancedpokedex.data;

import com.example.advancedpokedex.exceptions.BackendRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeApi {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/type/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches a list of Pokemon types from an external API.
     *
     * @return A List of String containing the names of Pokemon types.
     * @throws BackendRequestException if an I/O error occurs during the HTTP request or if there is no internet connection or the API is unreachable.
     */
    public List<String> getTypes() throws BackendRequestException {
        List<String> allTypes = new ArrayList<>();

        try {
            String jsonResponse = RequestHandler.sendGetRequest(BASE_URL);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");

            for (JsonNode resultNode : resultsNode) {
                allTypes.add(resultNode.path("name").asText());
            }
        } catch (IOException e) {
            throw new BackendRequestException(e.getMessage());
        }

        return allTypes;
    }

}
