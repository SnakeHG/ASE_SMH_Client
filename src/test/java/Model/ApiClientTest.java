package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiClientTest {

    private ApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient("http://localhost:8080");
    }

    @Test
    public void TestLogin() {
        String token = apiClient.login("admin", "admin123");
        assertNotNull(token);
    }



}