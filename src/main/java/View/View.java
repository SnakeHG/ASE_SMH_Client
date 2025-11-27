package View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Controller.DashboardController;
import Model.Roommate;

public class View {

    private DashboardController dashboardController;
    private DashboardView dashboardView;
    private Login login;

    public void addController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void login() {
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> dashboardController.login(username.getText(),
                new String(password.getPassword())));
        login = new Login(username, password, loginButton);
        login.setVisible(true);
    }

    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void initDashboard() {
        login.setVisible(false);

        RoommateProfileView roommateProfileView = this.createRoommateProfileView();
        SearchView searchView = this.createSearchView();


        dashboardView = new DashboardView(roommateProfileView, searchView);
        this.displayDashboard();
        dashboardView.setVisible(true);
    }

    public void updateSearchResults(List<Roommate> roommates) {
        this.dashboardView.updateSearch(createRoommatesWithRequest(roommates));
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


    private RoommateProfileView createRoommateProfileView() {

        JButton createBtn = new JButton("Create Roommate Profile");
        JTextField cityField = new JTextField(20);
        JTextField minBudgetField = new JTextField(10);
        JTextField maxBudgetField = new JTextField(10);
        JTextArea notesArea = new JTextArea(5, 20);
        createBtn.addActionListener(e -> {
            String city = cityField.getText().trim();
            String minStr = minBudgetField.getText().trim();
            String maxStr = maxBudgetField.getText().trim();
            String notes = notesArea.getText().trim();

            int minBudget = minStr.isEmpty() ? 0 : Integer.parseInt(minStr);
            int maxBudget = maxStr.isEmpty() ? 0 : Integer.parseInt(maxStr);

            System.out.printf("%s,%d,%d,%s\n", city, minBudget, maxBudget, notes);

            //dashboardController.setRoommateProfile(city, minBudget, maxBudget, notes);
            displayDashboard();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> displayDashboard());

        return new RoommateProfileView(cityField, minBudgetField,
                maxBudgetField, notesArea, createBtn, cancelButton);
    }

    public void displayDashboard() {
        // Update the roommate request lists first
        List<Roommate> accepted = this.dashboardController.getAcceptedRoommates();
        List<Roommate> recieved = this.dashboardController.getReceivedRoommates();
        List<Roommate> sent = this.dashboardController.getSentRoommates();
        this.dashboardView.updateAcceptedPanel(this.createRoommatesWithRequest(accepted));
        this.dashboardView.updateIncomingPanel(this.createRoommatesWithRequest(recieved));
        this.dashboardView.updateOutgoingPanel(this.createRoommatesWithRequest(sent));
        this.dashboardView.updateDashboardView();

    }

    private JButton createRequestBtn(long id) {
        JButton createBtn = new JButton("Request");
        createBtn.addActionListener(e -> {
            this.dashboardController.makeRoommateRequest(id);
        });

        return createBtn;
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


}
