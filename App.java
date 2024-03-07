import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/* Things to make:
Inverted Snake
Blind People Edition:
    Direction of snake currently
    And a beeper varying in intensity for how close you are to the apple
    Only thing actually on screen would be your points
 */

class App {

    // create a window frame and set the title in the toolbar
    static JFrame window = new JFrame("Snake     SNAKE     SNAKE!!     SNAAAAAAAAAAAAAKE!!!!");
    // create the jpanel to draw on.
    // this also initializes the game loop

    static Board board = new MenuBoard();
    static boolean isMenu = true;
    static boolean isFullScreen = false;
    static int smallWindowTileSize;
    static int fullWindowTileSize, fullWindowWidth;
    static GraphicsDevice gd;
    static GridBagConstraints constraints = new GridBagConstraints();

    private static void initWindow() {

        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setting window size based on screen resolution
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        smallWindowTileSize = (int)(((float)board.TILE_SIZE/(1920*1080)) * width * height);
        board.TILE_SIZE = smallWindowTileSize;
        fullWindowTileSize = (int)(height/(float)board.ROWS);
        fullWindowWidth = width;

        // add the jpanel to the window
        window.setLayout(new GridLayout());
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        int weight = (fullWindowWidth - (fullWindowTileSize * board.COLUMNS)) / 2;
        //constraints.ipadx = weight;
        window.add(board, constraints);
        // pass keyboard inputs to the jpanel
        window.addKeyListener(board);
        
        // don't allow the user to resize the window
        window.setResizable(true);
        // fit the window size around the components (just our jpanel).
        // pack() should be called after setResizable() to avoid issues on some platforms
        window.pack();
        // open window in the center of the screen
        window.setLocationRelativeTo(null);
        // set the icon image
        try{
            window.setIconImage(ImageIO.read(App.class.getResource("images/hasbula.png")));
        }catch (IOException e){
        }

        // display the window
        window.setVisible(true);
    }

    public static void main(String[] args) {
        // invokeLater() is used here to prevent our graphics processing from
        // blocking the GUI. https://stackoverflow.com/a/22534931/4655368
        // this is a lot of boilerplate code that you shouldn't be too concerned about.
        // just know that when main runs it will call initWindow() once.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow();
            }
        });
    }

    public static void switchBoard() {
        board.timer.stop();
        board.timer = null;
        window.remove(board);
        window.removeKeyListener(board);
        board = null;
        if(isMenu){
            board = new GameBoard();
        } else{
            board = new MenuBoard();
        }
        isMenu = !isMenu;
        window.add(board, constraints);
        window.addKeyListener(board);
        window.pack();
        window.setVisible(true);
    }

    public static void switchFullscreen() {
        if(!isFullScreen){
            board.TILE_SIZE = fullWindowTileSize;
            gd.setFullScreenWindow(window);
            window.setLocation((fullWindowWidth - (fullWindowTileSize * board.COLUMNS)) / 2, 0);
        } else {
            board.TILE_SIZE = smallWindowTileSize;
            gd.setFullScreenWindow(null);
            //window.setPreferredSize(new Dimension(board.TILE_SIZE * board.COLUMNS, board.TILE_SIZE * board.ROWS));
            //window.pack();
        }
        board.SCORE_FONT_SIZE = (int)(1.25 * board.TILE_SIZE);
        board.FONT_SIZE = (int)(1.5 * board.TILE_SIZE);

        //window.setPreferredSize(new Dimension(board.TILE_SIZE * board.COLUMNS, board.TILE_SIZE * board.ROWS));
        isFullScreen = !isFullScreen;

        //window.pack();
        //window.setLocationRelativeTo(null);


    }

}