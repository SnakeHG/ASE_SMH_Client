package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import Model.ApiClient;
import Model.Roommate;
import Model.Status;
import Model.UserSession;
import View.IView;
import View.MockView;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests rely on running a local instance of the API.
 * Run order matters, so in this case it would be best to restart the local instance before each
 * run of the tests.
 */
class DashboardControllerTest {

    private UserSession userSession;
    private DashboardController controller;
    private IView mockView;

    @BeforeEach
    void setUp() {
        userSession = new UserSession("http://localhost:8080");
        mockView = new MockView();
        controller = new DashboardController(userSession, mockView);
    }

    @Test
    void registerUser() {
        boolean result = controller.register("testUser", "test123", "email@email.com");
        assertTrue(result);

        boolean registerSameUsername = controller.register("testUser", "test123", "email2@email" +
                ".com");
        assertFalse(registerSameUsername);

        boolean registerSameEmail = controller.register("testUser2", "test123", "email@email.com");
        assertFalse(registerSameEmail);
    }

    @Test
    void login() {
        boolean result = controller.register("testUser3", "test123", "email3@email.com");
        assertTrue(result);

        result = controller.login("testUser3", "test123");
        assertTrue(result);

        boolean incorrectUsername = controller.login("DNE", "test123");
        assertFalse(incorrectUsername);

        boolean incorrectPassword = controller.login("testUser3", "WRONG");
        assertFalse(incorrectPassword);
    }

    @Test
    void setRoommateProfile() {
        controller.register("testUser4", "test123", "email4@email.com");
        controller.login("testUser4", "test123");

        List<Integer> values = List.of(5, 5, 5, 5, 5, 5, 5, 5);
        controller.setRoommateProfile("New York", 1234, 123455, "Likes Clean rooms", values);

        controller.setRoommateProfile("New York", 1234, 1, "Likes Clean rooms", values);
    }

    private Roommate findUser(String username, List<Roommate> roommates) {
        for (Roommate roommate : roommates) {
            if (roommate.getName().equalsIgnoreCase(username)) {
                return roommate;
            }
        }
        return null;
    }

    @Test
    void sendAndAcceptRequestsAndRecommendations() {
        controller.register("testUser5", "test123", "email5@email.com");
        controller.login("testUser5", "test123");
        List<Integer> values = List.of(7, 2, 4, 1, 10, 9, 8, 6);
        controller.setRoommateProfile("New York", 1000, 2500, "Likes Clean rooms", values);

        controller.register("testUser6", "test123", "email6@email.com");
        controller.login("testUser6", "test123");
        controller.setRoommateProfile("New York", 900, 2200, "Likes Clean rooms", values);

        List<Roommate> recs = controller.getRecommendations();
        assertFalse(recs.isEmpty());
        assertNotNull(findUser("testUser5", recs));

        // Ensure that that user is in the sent list.
        assertTrue(controller.makeRoommateRequest(findUser("testUser5", recs).getId()));
        List<Roommate> sent = controller.getSentRoommates();
        assertFalse(sent.isEmpty());
        assertNotNull(findUser("testUser5", recs));

        // Ensure that testuser6 is in the requester list.
        controller.login("testUser5", "test123");
        List<Roommate> requestedBy = controller.getRoommateRequests();
        assertFalse(requestedBy.isEmpty());
        assertNotNull(findUser("testUser6", requestedBy));

        // Reject request
        assertTrue(controller.rejectRequest(findUser("testUser6", requestedBy).getRequestId()));
        requestedBy = controller.getRoommateRequests();
        assertEquals(Status.REJECTED, findUser("testUser6", requestedBy).getStatus());

        // Accept request
        assertTrue(controller.acceptRequest(findUser("testUser6", requestedBy).getRequestId()));
        requestedBy = controller.getAcceptedRoommates();
        assertEquals(Status.ACCEPTED, findUser("testUser6", requestedBy).getStatus());

        // Check from testuser6's side
        controller.login("testUser6", "test123");
        List<Roommate> accepted = controller.getAcceptedRoommates();
        assertFalse(accepted.isEmpty());
        assertNotNull(findUser("testUser5", accepted));
        assertEquals(Status.ACCEPTED, findUser("testUser5", accepted).getStatus());
    }



}