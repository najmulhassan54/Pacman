import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField     nameField   = UITheme.makeField();
    private JTextField     emailField  = UITheme.makeField();
    private JTextField     numberField = UITheme.makeField();
    private JPasswordField passField   = UITheme.makePassField();
    private JPasswordField confirmField= UITheme.makePassField();
    private JLabel         errorLabel  = UITheme.makeLabel("", UITheme.FONT_SMALL, UITheme.ERROR_RED);

    public RegisterFrame() {
        setTitle("Pac-Man — Create Account");
        setSize(420, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        UITheme.styleFrame(this);

        setContentPane(buildContent());
        setVisible(true);
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.makeCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));
        card.setPreferredSize(new Dimension(350, 570));

  
        JLabel title = UITheme.makeLabel("CREATE ACCOUNT", UITheme.FONT_TITLE, UITheme.ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel sub = UITheme.makeLabel("Join the game", UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(24));

    
        card.add(fieldRow("Full Name",       nameField));    card.add(Box.createVerticalStrut(12));
        card.add(fieldRow("Email",           emailField));   card.add(Box.createVerticalStrut(12));
        card.add(fieldRow("Phone Number",    numberField));  card.add(Box.createVerticalStrut(12));
        card.add(fieldRow("Password",        passField));    card.add(Box.createVerticalStrut(12));
        card.add(fieldRow("Confirm Password",confirmField)); card.add(Box.createVerticalStrut(6));

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(16));

      
        JButton regBtn = UITheme.makeButton("REGISTER", UITheme.ACCENT, UITheme.BG_DARK);
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setMaximumSize(new Dimension(260, 42));
        regBtn.addActionListener(e -> doRegister());
        card.add(regBtn);
        card.add(Box.createVerticalStrut(14));

       
        JButton backBtn = UITheme.makeButton("BACK TO LOGIN", UITheme.BG_FIELD, UITheme.TEXT_WHITE);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(260, 42));
        backBtn.addActionListener(e -> { dispose(); new LoginFrame(); });
        card.add(backBtn);

        root.add(card);
        return root;
    }


    private void doRegister() {
        String name    = nameField.getText().trim();
        String email   = emailField.getText().trim();
        String number  = numberField.getText().trim();
        String pass    = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (name.isEmpty() || email.isEmpty() || number.isEmpty() || pass.isEmpty()) {
            showError("Please fill in all fields."); return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email."); return;
        }
        if (pass.length() < 6) {
            showError("Password must be at least 6 characters."); return;
        }
        if (!pass.equals(confirm)) {
            showError("Passwords do not match."); return;
        }
        if (UserStorage.emailExists(email)) {
            showError("Email already registered."); return;
        }

        UserStorage.saveUser(name, email, number, pass);
        JOptionPane.showMessageDialog(this,
            "<html><b>Account created!</b><br>Welcome, " + name + "!</html>",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginFrame();
    }

    private void showError(String msg) { errorLabel.setText(msg); }

    private JPanel fieldRow(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = UITheme.makeLabel(label, UITheme.FONT_LABEL, UITheme.TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(field);
        return p;
    }
}
