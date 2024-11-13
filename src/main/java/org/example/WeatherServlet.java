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
        // Get the city parameter from the request
        String city = req.getParameter("city");
        if (city == null || city.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("City parameter is required!");
            return;
        }

        // Call the weather API and get the response
        String apiKey = "8b859b6247d6bf0c1ffbfea4102802b8"; // Your OpenWeatherMap API key
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            // Fetch weather data
            String weatherData = WeatherApiClient.fetchWeather(apiUrl);
            JSONObject jsonResponse = new JSONObject(weatherData);

            // Extract data from the JSON response
            JSONObject main = jsonResponse.getJSONObject("main");
            double temperature = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

            JSONObject wind = jsonResponse.has("wind") ? jsonResponse.getJSONObject("wind") : null;
            double windSpeed = (wind != null) ? wind.optDouble("speed", 0.0) : 0.0;

            // Create an HTML response
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();
            writer.println("<html><body>");
            writer.println("<h1>Weather Data</h1>");
            writer.println("<p>City: " + city + "</p>");
            writer.println("<p>Temperature: " + temperature + "°C</p>");
            writer.println("<p>Weather: " + description + "</p>");
            writer.println("<p>Humidity: " + humidity + "%</p>");
            writer.println("<p>Wind Speed: " + windSpeed + " m/s</p>");
            writer.println("</body></html>");
        } catch (Exception e) {
            // Handle errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error fetching weather data: " + e.getMessage());
        }
    }
}
