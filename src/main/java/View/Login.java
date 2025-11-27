package View;

import java.awt.*;

import javax.swing.*;

public class Login extends JFrame {

    public Login(JTextField username, JPasswordField password, JButton login) {
        setTitle("Login");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(login);

        add(panel);
        setLocationRelativeTo(null);
    }


}
