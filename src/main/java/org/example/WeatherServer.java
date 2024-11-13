package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/weather", new WeatherHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class WeatherHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                String query = exchange.getRequestURI().getQuery();
                String location = query.split("=")[1];

                // OpenWeatherMap API call
                String apiKey = "8b859b6247d6bf0c1ffbfea4102802b8"; //  API key
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String weather = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
                double tempKelvin = jsonResponse.getJSONObject("main").getDouble("temp");
                double tempCelsius = tempKelvin - 273.15;

                JSONObject responseJson = new JSONObject();
                responseJson.put("city", location);
                responseJson.put("weather", weather);
                responseJson.put("temperature", String.format("%.2f", tempCelsius));

                byte[] responseBytes = responseJson.toString().getBytes();
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();

            } catch (Exception e) {
                try {
                    String error = "{\"error\": \"" + e.getMessage() + "\"}";
                    exchange.sendResponseHeaders(500, error.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(error.getBytes());
                    os.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
