package Model;

import java.util.List;

public class UserSession {
    private String sessionToken;
    private ApiClient apiClient;
    private Dashboard dashboard;

    public UserSession(String baseUrl) {
        apiClient = new ApiClient(baseUrl);
    }


    public void login(String username, String password) {
        sessionToken = apiClient.login(username, password);
    }

    public void setRoommateProfile(String city, Integer minBudget, Integer maxBudget,
                                   String notes) {
        apiClient.roommateProfile(city, minBudget, maxBudget, notes, this.sessionToken);
    }

    public List<?> getRoommateRequests() {
        return apiClient.getRoommateRequests(this.sessionToken);
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
        this.apiClient.makeRoommateRequest(id, this.sessionToken);
    }

    public List<Roommate> search() {
        return this.apiClient.searchRoommates(this.sessionToken);
    }


}
