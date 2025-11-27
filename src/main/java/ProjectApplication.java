import Controller.DashboardController;
import Model.UserSession;
import View.DashboardView;
import View.View;

public class ProjectApplication {

    public static void main(String[] args) {

        UserSession userSession = new UserSession("http://localhost:8080");

        View view = new View();

        DashboardController controller = new DashboardController(userSession, view);


    }
}
