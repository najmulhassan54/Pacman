import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField     emailField    = UITheme.makeField();
    private JPasswordField passField     = UITheme.makePassField();
    private JLabel         errorLabel    = UITheme.makeLabel("", UITheme.FONT_SMALL, UITheme.ERROR_RED);

    public LoginFrame() {
        setTitle("Pac-Man — Login");
        setSize(420, 540);
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
        card.setBorder(BorderFactory.createEmptyBorder(36, 40, 36, 40));
        card.setPreferredSize(new Dimension(340, 440));

        card.add(makeDots());
        card.add(Box.createVerticalStrut(8));

        JLabel title = UITheme.makeLabel("PAC-MAN", UITheme.FONT_TITLE, UITheme.ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel sub = UITheme.makeLabel("Sign in to play", UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        card.add(fieldRow("Email", emailField));
        card.add(Box.createVerticalStrut(14));
        card.add(fieldRow("Password", passField));
        card.add(Box.createVerticalStrut(6));

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(18));

        JButton loginBtn = UITheme.makeButton("LOGIN", UITheme.ACCENT, UITheme.BG_DARK);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(260, 42));
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(14));

        card.add(makeDivider("or"));
        card.add(Box.createVerticalStrut(14));

        JButton regBtn = UITheme.makeButton("CREATE ACCOUNT", UITheme.BG_FIELD, UITheme.TEXT_WHITE);
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setMaximumSize(new Dimension(260, 42));
        regBtn.addActionListener(e -> { dispose(); new RegisterFrame(); });
        card.add(regBtn);

        // Enter key support
        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        };
        emailField.addKeyListener(enter);
        passField.addKeyListener(enter);

        root.add(card);
        return root;
    }

    private void doLogin() {
        String email = emailField.getText().trim();
        String pass  = new String(passField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        if (UserStorage.validateLogin(email, pass)) {
            String[] userData = UserStorage.getUser(email);
            dispose();
            new GameFrame(userData);
        } else {
            showError("Incorrect email or password.");
            passField.setText("");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
    }


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

    private JPanel makeDots() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int[] sizes = {10, 7, 5, 4, 4};
                int x = (getWidth() - 60) / 2;
                for (int i = 0; i < sizes.length; i++) {
                    float alpha = 1f - i * 0.15f;
                    g2.setColor(new Color(1f, 0.78f, 0f, alpha));
                    g2.fillOval(x + i*13, (20 - sizes[i])/2, sizes[i], sizes[i]);
                }
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(260, 20));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return p;
    }

    private JPanel makeDivider(String text) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JSeparator left  = new JSeparator(); left.setForeground(UITheme.BORDER_DIM);
        JSeparator right = new JSeparator(); right.setForeground(UITheme.BORDER_DIM);
        JLabel mid = UITheme.makeLabel(text, UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        mid.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(left, BorderLayout.WEST);
        p.add(mid,  BorderLayout.CENTER);
        p.add(right,BorderLayout.EAST);
        return p;
    }
}
