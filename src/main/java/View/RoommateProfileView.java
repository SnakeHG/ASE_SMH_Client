package View;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RoommateProfileView extends JPanel {

    private final JTextField cityField;
    private final JTextField minBudgetField;
    private final JTextField maxBudgetField;
    private final JTextArea notesArea;
    private final List<JSpinner> personality;
    private final JButton btn;
    private final JButton btn2;

    public RoommateProfileView(JTextField cityField,
                               JTextField minBudgetField,
                               JTextField maxBudgetField,
                               JTextArea notesArea,
                               List<JSpinner> personalityQuestions,
                               String[] questions,
                               JButton btn,
                               JButton cancelButton) {
        this.cityField = cityField;
        this.minBudgetField = minBudgetField;
        this.maxBudgetField = maxBudgetField;
        this.notesArea = notesArea;
        this.personality = personalityQuestions;
        this.btn = btn;
        this.btn2 = cancelButton;

        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        setNumericFilter(minBudgetField);
        setNumericFilter(maxBudgetField);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Row 1: City
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Preferred City:"), gbc);

        gbc.gridx = 1;
        formPanel.add(cityField, gbc);

        // Row 2: Min Budget
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Min Budget:"), gbc);

        gbc.gridx = 1;
        formPanel.add(minBudgetField, gbc);

        // Row 3: Max Budget
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Max Budget:"), gbc);

        gbc.gridx = 1;
        formPanel.add(maxBudgetField, gbc);

        // Row 4: Notes
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(notesArea);
        formPanel.add(scroll, gbc);

        // Row 5/6 personality
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Personality Questions (Answer from 1-10):"), gbc);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        JPanel personalityPanel = new JPanel(new GridBagLayout());
        GridBagConstraints pg = new GridBagConstraints();
        pg.insets = new Insets(3, 3, 3, 3);
        pg.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < personalityQuestions.size(); i++) {
            pg.gridx = 0;
            pg.gridy = i;
            JPanel questionPanel = new JPanel(new BorderLayout(10, 0));
            questionPanel.add(new JLabel(questions[i]), BorderLayout.WEST);
            questionPanel.add(personality.get(i), BorderLayout.EAST);
            personalityPanel.add(questionPanel, pg);
        }
        formPanel.add(personalityPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btn);
        buttonPanel.add(btn2);

        // Final layout
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void setNumericFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string.matches("\\d*")) { // only allow digits
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }





}
