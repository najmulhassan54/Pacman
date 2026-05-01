import javax.swing.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField emailField;
    JPasswordField passField;
    JButton loginButton, registerButton;

    public LoginFrame() {

        setTitle("Please Login to Play Pacman");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 20, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 20, 150, 25);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(100, 60, 150, 25);
        add(passField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 100, 150, 25);
        loginButton.addActionListener(this);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(100, 130, 150, 25);
        registerButton.addActionListener(this);
        add(registerButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == registerButton) {
            dispose();
            new RegisterFrame();
            return;
        }

        String email = emailField.getText();
        String pass = new String(passField.getPassword());

        if (UserStorage.validateLogin(email, pass)) {
            dispose();
            new GameFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login!");
        }
    }
}