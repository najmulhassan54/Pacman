import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

public class PacMan extends JPanel {

    class Block {
        int x, y, width, height;
        Image image;

        Block(Image image, int x, int y, int width, int height) {
            this.image  = image;
            this.x      = x;
            this.y      = y;
            this.width  = width;
            this.height = height;
        }
    }

    final int rowCount = 16, columnCount = 19, tileSize = 32;
    final int boardWidth  = columnCount * tileSize;
    final int boardHeight = rowCount    * tileSize;

    Image wallImage;
    Image blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage;
    Image pacmanRightImage;

    HashSet<Block> walls, foods, ghosts;
    Block pacman;

    String[] tileMap;


    PacMan(Difficulty difficulty) {
        tileMap = GameMap.getMap(difficulty);

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        loadImages();
        loadMap();
    }

  
    private Image load(String name) {
        return new ImageIcon(getClass().getResource("./" + name)).getImage();
    }

    private void loadImages() {
        wallImage        = load("wall.png");
        blueGhostImage   = load("blueGhost.png");
        orangeGhostImage = load("orangeGhost.png");
        pinkGhostImage   = load("pinkGhost.png");
        redGhostImage    = load("redGhost.png");
        pacmanRightImage = load("pacmanRight.png");
    }

    private void loadMap() {
        walls  = new HashSet<>();
        foods  = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                int  x  = c * tileSize;
                int  y  = r * tileSize;
                char ch = tileMap[r].charAt(c);

                switch (ch) {
                    case 'X' -> walls.add(new Block(wallImage,        x, y, tileSize, tileSize));
                    case 'b' -> ghosts.add(new Block(blueGhostImage,   x, y, tileSize, tileSize));
                    case 'o' -> ghosts.add(new Block(orangeGhostImage, x, y, tileSize, tileSize));
                    case 'p' -> ghosts.add(new Block(pinkGhostImage,   x, y, tileSize, tileSize));
                    case 'r' -> ghosts.add(new Block(redGhostImage,    x, y, tileSize, tileSize));
                    case 'P' -> pacman = new Block(pacmanRightImage,   x, y, tileSize, tileSize);
                    case ' ' -> foods.add(new Block(null, x + 14, y + 14, 4, 4));
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Block wall : walls)
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);

        g.setColor(Color.WHITE);
        for (Block food : foods)
            g.fillRect(food.x, food.y, food.width, food.height);

        for (Block ghost : ghosts)
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);

        if (pacman != null)
            g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
    }
}