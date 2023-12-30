package com.example.advancedpokedex.data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RequestHandler {

    public static String sendGetRequest(String urlString) throws NoInternetException {
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            return inline.toString();
        } catch (Exception e) {
            throw new NoInternetException("Unnable to send Request" + e.getMessage());
        }
    }
}
