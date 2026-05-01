import javax.swing.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame implements ActionListener {

    JTextField nameField, emailField, numberField;
    JPasswordField passField;

    public RegisterFrame() {
        setTitle("Register");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Labels & Fields
        nameField   = addField("Name:", 20);
        emailField  = addField("Email:", 60);
        numberField = addField("Number:", 100);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 140, 100, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(120, 140, 180, 25);
        add(passField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(120, 190, 180, 30);
        registerButton.addActionListener(this);
        add(registerButton);

        setVisible(true);
    }

    // Reusable method
    private JTextField addField(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(20, y, 100, 25);
        add(label);

        JTextField field = new JTextField();
        field.setBounds(120, y, 180, 25);
        add(field);

        return field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UserStorage.saveUser(
            nameField.getText(),
            emailField.getText(),
            numberField.getText(),
            new String(passField.getPassword())
        );

        JOptionPane.showMessageDialog(this, "Registered Successfully!");
        dispose();
        new LoginFrame();
    }
}