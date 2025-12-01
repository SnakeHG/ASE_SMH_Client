package Controller;

import java.util.List;

import Model.Roommate;
import Model.Status;
import Model.UserSession;
import View.IView;

public class DashboardController {

    private UserSession userSession;
    private IView view;

    public DashboardController(UserSession session, IView view) {
        this.userSession = session;
        this.view = view;
        this.view.addController(this);
        view.login();
    }

    public boolean register(String username, String password, String email) {
        try {
            userSession.register(username, password, email);
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean login(String username, String password) {
        try {
            userSession.login(username, password);
            this.view.initDashboard();
            return true;
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return false;
        }
    }

    public void logout() {
        this.userSession.logout();
        this.view.login();
    }

    public void setRoommateProfile(String city, Integer minBudget, Integer maxBudget,
                                   String notes, List<Integer> personality) {
        try {
            userSession.setRoommateProfile(city, minBudget, maxBudget, notes, personality);
            this.view.displayDashboard();
        } catch (IllegalStateException | IllegalArgumentException e) {
            this.view.displayError(e.getMessage());
        }
    }


    public List<Roommate> getAcceptedRoommates() {
        try {
            return this.userSession.filterOutThisUser(userSession.getAcceptedRoommates());
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    private List<Roommate> filterOutStatus(List<Roommate> roommates, Status status) {
        return roommates.stream().filter(r -> !r.getStatus().equals(status)).toList();
    }

    public List<Roommate> getSentRoommates() {
        try {
            return filterOutStatus(this.userSession.getSentRoommates(), Status.ACCEPTED);
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public List<Roommate> getReceivedRoommates() {
        try {
            return filterOutStatus(this.userSession.getReceivedRoommates(), Status.ACCEPTED);
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public List<Roommate> searchRoommates(String username, String city, Integer minBudget,
                                   Integer maxBudget) {
        List<Roommate> roommates = this.userSession.search();
        this.view.updateSearchResults(this.userSession.filterOutThisUser(roommates));
        return roommates;
    }

    public List<Roommate> getRoommateRequests() {
        try {
            return userSession.getReceivedRoommates();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public boolean makeRoommateRequest(long id) {
        try {
            userSession.makeRoommateRequest(id);
            this.view.displayMessage("Successful Roommate Request", "Roommate Request");
            return true;
        } catch (Exception e) {
            this.view.displayError(e.getMessage());
        }
        return false;
    }

    public List<Roommate> getRecommendations() {
        try {
            List<Roommate> roommates =  userSession.getRecommendations();
            this.view.updateRecResults(roommates);
            return roommates;
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public boolean acceptRequest(long requestId) {
        try {
            userSession.acceptRoommateRequest(requestId);
            this.view.displayDashboard();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean rejectRequest(long requestId) {
        try {
            userSession.rejectRoommateRequest(requestId);
            this.view.displayDashboard();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return false;
        }
        return true;
    }



}
