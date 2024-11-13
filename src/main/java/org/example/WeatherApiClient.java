package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiClient {

    public static String fetchWeather(String apiUrl) throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Create the URL object
            URL url = new URL(apiUrl);
            // Open the connection
            connection = (HttpURLConnection) url.openConnection();
            // Set the request method
            connection.setRequestMethod("GET");
            // Set timeouts for the connection
            connection.setConnectTimeout(5000); // 5 seconds connection timeout
            connection.setReadTimeout(5000);    // 5 seconds read timeout

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Failed to fetch data from Weather API. Response code: " + responseCode);
            }

            // Read the response
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Return the response as a string
            return response.toString();

        } catch (Exception e) {
            // Throw the exception for error handling
            throw new Exception("Error while fetching weather data: " + e.getMessage(), e);

        } finally {
            // Close resources
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
