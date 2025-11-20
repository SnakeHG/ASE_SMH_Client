package Model;

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
    


    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }


}
