package Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;

public class ApiClient {

    private final String baseUrl;
    private final HttpClient httpClient;
    private final Gson gson;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        httpClient = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    private static class LoginResponse {
        String token;
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            switch (statusCode) {
                case 200:
                case 201:
                    return response;
                default:
                    throw new IllegalStateException(response.body());
            }

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public String register(String username, String password, String email) {
        return "";
    }

    /**
     * Attempts to login using the given Username and password to the API.
     * @param username The username to use.
     * @param password The password to use.
     * @return A token associated with the account that logged in using the given credentials.
     * @throws IllegalStateException if failed to log in using the given credentials.
     */
    public String login(String username, String password) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = sendRequest(request);
        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);
        return loginResponse.token;

    }

    private static class RoomatePreference {
        String city;
        Integer minBudget;
        Integer maxBudget;
        String notes;
        boolean lookingForRoommates;

        public RoomatePreference(String city, Integer minBudget,
                                 Integer maxBudget, String notes, boolean b) {
            this.city = city;
            this.minBudget = minBudget;
            this.maxBudget = maxBudget;
            this.notes = notes;
            lookingForRoommates = b;
        }
    }

    public void roommatePreference(String city, Integer minBudget, Integer maxBudget,
                                   String notes, String token) {
        RoomatePreference roomatePreference = new RoomatePreference(city, minBudget, maxBudget,
                notes, true);

        String input = gson.toJson(roomatePreference, RoomatePreference.class);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommate/new"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        sendRequest(request);
    }



}
