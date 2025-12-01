package View;

import java.util.List;
import java.awt.*;

import javax.swing.*;

import Model.Roommate;

public class DashboardView extends JFrame {

    private final JPanel acceptedPanel;
    private final JPanel incomingPanel;
    private final JPanel outgoingPanel;
    private final JButton refreshButton;
    private final JButton logoutButton;

    private JPanel panel;

    private final RoommateProfileView profileView;
    private final SearchView searchView;
    private final RecView recView;

    public DashboardView(RoommateProfileView roommateProfileView, SearchView searchView,
                         RecView recView,
                         JButton refreshButton, JButton logoutButton) {
        this.profileView = roommateProfileView;
        this.searchView = searchView;
        this.recView = recView;
        this.refreshButton = refreshButton;
        this.logoutButton = logoutButton;
        acceptedPanel = new JPanel();
        incomingPanel = new JPanel();
        outgoingPanel = new JPanel();

        setTitle("Dashboard");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        add(panel);
        this.updateDashboardView();
    }

    public void updateDashboardView() {
        panel.removeAll();

        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        acceptedPanel.setBorder(BorderFactory.createTitledBorder("Accepted Roommates"));
        incomingPanel.setBorder(BorderFactory.createTitledBorder("Incoming Requests"));
        outgoingPanel.setBorder(BorderFactory.createTitledBorder("Outgoing Requests"));

        acceptedPanel.setLayout(new BoxLayout(acceptedPanel, BoxLayout.Y_AXIS));
        incomingPanel.setLayout(new BoxLayout(incomingPanel, BoxLayout.Y_AXIS));
        outgoingPanel.setLayout(new BoxLayout(outgoingPanel, BoxLayout.Y_AXIS));

        leftPanel.add(new JScrollPane(acceptedPanel));
        leftPanel.add(new JScrollPane(incomingPanel));
        leftPanel.add(new JScrollPane(outgoingPanel));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JButton createBtn = new JButton("Create Roommate Profile");
        createBtn.addActionListener(e -> replaceContent(profileView));
        JButton recBtn = new JButton("Get Recommendations");
        recBtn.addActionListener(e -> replaceContent(recView));
        JButton searchBtn = new JButton("Search Roommates");
        searchBtn.addActionListener(e -> replaceContent(searchView));

        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        recBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(this.refreshButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(this.logoutButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createBtn);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(recBtn);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(searchBtn);
        rightPanel.add(Box.createVerticalStrut(20));

        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        refresh();
    }

    public void updateAcceptedPanel(List<JButton> acceptedRoommates) {
        acceptedPanel.removeAll();
        acceptedPanel.setLayout(new BoxLayout(acceptedPanel, BoxLayout.Y_AXIS));

        for (JButton roommate : acceptedRoommates) {
            acceptedPanel.add(roommate);
        }
    }

    public void updateIncomingPanel(List<JButton> incomingRoommates) {
        incomingPanel.removeAll();
        incomingPanel.setLayout(new BoxLayout(incomingPanel, BoxLayout.Y_AXIS));

        for (JButton roommate : incomingRoommates) {
            incomingPanel.add(roommate);
        }
    }

    public void updateOutgoingPanel(List<JButton> outgoingRoommates) {
        outgoingPanel.removeAll();
        outgoingPanel.setLayout(new BoxLayout(outgoingPanel, BoxLayout.Y_AXIS));

        for (JButton roommate : outgoingRoommates) {
            outgoingPanel.add(roommate);
        }
    }

    public void updateSearch(List<JButton> searchResults) {
        this.searchView.updateResultPanel(searchResults);
    }

    public void updateRec(List<JButton> searchResults) {
        this.recView.updateResultPanel(searchResults);
    }

    private void replaceContent(JPanel panel) {
        this.panel.removeAll();
        this.panel.add(panel);
        refresh();
    }

    public void refresh() {
        revalidate();
        repaint();
    }




}
