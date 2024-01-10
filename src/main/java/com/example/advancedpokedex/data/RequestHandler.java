package com.example.advancedpokedex.data;

import com.example.advancedpokedex.exceptions.BackendRequestException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RequestHandler {

    private RequestHandler(){

    }

    /**
     * Sends a GET request to a specified URL and retrieves the response as a string.
     *
     * @param urlString The URL to send the GET request to.
     * @return A string containing the response data from the URL.
     * @throws BackendRequestException  if there is no internet connection or the URL is unreachable.
     */
    public static String sendGetRequest(String urlString) throws BackendRequestException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new BackendRequestException("Repsonse Code is " + responseCode);
            }

            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();
            return inline.toString();
        } catch (IOException e){
            throw new BackendRequestException(e.getMessage());
        }
    }
}
