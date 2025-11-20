import Controller.DashboardController;
import Model.UserSession;

public class ProjectApplication {

    public static void main(String[] args) {

        UserSession userSession = new UserSession();

        DashboardController controller = new DashboardController(userSession);


    }
}
