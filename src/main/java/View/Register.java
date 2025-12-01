package View;

import java.awt.*;

import javax.swing.*;

public class Register extends JPanel {

    public Register(JTextField username, JPasswordField password, JTextField email, JButton login,
                    JButton register) {

        setLayout(new GridLayout(4, 2, 5, 5));
        add(new JLabel("Username:"));
        add(username);

        add(new JLabel("Password:"));
        add(password);

        add(new JLabel("Email:"));
        add(email);

        add(login);
        add(register);
    }
}
