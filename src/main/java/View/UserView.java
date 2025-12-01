package View;

import java.awt.*;

import javax.swing.*;

public class UserView extends JFrame {

    private final JPanel panel;

    public UserView(String username, String city, Integer minBudget, Integer maxBudget,
                    String notes) {

        panel = new JPanel();

        setTitle("User View");
        setBounds(100, 100, 450, 300);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Username
        panel.add(new JLabel("Username: " + username));

        // City
        panel.add(new JLabel("City: " + city));

        // Budget Range
        panel.add(new JLabel("Budget: $" + minBudget + " - $" + maxBudget));

        // Notes (multi-line)
        panel.add(new JLabel("Notes:"));
        JTextArea notesArea = new JTextArea(notes);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setEditable(false);
        notesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(notesArea);

        getContentPane().add(panel);
    }

    public UserView(String username, String city, Integer minBudget, Integer maxBudget,
                    String notes, JButton request) {

        this(username, city, minBudget, maxBudget, notes);

        // Request button
        request.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(request);
    }

    public UserView(String username, String city, Integer minBudget, Integer maxBudget,
                    String notes, JButton accept, JButton reject) {

        this(username, city, minBudget, maxBudget, notes);

        // Accept/Reject Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(accept);
        buttonPanel.add(reject);
        panel.add(buttonPanel);
    }

}
