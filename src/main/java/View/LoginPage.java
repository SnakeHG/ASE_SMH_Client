package View;

import javax.swing.*;

public class LoginPage extends JFrame {


    public LoginPage() {
        setTitle("Login");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new JPanel());
        setLocationRelativeTo(null);
    }

    public void updateContentPane(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }


}
