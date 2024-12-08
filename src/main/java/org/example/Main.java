package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your city: ");
        String city = scanner.nextLine();

        String apiKey = "8b859b6247d6bf0c1ffbfea4102802b8";

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            long startTime = System.currentTimeMillis();

            // Establish HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read API response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            long endTime = System.currentTimeMillis();


            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            String weather = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = jsonResponse.getJSONObject("main").getDouble("temp") - 273.15; // Kelvin to Celsius

            // Output weather details
            System.out.println("Weather in " + city + ": " + weather);
            System.out.println("Temperature: " + String.format("%.2f", temp) + "°C");
            System.out.println("API Response Time: " + (endTime - startTime) + " ms");

        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
        }
    }
}
