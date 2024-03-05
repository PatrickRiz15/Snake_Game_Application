import javax.imageio.ImageIO;
import javax.swing.*;
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

    private static void initWindow() {

        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // add the jpanel to the window
        window.add(board);
        // pass keyboard inputs to the jpanel
        window.addKeyListener(board);
        
        // don't allow the user to resize the window
        window.setResizable(false);
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
            isMenu = false;
        } else{
            board = new MenuBoard();
            isMenu = true;
        }
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);
    }

}