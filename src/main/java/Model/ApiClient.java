package Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    if (response.body().isEmpty()) {
                        throw new IllegalStateException("Request Failed");
                    }
                    throw new IllegalStateException(response.body());
            }

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void register(String username, String password, String email) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}",
                username, password, email);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        sendRequest(request);
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
                                   String notes, List<Integer> personality, String token) {
        RoommatePreference roommatePreference = new RoommatePreference(city, minBudget, maxBudget,
                notes, true);

        String input = gson.toJson(roommatePreference, RoommatePreference.class);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/new"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        HttpResponse<String> response = sendRequest(request);

        JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject user = responseJson.getAsJsonObject("user");
        long id = user.get("id").getAsLong();

        JsonArray personalityJsonArr = new JsonArray();
        for (Integer i : personality) {
            personalityJsonArr.add(i);
        }
        JsonObject personalityJson = new JsonObject();
        personalityJson.addProperty("userId", id);
        personalityJson.add("responseValues", personalityJsonArr);

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/personality"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(personalityJson.toString()))
                .build();
        sendRequest(request2);
    }


    public List<Roommate> getUserInfo(List<String> username, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/search"))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);

        List<Roommate> userInfo = new ArrayList<>();
        JsonArray users = JsonParser.parseString(response.body()).getAsJsonArray();
        for (JsonElement user : users) {
            JsonObject userJson = user.getAsJsonObject();
            String name = userJson.getAsJsonObject("user").get("username").getAsString();
            if (!username.contains(name)) {
                continue;
            }
            long id = userJson.getAsJsonObject("user").get("id").getAsLong();
            String city = userJson.get("city").getAsString();
            int minBudget = userJson.get("minBudget").getAsInt();
            int maxBudget = userJson.get("maxBudget").getAsInt();
            userInfo.add(new Roommate(id, name, city, minBudget, maxBudget, ""));
        }

        return userInfo;
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

    private Status getStatus(String status) {
        if (status.equalsIgnoreCase("pending")) {
            return Status.PENDING;
        } else if (status.equalsIgnoreCase("accepted")) {
            return Status.ACCEPTED;
        } else {
            return Status.REJECTED;
        }
    }

    private List<Roommate> createRoommateListFromRequest(String json, String userField, String token) {
        JsonArray users = JsonParser.parseString(json).getAsJsonArray();
        List<String> names = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<Status> statuses = new ArrayList<>();
        for (JsonElement request : users) {
            JsonObject userJson = request.getAsJsonObject();
            long requestId = userJson.get("id").getAsLong();
            Status status = getStatus(userJson.get("status").getAsString());
            String name = userJson.getAsJsonObject(userField).get("username").getAsString();
            names.add(name);
            ids.add(requestId);
            statuses.add(status);
        }
        List<Roommate> roommates = this.getUserInfo(names, token);
        for (int i = 0; i < roommates.size(); i++) {
            roommates.get(i).setRequestId(ids.get(i));
            roommates.get(i).setStatus(statuses.get(i));
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
        return this.createRoommateListFromRequest(response.body(), "requester", token);
    }

    public List<Roommate> getSentRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/history/sent"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        return this.createRoommateListFromRequest(response.body(), "candidate", token);
    }

    private List<Roommate> getUniqueRoommates(List<Roommate> list1, List<Roommate> list2) {
        Set<Roommate> uniqueSet = new HashSet<>();
        uniqueSet.addAll(list1);
        uniqueSet.addAll(list2);

        return new ArrayList<>(uniqueSet);
    }


    public List<Roommate> getAcceptedRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/history/matches"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        List<Roommate> list1 = this.createRoommateListFromRequest(response.body(), "candidate",
                token);
        List<Roommate> list2 = this.createRoommateListFromRequest(response.body(), "requester",
                token);

        return getUniqueRoommates(list1, list2);
    }

    public void makeRoommateRequest(long id, String username, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/request/" + id + "?requesterUsername=" + username))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        sendRequest(request);
    }


    // TO BE IMPLEMENTED

    public List<Roommate> searchRoommates(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/search"))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = sendRequest(request);
        return this.createRoommateListFromJson(response.body());
    }


    public List<Roommate> getRecommendations(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/recommendation"))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = sendRequest(request);

        JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();

        // Extract usernames
        List<String> usernames = new ArrayList<>();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();
            usernames.add(obj.get("username").getAsString());
        }

        return this.getUserInfo(usernames, token);
    }



    public void acceptRequest(long matchID, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/" + matchID + "/accept"))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        sendRequest(request);
    }

    public void rejectRequest(long matchID, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/roommates/" + matchID + "/reject"))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        sendRequest(request);
    }




}
