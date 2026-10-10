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
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();

                JsonNode rootNode = mapper.readTree(response.body());

                System.out.println("--- Remote Jobs Found ---");
                int remoteCount = 0;

                if (rootNode.isArray()) {
                    for (JsonNode job : rootNode) {
                        JsonNode workplace = job.get("workplace");

                        if (workplace != null && "remote".equalsIgnoreCase(workplace.asText())) {
                            remoteCount++;

                            String title = job.has("name") ? job.get("name").asText() : "Unknown Title";
                            String company = job.has("company") ? job.get("company").asText() : "Unknown Company";

                            System.out.println(remoteCount + ". " + title + " @ " + company);

                            // System.out.println(job.toPrettyString());
                        }
                    }
                    System.out.println("\nTotal remote jobs found: " + remoteCount);
                } else {
                    System.out.println("Expected a JSON array, but received a different structure.");
                }
            } else {
                System.err.println("Request failed with status code: " + response.statusCode());
            }

        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

}
