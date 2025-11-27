package Controller;

import java.util.List;

import Model.Roommate;
import Model.UserSession;
import View.View;

public class DashboardController {

    private UserSession userSession;
    private View view;

    public DashboardController(UserSession session, View view) {
        this.userSession = session;
        this.view = view;
        this.view.addController(this);
        view.login();
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

    public void setRoommateProfile(String city, Integer minBudget, Integer maxBudget,
                                   String notes) {
        try {
            userSession.setRoommateProfile(city, minBudget, maxBudget, notes);
            this.view.displayDashboard();
        } catch (IllegalStateException | IllegalArgumentException e) {
            this.view.displayError(e.getMessage());
        }
    }


    public List<Roommate> getAcceptedRoommates() {
        try {
            return this.userSession.getAcceptedRoommates();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public List<Roommate> getSentRoommates() {
        try {
            return this.userSession.getSentRoommates();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public List<Roommate> getReceivedRoommates() {
        try {
            return this.userSession.getReceivedRoommates();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return List.of();
        }
    }

    public List<?> searchRoommates(String username, String city, Integer minBudget,
                                   Integer maxBudget) {

        this.view.updateSearchResults(this.userSession.search());
        return null;
    }

    public List<?> getRoommateRequests() {
        try {
            return userSession.getRoommateRequests();
        } catch (IllegalStateException e) {
            this.view.displayError(e.getMessage());
            return null;
        }
    }

    public void makeRoommateRequest(long id) {
        try {
            userSession.makeRoommateRequest(id);
            this.view.displayMessage("Successful Roommate Request", "Roommate Request");
        } catch (Exception e) {
            this.view.displayError(e.getMessage());
        }
    }




}
