import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProfileFrame extends JFrame {

    private final String[] userData;

    public ProfileFrame(String[] userData) {
        this.userData = userData;
        setTitle("Pac-Man — Profile");
        setSize(480, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        UITheme.styleFrame(this);
        setContentPane(buildContent());
        setVisible(true);
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

     
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titleLbl = UITheme.makeLabel("PROFILE", UITheme.FONT_TITLE, UITheme.ACCENT);
        header.add(titleLbl, BorderLayout.WEST);

        JButton closeBtn = UITheme.makeButton("✕", UITheme.BG_FIELD, UITheme.TEXT_MUTED);
        closeBtn.setPreferredSize(new Dimension(36, 36));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

  
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);
        centre.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        centre.add(makeAvatar());
        centre.add(Box.createVerticalStrut(16));

      
        String name = userData.length > 0 ? userData[0] : "Player";
        JLabel nameLbl = UITheme.makeLabel(name, new Font("Arial", Font.BOLD, 22), UITheme.TEXT_WHITE);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        centre.add(nameLbl);

        String email = userData.length > 1 ? userData[1] : "";
        JLabel emailLbl = UITheme.makeLabel(email, UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        emailLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        centre.add(emailLbl);
        centre.add(Box.createVerticalStrut(28));

 
        int highScore = UserStorage.getHighScore(email);
        centre.add(makeStatsRow(highScore));
        centre.add(Box.createVerticalStrut(28));

        
        centre.add(makeLeaderboard());
        centre.add(Box.createVerticalStrut(24));

   
        JButton playBtn = UITheme.makeButton("▶  PLAY GAME", UITheme.ACCENT, UITheme.BG_DARK);
        playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playBtn.setMaximumSize(new Dimension(300, 44));
        playBtn.addActionListener(e -> { dispose(); new GameFrame(userData); });
        centre.add(playBtn);
        centre.add(Box.createVerticalStrut(12));

        JButton logoutBtn = UITheme.makeButton("LOGOUT", UITheme.BG_FIELD, UITheme.ERROR_RED);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(300, 44));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        centre.add(logoutBtn);

        root.add(centre, BorderLayout.CENTER);
        return root;
    }

    private JPanel makeAvatar() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.ACCENT);
                g2.fillOval(10, 10, 80, 80);
           
                g2.setColor(UITheme.ACCENT);
                g2.fillArc(14, 14, 72, 72, 30, 300);
        
                g2.setColor(UITheme.BG_DARK);
                g2.fillOval(50, 22, 10, 10);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 100));
        p.setMaximumSize(new Dimension(100, 100));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }

   
    private JPanel makeStatsRow(int highScore) {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(380, 90));

        row.add(makeStatCard("🏆  High Score", String.valueOf(highScore), UITheme.ACCENT));
        row.add(makeStatCard("🎮  Rank", getRank(highScore), UITheme.GHOST_BLUE));
        return row;
    }

    private JPanel makeStatCard(String label, String value, Color valueColor) {
        JPanel card = UITheme.makeCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel lbl = UITheme.makeLabel(label, UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel val = UITheme.makeLabel(value, UITheme.FONT_SCORE, valueColor);
        val.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lbl);
        card.add(Box.createVerticalStrut(6));
        card.add(val);
        return card;
    }

    private JPanel makeLeaderboard() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(380, 200));

        JLabel title = UITheme.makeLabel("🏅  TOP SCORES", UITheme.FONT_LABEL, UITheme.TEXT_WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(title);
        wrapper.add(Box.createVerticalStrut(10));

        List<String[]> top = UserStorage.getTopScores(5);
        String currentEmail = userData.length > 1 ? userData[1] : "";

        for (int i = 0; i < top.size(); i++) {
            String[] row   = top.get(i);
            String rowName  = row.length > 0 ? row[0] : "?";
            String rowEmail = row.length > 1 ? row[1] : "";
            int    rowScore = row.length > 4 ? tryParse(row[4]) : 0;

            boolean isMe = rowEmail.equalsIgnoreCase(currentEmail);

            JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
            rowPanel.setOpaque(false);
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            rowPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

            String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : (i+1) + ".";
            Color nameColor = isMe ? UITheme.ACCENT : UITheme.TEXT_WHITE;

            JLabel rankLbl  = UITheme.makeLabel(medal,                    UITheme.FONT_LABEL, UITheme.TEXT_MUTED);
            JLabel nameLbl  = UITheme.makeLabel(rowName + (isMe ? " ★":""),UITheme.FONT_LABEL, nameColor);
            JLabel scoreLbl = UITheme.makeLabel(String.valueOf(rowScore),  UITheme.FONT_LABEL, UITheme.ACCENT);

            rankLbl.setPreferredSize(new Dimension(30, 24));
            rowPanel.add(rankLbl, BorderLayout.WEST);
            rowPanel.add(nameLbl, BorderLayout.CENTER);
            rowPanel.add(scoreLbl,BorderLayout.EAST);

            wrapper.add(rowPanel);
        }
        return wrapper;
    }

    private String getRank(int score) {
        if (score >= 5000) return "Legend";
        if (score >= 2000) return "Pro";
        if (score >= 500)  return "Regular";
        return "Rookie";
    }

    private int tryParse(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
