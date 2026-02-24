/**
 * PROJECT 2 - TASK 4 (Console Client)
 *
 * This console program tests the Service Layer by invoking REST endpoints over HTTP.
 * It performs: Create -> Get -> Update -> Get -> Delete -> Get for the Locations resource.
 *
 * HOW TO RUN:
 * 1) Start the service in one terminal:
 *    mvn -q exec:java "-Dexec.mainClass=PantryServiceApi"
 * 2) Run this client in a second terminal:
 *    mvn -q exec:java "-Dexec.mainClass=PantryConsoleClient"
 */

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PantryConsoleClient {

    private static final String BASE_URL = "http://localhost:7070";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Minimal DTO used only by the client (matches your JSON structure)
    public static class LocationDto {
        public int id;
        public String name;

        @Override
        public String toString() {
            return "LocationDto{id=" + id + ", name='" + name + "'}";
        }
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Use a unique name so we can find it reliably after POST
        String uniqueName = "Console Test Location " + Instant.now().toEpochMilli();

        System.out.println("=== TASK 4 CONSOLE TEST (Locations) ===");
        System.out.println("Base URL: " + BASE_URL);

        // 1) CREATE (POST /locations)
        System.out.println("\n1) CREATE (POST /locations)");
        String createJson = "{\"name\":\"" + escapeJson(uniqueName) + "\"}";
        String createResponse = send(client, "POST", "/locations", createJson);
        System.out.println("Response: " + createResponse);

        // 2) READ (GET /locations) and find the ID of what we created
        System.out.println("\n2) READ (GET /locations) and find created ID");
        LocationDto created = findLocationByName(client, uniqueName)
                .orElseThrow(() -> new IllegalStateException(
                        "Could not find newly created location by name. " +
                        "GET /locations did not include: " + uniqueName));

        System.out.println("Found created row: " + created);

        // 3) UPDATE (PUT /locations)
        System.out.println("\n3) UPDATE (PUT /locations)");
        String updatedName = uniqueName + " [UPDATED]";
        String updateJson = "{\"id\":" + created.id + ",\"name\":\"" + escapeJson(updatedName) + "\"}";
        String updateResponse = send(client, "PUT", "/locations", updateJson);
        System.out.println("Response: " + updateResponse);

        // 4) READ again (GET /locations) to confirm update
        System.out.println("\n4) READ (GET /locations) confirm updated name");
        LocationDto updated = findLocationById(client, created.id)
                .orElseThrow(() -> new IllegalStateException(
                        "Could not find location id=" + created.id + " after update."));

        System.out.println("Row after update: " + updated);
        if (!updatedName.equals(updated.name)) {
            throw new IllegalStateException("Update did not apply. Expected name '" + updatedName +
                    "' but got '" + updated.name + "'");
        }

        // 5) DELETE (DELETE /locations/{id})
        System.out.println("\n5) DELETE (DELETE /locations/" + created.id + ")");
        int deleteStatus = sendStatus(client, "DELETE", "/locations/" + created.id, null);
        System.out.println("Delete status: " + deleteStatus + " (204 expected)");

        // 6) READ again to confirm deletion
        System.out.println("\n6) READ (GET /locations) confirm deletion");
        Optional<LocationDto> afterDelete = findLocationById(client, created.id);
        System.out.println("Exists after delete? " + afterDelete.isPresent());

        if (afterDelete.isPresent()) {
            throw new IllegalStateException("Delete did not remove row id=" + created.id);
        }

        System.out.println("\nCRUD test completed successfully.");
    }

    private static Optional<LocationDto> findLocationByName(HttpClient client, String name) throws Exception {
        LocationDto[] all = getAllLocations(client);
        return Arrays.stream(all).filter(l -> name.equals(l.name)).findFirst();
    }

    private static Optional<LocationDto> findLocationById(HttpClient client, int id) throws Exception {
        LocationDto[] all = getAllLocations(client);
        return Arrays.stream(all).filter(l -> l.id == id).findFirst();
    }

    private static LocationDto[] getAllLocations(HttpClient client) throws Exception {
        String body = send(client, "GET", "/locations", null);
        return MAPPER.readValue(body, LocationDto[].class);
    }

    private static String send(HttpClient client, String method, String path, String jsonBody) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path));

        if ("GET".equalsIgnoreCase(method)) {
            b.GET();
        } else if ("POST".equalsIgnoreCase(method)) {
            b.header("Content-Type", "application/json");
            b.POST(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
        } else if ("PUT".equalsIgnoreCase(method)) {
            b.header("Content-Type", "application/json");
            b.PUT(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
        } else if ("DELETE".equalsIgnoreCase(method)) {
            b.DELETE();
        } else {
            throw new IllegalArgumentException("Unsupported method: " + method);
        }

        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString());
        // Print status for transparency (helps screenshots)
        System.out.println(method + " " + path + " -> HTTP " + resp.statusCode());
        return resp.body();
    }

    private static int sendStatus(HttpClient client, String method, String path, String jsonBody) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path));

        if ("DELETE".equalsIgnoreCase(method)) {
            b.DELETE();
        } else {
            throw new IllegalArgumentException("sendStatus currently supports DELETE only");
        }

        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString());
        return resp.statusCode();
    }

    private static String escapeJson(String s) {
        // Minimal escaping for quotes/backslashes (good enough for our test strings)
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}