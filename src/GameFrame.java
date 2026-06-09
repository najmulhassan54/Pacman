import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final String[] userData;

    public GameFrame(String[] userData) {
        this.userData = userData;
        setTitle("Pac-Man — Select Difficulty");
        setSize(360, 420);
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
        card.setPreferredSize(new Dimension(300, 360));

        // Greeting
        String name = userData != null && userData.length > 0 ? userData[0] : "Player";
        JLabel greet = UITheme.makeLabel("Hello, " + name + "!", UITheme.FONT_LABEL, UITheme.TEXT_MUTED);
        greet.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(greet);
        card.add(Box.createVerticalStrut(4));

        JLabel title = UITheme.makeLabel("SELECT DIFFICULTY", UITheme.FONT_TITLE, UITheme.ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(28));

        card.add(diffBtn("  EASY",   "5 lives  •  Normal speed",  new Color(80,200,120),  Difficulty.EASY));
        card.add(Box.createVerticalStrut(12));
        card.add(diffBtn("  MEDIUM", "4 lives  •  Faster ghosts", new Color(255,180, 50),  Difficulty.MEDIUM));
        card.add(Box.createVerticalStrut(12));
        card.add(diffBtn("  HARD",   "3 lives  •  Fastest ghosts",new Color(255, 80, 80),  Difficulty.HARD));
        card.add(Box.createVerticalStrut(24));


        JButton profileBtn = UITheme.makeButton("👤  VIEW PROFILE", UITheme.BG_FIELD, UITheme.TEXT_WHITE);
        profileBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileBtn.setMaximumSize(new Dimension(260, 38));
        profileBtn.addActionListener(e -> { dispose(); new ProfileFrame(userData); });
        card.add(profileBtn);

        root.add(card);
        return root;
    }

    private JPanel diffBtn(String label, String desc, Color color, Difficulty diff) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JButton btn = UITheme.makeButton(label, color, Color.WHITE);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.addActionListener(e -> startGame(diff));

        JLabel descLbl = UITheme.makeLabel(desc, UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        descLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(btn);
        p.add(Box.createVerticalStrut(3));
        p.add(descLbl);
        return p;
    }

    private void startGame(Difficulty diff) {
        dispose();
        JFrame gameFrame = new JFrame("Pac-Man — " + diff.name());
        PacMan game = new PacMan(diff, userData, gameFrame);
        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
        game.requestFocusInWindow();
    }
}
