package de.husakki.itscraper.germantech;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.husakki.itscraper.DTO.Job;
import de.husakki.itscraper.interfaces.IScraper;

public class Germantech implements IScraper {
    @Override
    public void startScraping() {
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

                int remoteCount = 0;
                int jobsInHamburgCount = 0;

                HashSet<Job> foundJobList = new HashSet<>();

                if (rootNode.isArray()) {
                    for (JsonNode job : rootNode) {
                        JsonNode remote = job.get("workplace");

                        if (remote != null && "remote".equalsIgnoreCase(remote.asText())) {
                            remoteCount++;

                            String companyName = job.has("company") ? job.get("company").asText() : "Unknown Company";
                            String jobTitle = job.has("name") ? job.get("name").asText() : "Unknown Title";
                            String jobURL = job.has("jobUrl") ? job.get("name").asText() : "Unknown URL";
                            String city = job.has("cityCategory") ? job.get("name").asText() : "Unknown City";

                            foundJobList.add(new Job(companyName, jobTitle, jobURL, city, true));
                            // System.out.println(remoteCount + ". " + title + " @ " + company);

                            // System.out.println(job.toPrettyString());
                        }

                        JsonNode city = job.get("cityCategory");

                        if (city != null && "Hamburg".equalsIgnoreCase(city.asText())) {
                            jobsInHamburgCount++;

                            String companyName = job.has("company") ? job.get("company").asText() : "Unknown Company";
                            String jobTitle = job.has("name") ? job.get("name").asText() : "Unknown Title";
                            String jobURL = job.has("jobUrl") ? job.get("name").asText() : "Unknown URL";
                            String workplace = job.has("workplace") ? job.get("workplace").asText()
                                    : "Unknown Workplace";

                            boolean isRemote = "remote".equalsIgnoreCase(workplace) ? true : false;
                            foundJobList.add(new Job(companyName, jobTitle, jobURL, "Hamburg", isRemote));

                            // System.out.println(jobsInHamburgCount + ". " + title + " @ " + company);

                        }
                    }
                    System.out.println("\nTotal remote jobs found: " + remoteCount);
                    System.out.println("\nTotal jobs in Hamburg found: " + jobsInHamburgCount);
                    System.out.println("Found job list size: " + foundJobList.size());
                    System.out.println(
                            "Number of duplicates are: " + ((remoteCount + jobsInHamburgCount) - foundJobList.size()));
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
