package de.husakki.itscraper.germantech;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.husakki.itscraper.DTO.Job;
import de.husakki.itscraper.SQLite.SQLFacade;
import de.husakki.itscraper.interfaces.ISQLite;
import de.husakki.itscraper.interfaces.IScraper;

public class Germantech implements IScraper {
    private final ISQLite db;

    public Germantech(ISQLite db) {
        this.db = db;
    }

    public Germantech() {
        this(new SQLFacade());
    }

    @Override
    public void startScraping() {
        HttpClient client = HttpClient.newHttpClient();

        String APIURL = "https://germantechjobs.de/api/jobsLight";
        String germantechURL = "https://germantechjobs.de/jobs/";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(APIURL))
                .header("Accept", "application/json")
                .GET()
                .build();

        HashSet<Job> foundJobSet = new HashSet<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();

                JsonNode rootNode = mapper.readTree(response.body());

                if (rootNode.isArray()) {
                    for (JsonNode job : rootNode) {
                        JsonNode remote = job.get("workplace");

                        if (remote != null && "remote".equalsIgnoreCase(remote.asText())) {

                            String companyName = job.has("company") ? job.get("company").asText() : "Unknown Company";
                            String jobTitle = job.has("name") ? job.get("name").asText() : "Unknown Title";
                            String jobURL = job.has("jobUrl") ? job.get("jobUrl").asText() : "Unknown URL";
                            String city = job.has("cityCategory") ? job.get("cityCategory").asText() : "Unknown City";

                            foundJobSet.add(new Job(companyName, jobTitle, (germantechURL + jobURL), city, true));

                        }

                        JsonNode city = job.get("cityCategory");

                        if (city != null && "Hamburg".equalsIgnoreCase(city.asText())) {

                            String companyName = job.has("company") ? job.get("company").asText() : "Unknown Company";
                            String jobTitle = job.has("name") ? job.get("name").asText() : "Unknown Title";
                            String jobURL = job.has("jobUrl") ? job.get("jobUrl").asText() : "Unknown URL";
                            String workplace = job.has("workplace") ? job.get("workplace").asText()
                                    : "Unknown Workplace";

                            boolean isRemote = "remote".equalsIgnoreCase(workplace) ? true : false;
                            foundJobSet
                                    .add(new Job(companyName, jobTitle, (germantechURL + jobURL), "Hamburg", isRemote));

                        }
                    }

                } else {
                    System.out.println("Expected a JSON array, but received a different structure.");
                }
            } else {
                System.err.println("Request failed with status code: " + response.statusCode());
            }

        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }

        if (!foundJobSet.isEmpty()) {
            db.writeScrapToDB(foundJobSet);
        }
    }

}
