package View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Controller.DashboardController;
import Model.Roommate;

public class View implements IView{

    private DashboardController dashboardController;
    private DashboardView dashboardView;
    private LoginPage loginPage;
    private String[] questions;

    public View() {
        this.questions = new String[]{
                "1. I prefer to keep shared spaces clean and organized.",
                "2. I am comfortable with guests coming over frequently.",
                "3. I like having a generally quiet living environment.",
                "4. I communicate openly about issues or concerns.",
                "5. I go to bed early and wake up early.",
                "6. I am okay with sharing household items.",
                "7. I prefer to split chores on a regular schedule.",
                "8. I am comfortable with pets in the living space."
        };
        loginPage = new LoginPage();
    }

    @Override
    public void addController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @Override
    public void login() {
        if (this.dashboardView != null) {
            this.dashboardView.setVisible(false);
        }
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> dashboardController.login(username.getText(),
                new String(password.getPassword())));
        JButton registerButton = new JButton("New Account");
        registerButton.addActionListener(e -> register());
        Login login = new Login(username, password, loginButton, registerButton);
        this.loginPage.updateContentPane(login);
        this.loginPage.setVisible(true);
    }


    @Override
    public void register() {
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField email = new JTextField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            dashboardController.register(username.getText(),
                new String(password.getPassword()), email.getText());
            login();
        });
        Register register = new Register(username, password, email, loginButton, registerButton);
        this.loginPage.updateContentPane(register);
        this.loginPage.setVisible(true);
    }

    @Override
    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void displayMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void initDashboard() {
        loginPage.setVisible(false);

        RoommateProfileView roommateProfileView = this.createRoommateProfileView();
        SearchView searchView = this.createSearchView();
        RecView recView = this.createRecView();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> this.displayDashboard());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> this.dashboardController.logout());

        dashboardView = new DashboardView(roommateProfileView, searchView, recView, refreshButton,
                logoutButton);
        this.displayDashboard();
        dashboardView.setVisible(true);
    }

    @Override
    public void updateSearchResults(List<Roommate> roommates) {
        this.dashboardView.updateSearch(createRoommatesWithRequest(roommates));
    }

    @Override
    public void updateRecResults(List<Roommate> roommates) {
        this.dashboardView.updateRec(createRoommatesWithRequest(roommates));
    }

    private SearchView createSearchView() {
        JButton createBtn = new JButton("Search");
        JTextField usernameField = new JTextField(20);
        JTextField cityField = new JTextField(20);
        JTextField minBudgetField = new JTextField(10);
        JTextField maxBudgetField = new JTextField(10);
        createBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String city = cityField.getText().trim();
            String minStr = minBudgetField.getText().trim();
            String maxStr = maxBudgetField.getText().trim();

            int minBudget = minStr.isEmpty() ? 0 : Integer.parseInt(minStr);
            int maxBudget = maxStr.isEmpty() ? 0 : Integer.parseInt(maxStr);

            System.out.printf("%s,%d,%d,%s\n", city, minBudget, maxBudget, username);

            dashboardController.searchRoommates(username, city,
                    minBudget, maxBudget);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> displayDashboard());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        JPanel searchFields = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchFields.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        searchFields.add(usernameField, gbc);

        gbc.gridx = 2;
        searchFields.add(new JLabel("Preferred City:"), gbc);
        gbc.gridx = 3;
        searchFields.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchFields.add(new JLabel("Min Budget:"), gbc);

        gbc.gridx = 1;
        searchFields.add(minBudgetField, gbc);

        // Row 3: Max Budget
        gbc.gridx = 2;
        searchFields.add(new JLabel("Max Budget:"), gbc);

        gbc.gridx = 3;
        searchFields.add(maxBudgetField, gbc);

        searchPanel.add(searchFields, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(createBtn);
        buttonPanel.add(cancelButton);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);


        return new SearchView(searchPanel);
    }

    private RecView createRecView() {
        JButton recButton = new JButton("Get Recommendations");
        recButton.addActionListener(e -> this.dashboardController.getRecommendations());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> displayDashboard());
        return new RecView(recButton, cancelButton);
    }

    private List<JSpinner> personality() {

        List<JSpinner> spinners = new ArrayList<>();
        for (String q : questions) {

            // Enforce 1–10 input
            JSpinner spinner = new JSpinner(
                    new SpinnerNumberModel(5, 1, 10, 1)
            );

            ((JSpinner.DefaultEditor) spinner.getEditor())
                    .getTextField().setEditable(false);
            spinners.add(spinner);
        }
        return spinners;
    }


    private RoommateProfileView createRoommateProfileView() {

        JButton createBtn = new JButton("Create Roommate Profile");
        JTextField cityField = new JTextField(20);
        JTextField minBudgetField = new JTextField(10);
        JTextField maxBudgetField = new JTextField(10);
        JTextArea notesArea = new JTextArea(5, 20);
        List<JSpinner> personalityQuestions = personality();
        createBtn.addActionListener(e -> {
            String city = cityField.getText().trim();
            String minStr = minBudgetField.getText().trim();
            String maxStr = maxBudgetField.getText().trim();
            String notes = notesArea.getText().trim();

            int minBudget = minStr.isEmpty() ? 0 : Integer.parseInt(minStr);
            int maxBudget = maxStr.isEmpty() ? 0 : Integer.parseInt(maxStr);
            List<Integer> personalityValues = new ArrayList<>();
            for (JSpinner spinner : personalityQuestions) {
                personalityValues.add((Integer) spinner.getValue());
            }

            dashboardController.setRoommateProfile(city, minBudget, maxBudget, notes, personalityValues);
            displayDashboard();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> displayDashboard());

        return new RoommateProfileView(cityField, minBudgetField,
                maxBudgetField, notesArea, personalityQuestions, questions, createBtn,
                cancelButton);
    }

    @Override
    public void displayDashboard() {
        // Update the roommate request lists first
        List<Roommate> accepted = this.dashboardController.getAcceptedRoommates();
        List<Roommate> recieved = this.dashboardController.getReceivedRoommates();
        List<Roommate> sent = this.dashboardController.getSentRoommates();
        this.dashboardView.updateAcceptedPanel(this.createRoommatesWithAcceptReject(accepted));
        this.dashboardView.updateIncomingPanel(this.createRoommatesWithAcceptReject(recieved));
        this.dashboardView.updateOutgoingPanel(this.createRoommatesWithNone(sent));
        this.dashboardView.updateDashboardView();

    }

    private JButton createRequestBtn(long id) {
        JButton createBtn = new JButton("Request");
        createBtn.addActionListener(e -> {
            this.dashboardController.makeRoommateRequest(id);
        });

        return createBtn;
    }

    private JButton createAcceptBtn(long id) {
        JButton createBtn = new JButton("Accept");
        createBtn.addActionListener(e -> {
            this.dashboardController.acceptRequest(id);
        });

        return createBtn;
    }

    private JButton createRejectBtn(long id) {
        JButton createBtn = new JButton("Reject");
        createBtn.addActionListener(e -> {
            this.dashboardController.rejectRequest(id);
        });

        return createBtn;
    }

    private List<JButton> createRoommatesWithNone(List<Roommate> roommates) {
        List<JButton> roommatesButtons = new ArrayList<>();
        for (Roommate roommate : roommates) {
            JButton rmBtn = new JButton(roommate.getName());
            rmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, rmBtn.getPreferredSize().height));
            rmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            rmBtn.addActionListener(e -> {
                UserView view = new UserView(
                        roommate.getName(),
                        roommate.getCity(),
                        roommate.getMinBudget(),
                        roommate.getMaxBudget(),
                        roommate.getNotes()
                );
                view.setVisible(true);
            });
            roommatesButtons.add(rmBtn);
        }
        return roommatesButtons;
    }


    private List<JButton> createRoommatesWithRequest(List<Roommate> roommates) {
        List<JButton> roommatesButtons = new ArrayList<>();
        for (Roommate roommate : roommates) {
            JButton rmBtn = new JButton(roommate.getName());
            rmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, rmBtn.getPreferredSize().height));
            rmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            rmBtn.addActionListener(e -> {
                UserView view = new UserView(
                        roommate.getName(),
                        roommate.getCity(),
                        roommate.getMinBudget(),
                        roommate.getMaxBudget(),
                        roommate.getNotes(),
                        createRequestBtn(roommate.getId())
                );
                view.setVisible(true);
            });
            roommatesButtons.add(rmBtn);
        }
        return roommatesButtons;
    }

    private List<JButton> createRoommatesWithAcceptReject(List<Roommate> roommates) {
        List<JButton> roommatesButtons = new ArrayList<>();
        for (Roommate roommate : roommates) {
            JButton rmBtn = new JButton(roommate.getName());
            rmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, rmBtn.getPreferredSize().height));
            rmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            rmBtn.addActionListener(e -> {
                UserView view = new UserView(
                        roommate.getName(),
                        roommate.getCity(),
                        roommate.getMinBudget(),
                        roommate.getMaxBudget(),
                        roommate.getNotes(),
                        createAcceptBtn(roommate.getRequestId()),
                        createRejectBtn(roommate.getRequestId())
                );
                view.setVisible(true);
            });
            roommatesButtons.add(rmBtn);
        }
        return roommatesButtons;
    }


}
