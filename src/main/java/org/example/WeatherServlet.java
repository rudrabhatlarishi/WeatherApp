package org.example;

import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "WeatherServlet", urlPatterns = "/weather")
public class WeatherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Content-Type", "application/json");

        // Get the city parameter from the request
        String city = req.getParameter("city");
        if (city == null || city.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"error\": \"City parameter is required!\"}");
            return;
        }

        // Call the weather API and get the response
        String apiKey = "8b859b6247d6bf0c1ffbfea4102802b8"; // Your OpenWeatherMap API key
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            // Fetch weather data
            String weatherData = WeatherApiClient.fetchWeather(apiUrl);
            JSONObject jsonResponse = new JSONObject(weatherData);

            // Check for API errors
            if (!jsonResponse.has("main")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("{\"error\": \"Invalid city or data not found!\"}");
                return;
            }

            // Extract data from the JSON response
            JSONObject main = jsonResponse.getJSONObject("main");
            double temperature = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

            JSONObject wind = jsonResponse.has("wind") ? jsonResponse.getJSONObject("wind") : null;
            double windSpeed = (wind != null) ? wind.optDouble("speed", 0.0) : 0.0;

            // Create the JSON response
            JSONObject responseJson = new JSONObject();
            responseJson.put("city", city);
            responseJson.put("temperature", temperature);
            responseJson.put("weather", description);
            responseJson.put("humidity", humidity);
            responseJson.put("windSpeed", windSpeed);

            // Write the JSON response
            PrintWriter writer = resp.getWriter();
            writer.print(responseJson.toString());
            writer.flush();
        } catch (Exception e) {
            // Handle errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("{\"error\": \"Error fetching weather data: " + e.getMessage() + "\"}");
        }
    }
}
