package View;

import java.awt.*;

import java.util.List;

import javax.swing.*;


public class SearchView extends JPanel {

    private JPanel searchPanel;
    private JPanel resultPanel;

    public SearchView(JPanel searchPanel) {
        this.searchPanel = searchPanel;
        this.resultPanel = new JPanel();
        this.resultPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        this.resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultPanel), BorderLayout.CENTER);
    }

    public void updateResultPanel(List<JButton> users) {
        this.resultPanel.removeAll();
        this.resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        for (JButton roommate : users) {
            this.resultPanel.add(roommate);
        }

        revalidate();
        repaint();
    }


}
