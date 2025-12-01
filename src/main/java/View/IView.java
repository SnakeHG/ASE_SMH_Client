package View;

import java.util.List;

import Controller.DashboardController;
import Model.Roommate;

public interface IView {

    void addController(DashboardController controller);

    void login();

    void register();

    void initDashboard();

    void displayError(String error);

    void displayDashboard();

    void updateSearchResults(List<Roommate> roommates);

    void updateRecResults(List<Roommate> roommates);

    void displayMessage(String message, String title);
}
