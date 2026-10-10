package de.husakki.itscraper.germantech;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Germantech {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://germantechjobs.de/api/jobsLight"))
                .header("Accept", "application/json") // Request a JSON response
                .GET()
                .build();

        try {
            // 3. Send the request and receive the response as a String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());

            String jsonPayload = response.body();
            // System.out.println("JSON Payload:\n" + jsonPayload);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonPayload);
            String allremoteJobs = jsonNode.get("workplace").asText();
            System.out.println("Remote Job:" + allremoteJobs);

        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
        }
    }
}
