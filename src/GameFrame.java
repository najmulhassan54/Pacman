import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Select Difficulty");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton easy   = new JButton("EASY");
        JButton medium = new JButton("MEDIUM");
        JButton hard   = new JButton("HARD");

        add(easy);
        add(medium);
        add(hard);

        easy.addActionListener(e   -> startGame(Difficulty.EASY));
        medium.addActionListener(e -> startGame(Difficulty.MEDIUM));
        hard.addActionListener(e   -> startGame(Difficulty.HARD));

        setVisible(true);
    }

    private void startGame(Difficulty diff) {
        dispose();

        JFrame gameFrame = new JFrame("PacMan");
        PacMan game = new PacMan(diff);          // ← fixed: only diff, no extra arg

        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
        game.requestFocusInWindow();
    }
}