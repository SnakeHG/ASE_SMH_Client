package View;

import java.awt.*;

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
    private final JButton btn;
    private final JButton btn2;

    public RoommateProfileView(JTextField cityField,
                               JTextField minBudgetField,
                               JTextField maxBudgetField,
                               JTextArea notesArea,
                               JButton btn,
                               JButton cancelButton) {
        this.cityField = cityField;
        this.minBudgetField = minBudgetField;
        this.maxBudgetField = maxBudgetField;
        this.notesArea = notesArea;
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
