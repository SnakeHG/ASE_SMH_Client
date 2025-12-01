package View;

import java.awt.*;

import javax.swing.*;

public class Login extends JPanel {


    public Login(JTextField username, JPasswordField password, JButton login, JButton register) {

        setLayout(new GridLayout(3, 2, 5, 5));
        add(new JLabel("Username:"));
        add(username);
        add(new JLabel("Password:"));
        add(password);
        add(login);
        add(register);

    }


}
