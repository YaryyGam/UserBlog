package com.yaryy.post_service.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class WeatherService {

    private final String API_KEY = "90531ee9ebe208803ea457d3482387e7"; // заміни на свій
    private final String URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    public String getCurrentTemperature(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = String.format(URL, city, API_KEY);
            ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

            JSONObject json = new JSONObject(response.getBody());
            double temp = json.getJSONObject("main").getDouble("temp");

            return temp + "°C";
        } catch (Exception e) {
            return "Unavailable";
        }
    }
}
