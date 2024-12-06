import javax.swing.JFrame;
public class Main {

    public static void main(String[] args) {
        int rowCount =  21;
        int columnCount = 19;
        int tileSize = 32; //Pixels
        int boardWidth = columnCount*tileSize;
        int boardHeight = rowCount* tileSize;
        // Setting Game window
        JFrame frame = new JFrame("Pac-man");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add frame to the window
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        pacmanGame.requestFocus();
        frame.pack();



    }
}