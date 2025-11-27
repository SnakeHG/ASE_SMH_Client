package Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    private static class RoommatePreference {
        String city;
        Integer minBudget;
        Integer maxBudget;
        String notes;
        boolean lookingForRoommates;

        public RoommatePreference(String city, Integer minBudget,
                                  Integer maxBudget, String notes, boolean b) {
            this.city = city;
            this.minBudget = minBudget;
            this.maxBudget = maxBudget;
            this.notes = notes;
            this.lookingForRoommates = b;
        }
    }

    public void roommateProfile(String city, Integer minBudget, Integer maxBudget,
                                   String notes, String token) {
        RoommatePreference roommatePreference = new RoommatePreference(city, minBudget, maxBudget,
                notes, true);

        String input = gson.toJson(roommatePreference, RoommatePreference.class);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommate/new"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        sendRequest(request);
    }

    public Roommate getUserInfo(String username, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/search"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);

        JsonArray users = JsonParser.parseString(response.body()).getAsJsonArray();
        for (JsonElement user : users) {
            JsonObject userJson = user.getAsJsonObject();
            String name = userJson.getAsJsonObject("user").get("username").getAsString();
            if (!name.equalsIgnoreCase(username)) {
                continue;
            }
            long id = userJson.get("id").getAsLong();
            String city = userJson.get("city").getAsString();
            int minBudget = userJson.get("minBudget").getAsInt();
            int maxBudget = userJson.get("maxBudget").getAsInt();
            return new Roommate(id, name, city, minBudget, maxBudget, "");
        }

        throw new IllegalArgumentException("User does not exist");
    }

    private List<Roommate> createRoommateListFromJson(String json) {
        JsonArray users = JsonParser.parseString(json).getAsJsonArray();
        List<Roommate> roommates = new ArrayList<>();
        for (JsonElement user : users) {
            JsonObject userJson = user.getAsJsonObject();
            String name = userJson.getAsJsonObject("user").get("username").getAsString();
            long id = userJson.get("id").getAsLong();
            String city = userJson.get("city").getAsString();
            int minBudget = userJson.get("minBudget").getAsInt();
            int maxBudget = userJson.get("maxBudget").getAsInt();
            roommates.add(new Roommate(id, name, city, minBudget, maxBudget, ""));
        }
        return roommates;
    }

    public List<Roommate> getReceivedRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/history/received"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        return this.createRoommateListFromJson(response.body());
    }

    public List<Roommate> getSentRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/history/sent"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        return this.createRoommateListFromJson(response.body());
    }


    public List<Roommate> getAcceptedRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/history/matches"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        return this.createRoommateListFromJson(response.body());
    }

    public void makeRoommateRequest(long id, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/request/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        sendRequest(request);
    }


    // TO BE IMPLEMENTED

    public List<Roommate> searchRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/search"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        return this.createRoommateListFromJson(response.body());
    }

    public List<?> getRoommateRequests(String token) {
        return null;
    }

    public List<?> getRecommendations(String token) {
        return null;
    }



    public void acceptRequest(int matchID, String token) {

    }

    public void rejectRequest(int matchID, String token) {

    }




}
