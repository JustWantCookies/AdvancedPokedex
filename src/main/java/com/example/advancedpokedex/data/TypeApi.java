package com.example.advancedpokedex.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeApi {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/type/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode resultsNode;

    public List<String> getTypes() throws IOException, NoInternetException {
        List<String> allTypes = new ArrayList<>();

        String jsonResponse = RequestHandler.sendGetRequest(BASE_URL);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("results");

        try {
            for (JsonNode resultNode : resultsNode) {
                allTypes.add(resultNode.path("name").asText());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return allTypes;
    }

}
