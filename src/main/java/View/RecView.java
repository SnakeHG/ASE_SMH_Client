package View;

import java.awt.*;
import java.util.List;

import javax.swing.*;

public class RecView extends JPanel {

    private JPanel resultPanel;
    private JPanel userPanel;

    public RecView(JButton recButton, JButton cancelButton) {
        this.resultPanel = new JPanel();
        this.resultPanel.setBorder(BorderFactory.createTitledBorder("Recommendation Results"));
        this.resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(recButton);
        buttonPanel.add(cancelButton);
        this.resultPanel.add(buttonPanel, BorderLayout.NORTH);

        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(userPanel);
        this.resultPanel.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(resultPanel, BorderLayout.CENTER);
    }

    public void updateResultPanel(List<JButton> users) {
        this.userPanel.removeAll();
        this.userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        for (JButton roommate : users) {
            this.userPanel.add(roommate);
        }

        revalidate();
        repaint();
    }


}
