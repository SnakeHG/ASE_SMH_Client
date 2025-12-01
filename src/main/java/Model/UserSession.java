package Model;

import java.util.List;
import java.util.stream.Collectors;

public class UserSession {
    private String sessionToken;
    private ApiClient apiClient;
    private String username;

    public UserSession(String baseUrl) {
        apiClient = new ApiClient(baseUrl);
    }

    public void register(String username, String password, String email) {
        apiClient.register(username, password, email);
    }

    public void login(String username, String password) {
        this.username = username;
        sessionToken = apiClient.login(username, password);
    }

    public void logout() {
        sessionToken = null;
        username = null;
    }

    public void setRoommateProfile(String city, Integer minBudget, Integer maxBudget,
                                   String notes, List<Integer> value) {
        apiClient.roommateProfile(city, minBudget, maxBudget, notes, value, this.sessionToken);
    }

    public List<Roommate> filterOutThisUser(List<Roommate> roommates) {
        return roommates.stream().filter(r -> !r.getName().equals(username)).toList();
    }

    public List<Roommate> getAcceptedRoommates() {
        return this.apiClient.getAcceptedRoommates(this.sessionToken);
    }

    public List<Roommate> getSentRoommates() {
        return this.apiClient.getSentRoommates(this.sessionToken);
    }

    public List<Roommate> getReceivedRoommates() {
        return this.apiClient.getReceivedRoommates(this.sessionToken);
    }


    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void makeRoommateRequest(long id) {
        this.apiClient.makeRoommateRequest(id, username, this.sessionToken);
    }

    public List<Roommate> search() {
        return this.apiClient.searchRoommates(this.sessionToken);
    }

    public List<Roommate> getRecommendations() {
        return this.apiClient.getRecommendations(this.sessionToken);
    }

    public void acceptRoommateRequest(long id) {
        this.apiClient.acceptRequest(id, this.sessionToken);
    }

    public void rejectRoommateRequest(long id) {
        this.apiClient.rejectRequest(id, this.sessionToken);
    }


}
