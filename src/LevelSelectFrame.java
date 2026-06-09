import javax.swing.*;
import java.awt.*;

public class LevelSelectFrame extends JFrame {

    public LevelSelectFrame() {

        setTitle("Select Difficulty");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton easy = new JButton("EASY");
        JButton medium = new JButton("MEDIUM");
        JButton hard = new JButton("HARD");

        add(easy);
        add(medium);
        add(hard);

        easy.addActionListener(e -> startGame(Difficulty.EASY));
        medium.addActionListener(e -> startGame(Difficulty.MEDIUM));
        hard.addActionListener(e -> startGame(Difficulty.HARD));

        setVisible(true);
    }

    private void startGame(Difficulty diff) {

        dispose();

        JFrame frame = new JFrame("PacMan");
        
        PacMan game = new PacMan();

        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}