import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block {
        int x, y, width, height;
        int startX, startY;
        int velocityX = 0, velocityY = 0;
        char direction = 'R';
        Image image;

        Block(Image image, int x, int y, int width, int height) {
            this.image  = image;
            this.x      = x; this.startX = x;
            this.y      = y; this.startY = y;
            this.width  = width;
            this.height = height;
        }

        void tryDirection(char dir) {
            char prev = this.direction;
            this.direction = dir;
            updateVelocity();
            x += velocityX; y += velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    x -= velocityX; y -= velocityY;
                    this.direction = prev;
                    updateVelocity();
                    return;
                }
            }
        }

        void updateVelocity() {
            int spd = tileSize / 4;
            switch (direction) {
                case 'U' -> { velocityX = 0;    velocityY = -spd; }
                case 'D' -> { velocityX = 0;    velocityY =  spd; }
                case 'L' -> { velocityX = -spd; velocityY = 0;    }
                case 'R' -> { velocityX =  spd; velocityY = 0;    }
            }
        }

        void reset() { x = startX; y = startY; velocityX = 0; velocityY = 0; }
    }

    final int rowCount = 16, columnCount = 19, tileSize = 32;
    final int boardWidth  = columnCount * tileSize;
    final int boardHeight = rowCount    * tileSize;


    Image wallImage;
    Image blueGhostImg, orangeGhostImg, pinkGhostImg, redGhostImg, scaredGhostImg;
    Image[] pacImgs = new Image[4]; // R, L, U, D


    HashSet<Block> walls, foods, ghosts;
    Block pacman;


    Difficulty  difficulty;
    String[]    tileMap;
    String[]    userData;
    JFrame      parentFrame;
    javax.swing.Timer       gameLoop;
    Random      random = new Random();

    int    score       = 0;
    int    lives       = 3;
    int    totalFood   = 0;
    boolean gameOver   = false;
    boolean won        = false;
    boolean paused     = false;


    double ghostSpeed  = 1.0;
    int    ghostTimer  = 0;  

    int    mouthAngle  = 45;
    int    mouthDelta  = -5;
    int    animTick    = 0;

    char   nextDir     = 'R';

    boolean chaseMode  = false;
    int     modeTimer  = 0;

    static final char[] DIRS = {'U','D','L','R'};


    public PacMan(Difficulty difficulty, String[] userData, JFrame parentFrame) {
        this.difficulty   = difficulty;
        this.userData     = userData;
        this.parentFrame  = parentFrame;
        this.tileMap      = GameMap.getMap(difficulty);

        setPreferredSize(new Dimension(boardWidth, boardHeight + 40)); // +40 for HUD
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        loadImages();
        applyDifficulty();
        loadMap();

        gameLoop = new javax.swing.Timer(40, this); // ~25 fps
        gameLoop.start();
    }

    private void applyDifficulty() {
        switch (difficulty) {
            case EASY   -> { lives = 5; ghostSpeed = 1.0; }
            case MEDIUM -> { lives = 4; ghostSpeed = 1.4; }
            case HARD   -> { lives = 3; ghostSpeed = 1.8; }
        }
    }

    private Image load(String name) {
        try {
            return new ImageIcon(getClass().getResource("/" + name)).getImage();
        } catch (Exception e) {
            // Return a placeholder coloured image if asset missing
            BufferedImage img = new java.awt.image.BufferedImage(tileSize, tileSize,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.MAGENTA);
            g.fillRect(0, 0, tileSize, tileSize);
            g.dispose();
            return img;
        }
    }

    private void loadImages() {
        wallImage      = load("wall.png");
        blueGhostImg   = load("blueGhost.png");
        orangeGhostImg = load("orangeGhost.png");
        pinkGhostImg   = load("pinkGhost.png");
        redGhostImg    = load("redGhost.png");
        pacImgs[0]     = load("pacmanRight.png");
        pacImgs[1]     = load("pacmanLeft.png");
        pacImgs[2]     = load("pacmanUp.png");
        pacImgs[3]     = load("pacmanDown.png");
    }

    public void loadMap() {
        walls  = new HashSet<>();
        foods  = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                int  x  = c * tileSize;
                int  y  = r * tileSize + 40; // offset by HUD height
                char ch = tileMap[r].charAt(c);
                switch (ch) {
                    case 'X' -> walls.add(new Block(wallImage,        x, y, tileSize, tileSize));
                    case 'b' -> {Block g=new Block(blueGhostImg,   x, y, tileSize, tileSize); g.direction=DIRS[random.nextInt(4)]; g.updateVelocity(); ghosts.add(g);}
                    case 'o' -> {Block g=new Block(orangeGhostImg, x, y, tileSize, tileSize); g.direction=DIRS[random.nextInt(4)]; g.updateVelocity(); ghosts.add(g);}
                    case 'p' -> {Block g=new Block(pinkGhostImg,   x, y, tileSize, tileSize); g.direction=DIRS[random.nextInt(4)]; g.updateVelocity(); ghosts.add(g);}
                    case 'r' -> {Block g=new Block(redGhostImg,    x, y, tileSize, tileSize); g.direction=DIRS[random.nextInt(4)]; g.updateVelocity(); ghosts.add(g);}
                    case 'P' -> pacman = new Block(pacImgs[0], x, y, tileSize, tileSize);
                    case ' ' -> foods.add(new Block(null, x+14, y+14, 4, 4));
                }
            }
        }
        totalFood = foods.size();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawHUD(g2);
        drawWalls(g2);
        drawFood(g2);
        drawGhosts(g2);
        drawPacman(g2);

        if (paused && !gameOver && !won) drawPauseOverlay(g2);
        if (gameOver) drawGameOverOverlay(g2);
        if (won)      drawWinOverlay(g2);
    }

    private void drawHUD(Graphics2D g) {

        g.setColor(new Color(10, 10, 25));
        g.fillRect(0, 0, boardWidth, 40);
        g.setColor(new Color(50, 50, 90));
        g.drawLine(0, 39, boardWidth, 39);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(new Color(255, 200, 0));
        g.drawString("SCORE", 12, 16);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(String.valueOf(score), 12, 34);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(new Color(255, 200, 0));
        g.drawString("LIVES", boardWidth/2 - 20, 16);
        for (int i = 0; i < lives; i++) {
            g.setColor(new Color(255, 200, 0));
            g.fillArc(boardWidth/2 - 20 + i*22, 20, 16, 16, 30, 300);
        }

        g.setFont(new Font("Arial", Font.BOLD, 12));
        Color dc = difficulty == Difficulty.EASY ? new Color(80,200,120)
                 : difficulty == Difficulty.MEDIUM ? new Color(255,180,50)
                 : new Color(255,80,80);
        g.setColor(dc);
        String dlabel = difficulty.name();
        g.drawString(dlabel, boardWidth - 60, 24);

        if (totalFood > 0) {
            int barW = 100;
            int filled = (int)(barW * (1.0 - (double)foods.size() / totalFood));
            g.setColor(new Color(40, 40, 70));
            g.fillRect(boardWidth - 120, 30, barW, 6);
            g.setColor(new Color(255, 200, 0));
            g.fillRect(boardWidth - 120, 30, filled, 6);
        }
    }

    private void drawWalls(Graphics2D g) {
        for (Block wall : walls)
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
    }

    private void drawFood(Graphics2D g) {
        for (Block food : foods) {
            g.setColor(new Color(255, 255, 200, 60));
            g.fillOval(food.x - 3, food.y - 3, food.width + 6, food.height + 6);
            g.setColor(Color.WHITE);
            g.fillOval(food.x, food.y, food.width, food.height);
        }
    }

    private void drawGhosts(Graphics2D g) {
        for (Block ghost : ghosts)
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
    }

    private void drawPacman(Graphics2D g) {
        if (pacman == null) return;
        int x = pacman.x, y = pacman.y, s = tileSize;

        if (pacman.image != null) {
            g.drawImage(pacman.image, x, y, s, s, null);
        } else {

            int startAngle = switch (pacman.direction) {
                case 'R' -> mouthAngle / 2;
                case 'L' -> 180 + mouthAngle / 2;
                case 'U' -> 90 + mouthAngle / 2;
                case 'D' -> 270 + mouthAngle / 2;
                default  -> mouthAngle / 2;
            };
            g.setColor(new Color(255, 200, 0));
            g.fillArc(x+2, y+2, s-4, s-4, startAngle, 360 - mouthAngle);
            g.setColor(Color.BLACK);
            g.fillOval(x + s/2, y + s/4, 5, 5); // eye
        }
    }

    private void drawPauseOverlay(Graphics2D g) {
        drawOverlay(g, new Color(0,0,0,160));
        g.setColor(new Color(255,200,0));
        g.setFont(new Font("Arial", Font.BOLD, 36));
        drawCentered(g, "PAUSED", boardHeight/2 - 20 + 40);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        drawCentered(g, "Press P to resume", boardHeight/2 + 20 + 40);
    }

    private void drawGameOverOverlay(Graphics2D g) {
        drawOverlay(g, new Color(0,0,0,180));
        g.setColor(new Color(255,80,80));
        g.setFont(new Font("Arial", Font.BOLD, 42));
        drawCentered(g, "GAME OVER", boardHeight/2 - 50 + 40);
        g.setColor(new Color(255,200,0));
        g.setFont(new Font("Arial", Font.BOLD, 22));
        drawCentered(g, "Score: " + score, boardHeight/2 + 0 + 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        drawCentered(g, "Press R to restart  |  Q to quit", boardHeight/2 + 40 + 40);
    }

    private void drawWinOverlay(Graphics2D g) {
        drawOverlay(g, new Color(0,30,0,180));
        g.setColor(new Color(80,220,120));
        g.setFont(new Font("Arial", Font.BOLD, 42));
        drawCentered(g, "YOU WIN!", boardHeight/2 - 50 + 40);
        g.setColor(new Color(255,200,0));
        g.setFont(new Font("Arial", Font.BOLD, 22));
        drawCentered(g, "Score: " + score, boardHeight/2 + 0 + 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        drawCentered(g, "Press R to play again  |  Q to quit", boardHeight/2 + 40 + 40);
    }

    private void drawOverlay(Graphics2D g, Color c) {
        g.setColor(c);
        g.fillRect(0, 40, boardWidth, boardHeight);
    }

    private void drawCentered(Graphics2D g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (boardWidth - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (paused || gameOver || won) { repaint(); return; }
        move();
        animateMouth();
        modeTimer++;
        repaint();
    }

    private void move() {
        pacman.tryDirection(nextDir);

        int nx = pacman.x + pacman.velocityX;
        int ny = pacman.y + pacman.velocityY;
        boolean blocked = false;

        pacman.x = nx; pacman.y = ny;
        for (Block wall : walls) {
            if (collision(pacman, wall)) { blocked = true; break; }
        }
        if (blocked) { pacman.x = nx - pacman.velocityX; pacman.y = ny - pacman.velocityY; }

        if (pacman.x + pacman.width < 0) pacman.x = boardWidth;
        if (pacman.x > boardWidth)        pacman.x = -pacman.width;


        Block eaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) { eaten = food; score += 10; }
        }
        foods.remove(eaten);

        if (foods.isEmpty()) {
            won = true;
            saveScore();
            return;
        }

        for (Block ghost : ghosts) {

            double steps = ghostSpeed;
            int wholeSteps = (int) steps;

            for (int s = 0; s < wholeSteps; s++) moveGhostOneStep(ghost);

            if (collision(pacman, ghost)) {
                lives--;
                if (lives <= 0) { gameOver = true; saveScore(); return; }
                resetPositions();
                return;
            }
        }

        if (modeTimer > 250) { chaseMode = !chaseMode; modeTimer = 0; }
    }

    private void moveGhostOneStep(Block ghost) {
        ghost.x += ghost.velocityX;
        ghost.y += ghost.velocityY;

        if (ghost.x + ghost.width < 0) ghost.x = boardWidth;
        if (ghost.x > boardWidth)       ghost.x = -ghost.width;

        boolean hitWall = false;
        for (Block wall : walls) {
            if (collision(ghost, wall)) {
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                hitWall = true;
                break;
            }
        }

        if (hitWall) {
            char newDir = chaseMode ? smartDir(ghost) : DIRS[random.nextInt(4)];
            ghost.direction = newDir;
            ghost.updateVelocity();
        }
    }

    private char smartDir(Block ghost) {
        int dx = pacman.x - ghost.x;
        int dy = pacman.y - ghost.y;
      
        char[] preferred;
        if (Math.abs(dx) > Math.abs(dy)) {
            preferred = dx > 0 ? new char[]{'R','L','U','D'} : new char[]{'L','R','U','D'};
        } else {
            preferred = dy > 0 ? new char[]{'D','U','R','L'} : new char[]{'U','D','R','L'};
        }
   
        for (char dir : preferred) {
            if (canMove(ghost, dir)) return dir;
        }
        return DIRS[random.nextInt(4)];
    }

    private boolean canMove(Block ghost, char dir) {
        int nx = ghost.x, ny = ghost.y;
        int spd = tileSize / 4;
        switch (dir) {
            case 'U' -> ny -= spd;
            case 'D' -> ny += spd;
            case 'L' -> nx -= spd;
            case 'R' -> nx += spd;
        }
        Block test = new Block(null, nx, ny, ghost.width, ghost.height);
        for (Block wall : walls) if (collision(test, wall)) return false;
        return true;
    }


    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width  &&
               a.x + a.width  > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    private void resetPositions() {
        pacman.reset();
        nextDir = 'R';
        for (Block ghost : ghosts) {
            ghost.reset();
            ghost.direction = DIRS[random.nextInt(4)];
            ghost.updateVelocity();
        }
    }

    private void animateMouth() {
        animTick++;
        if (animTick % 2 == 0) {
            mouthAngle += mouthDelta;
            if (mouthAngle <= 5 || mouthAngle >= 60) mouthDelta = -mouthDelta;
        }
    }

    private void saveScore() {
        if (userData != null && userData.length > 1) {
            UserStorage.updateHighScore(userData[1], score);
        }
    }

    private void restartGame() {
        score    = 0;
        lives    = switch (difficulty) { case EASY -> 5; case MEDIUM -> 4; default -> 3; };
        gameOver = false;
        won      = false;
        modeTimer= 0;
        loadMap();
        resetPositions();
        gameLoop.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP,    KeyEvent.VK_W -> { nextDir = 'U'; pacman.image = pacImgs[2]; }
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S -> { nextDir = 'D'; pacman.image = pacImgs[3]; }
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> { nextDir = 'L'; pacman.image = pacImgs[1]; }
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> { nextDir = 'R'; pacman.image = pacImgs[0]; }
            case KeyEvent.VK_P -> paused = !paused;
            case KeyEvent.VK_R -> { if (gameOver || won) restartGame(); }
            case KeyEvent.VK_Q -> {
                gameLoop.stop();
                parentFrame.dispose();
                new GameFrame(userData);
            }
        }
    }

    @Override public void keyPressed(KeyEvent e)  {}
    @Override public void keyTyped(KeyEvent e)     {}
}
