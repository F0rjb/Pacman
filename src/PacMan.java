import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import javax.swing.Timer;

public class PacMan extends JPanel implements ActionListener , KeyListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }
        if (pacman.direction== 'U'){
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction== 'D'){
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction== 'L'){
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction== 'R'){
            pacman.image = pacmanRightImage;
        }
    }

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U';
        int velocityX= 0;
        int velocityY= 0;


        Block (Image image , int x , int y, int width, int height){
            this.image = image;
            this.x =x;
            this.y = y;
            this.width= width;
            this.height= height;
            this.startX =x;
            this.startY= y;
        }
        void updateDirection (char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls ){
                if (collision(this, wall)) {
                    this.x-= this.velocityX;
                    this.y-= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }
        void updateVelocity (){ switch (direction){
            case 'U': {
                velocityX = 0;
                velocityY = -tileSize/4;

                break;
            }
            case 'D':
            {
                velocityX = 0;
                velocityY = tileSize/4;

                break;
            }
            case 'L':{
                velocityX = -tileSize/4;
                velocityY = 0;

                break;
            }
            case 'R':{
                velocityX = tileSize/4;
                velocityY = 0;

                break;
            }
        }

        }
    }
    private int rowCount =  21;
    private int columnCount = 19;
    private int tileSize = 32; //Pixels
    private int boardWidth = columnCount*tileSize;
    private int boardHeight = rowCount* tileSize;
    //Side variable
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    // Pacman
    private Image pacmanUpImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private Image pacmanDownImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    Timer gameLoop;
    public PacMan() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        // Listen to keystrokes using Keylisteners
        addKeyListener(this);
        setFocusable(
                true);
        //Load side Images
        wallImage= new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage= new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        redGhostImage= new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pinkGhostImage= new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        orangeGhostImage= new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        //Load Pacman Image
        pacmanUpImage= new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanRightImage= new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        pacmanLeftImage= new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanDownImage= new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();

        loadMap();
        // how long it'll take to refresh in milliseconds => fps = 1000/50 = 20
        gameLoop = new Timer(50, this);
        gameLoop.start();


    }
    public  void loadMap(){
        walls= new HashSet<Block>();
        foods= new HashSet<Block>();
        ghosts= new HashSet<Block>();
        for (int r = 0 ;r<rowCount; r++){
            for (int c =0 ;c<columnCount ;c++ ) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') {// wall block
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') {// blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') {// orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                } else if (tileMapChar == 'r') {// orange ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                } else if (tileMapChar == 'p') {// pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                } else if (tileMapChar == 'P') { // pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') {// food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
            }

        }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw (Graphics g){
        g.drawImage(pacman.image,pacman.x, pacman.y, pacman.width, pacman.height,null);
        for (Block ghost : ghosts){
            g.drawImage(ghost.image,ghost.x, ghost.y, ghost.width, ghost.height,null);
        }for (Block wall : walls){
            g.drawImage(wall.image,wall.x, wall.y, wall.width, wall.height,null);
        }
        for (Block food : foods) {
            g.drawRect( food.x, food.y, food.width, food.height);
        }
        g.setColor(Color.white);

    }
    public void move (){

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        //check wall collision
        for (Block wall : walls){
            if (collision(pacman,wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
    }
    public boolean collision(Block a , Block b){
       return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

}


