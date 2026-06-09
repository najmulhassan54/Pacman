import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class UITheme {

    public static final Color BG_DARK      = new Color(10,  10,  30);
    public static final Color BG_PANEL     = new Color(18,  18,  45);
    public static final Color BG_FIELD     = new Color(28,  28,  60);
    public static final Color ACCENT       = new Color(255, 200,  0);   // pac-man yellow
    public static final Color ACCENT_HOVER = new Color(255, 230, 80);
    public static final Color TEXT_WHITE   = new Color(240, 240, 255);
    public static final Color TEXT_MUTED   = new Color(150, 150, 180);
    public static final Color ERROR_RED    = new Color(255,  80,  80);
    public static final Color SUCCESS_GREEN= new Color( 80, 220, 120);
    public static final Color GHOST_BLUE   = new Color( 50, 150, 255);
    public static final Color BORDER_DIM   = new Color( 50,  50,  90);

    public static final Font FONT_TITLE  = new Font("Arial", Font.BOLD,  26);
    public static final Font FONT_LABEL  = new Font("Arial", Font.BOLD,  13);
    public static final Font FONT_INPUT  = new Font("Arial", Font.PLAIN, 13);
    public static final Font FONT_BTN    = new Font("Arial", Font.BOLD,  14);
    public static final Font FONT_SMALL  = new Font("Arial", Font.PLAIN, 11);
    public static final Font FONT_SCORE  = new Font("Arial", Font.BOLD,  16);

    public static JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? bg.darker() :
                            getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField makeField() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    public static JPasswordField makePassField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    private static void styleField(JTextComponent f) {
        f.setFont(FONT_INPUT);
        f.setForeground(TEXT_WHITE);
        f.setBackground(BG_FIELD);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_DIM, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    public static JPanel makeCard() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER_DIM);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    public static JLabel makeLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BG_DARK);
    }
}
