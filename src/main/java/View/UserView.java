package View;

import java.awt.*;

import javax.swing.*;

public class UserView extends JFrame {

    private JButton request;

    public UserView(String username, String city, Integer minBudget, Integer maxBudget,
                    String notes, JButton request) {

        this.request = request;

        setTitle("User View");
        setBounds(100, 100, 450, 300);

        JPanel panel = new JPanel();
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

        // Request button
        request.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(request);

        getContentPane().add(panel);
    }
}
