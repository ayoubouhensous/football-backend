package com.example.fotballl_backend.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FootballController {

    @Value("${VITE_FOOTBALL_API_KEY}")
    private String footballApiKey;

    @Value("${VITE_NEWS_API_KEY}")
    private String newsApiKey;

    private final RestTemplate restTemplate;

    public FootballController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Endpoint pour récupérer les matchs du jour
    @GetMapping("/api/matches")
    public Object getTodayMatches(@RequestParam String date) {
        System.out.println("Appel reçu sur /api/matches avec date=" + date);

        String url = "https://api.football-data.org/v4/matches?dateFrom=" + date + "&dateTo=" + date;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", footballApiKey); // à injecter via @Value
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }


    // Endpoint pour récupérer les actualités sur le football
    @GetMapping("/api/football-news")
    public Object getFootballNews() {
        System.out.println("Appel reçu sur /api/football-news");

        String url = "https://newsapi.org/v2/everything?q=football&apiKey=" + newsApiKey;
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);


        return response.getBody();
    }



    // Endpoint pour récupérer les équipes tendance
    @GetMapping("/api/trending-teams")
    public Object getTrendingTeams() {
        String url = "https://api.football-data.org/v4/competitions/CL/teams";

        // Ajouter la clé API dans les en-têtes de la requête
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", footballApiKey);

        // Effectuer la requête avec les en-têtes appropriés
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class, headers);
        return response.getBody();
    }
}
