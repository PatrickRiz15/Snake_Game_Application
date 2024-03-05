import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public abstract class Board extends JPanel implements ActionListener, KeyListener {

    // controls the delay between each tick in ms
    public static final int DELAY = 135;
    //the fastest that the game speed will be
    public static final int SMALLEST_DELAY = 70;
    //how much the delay will be decreased at a time
    public static final int DELAY_DECREASER = 5;
    public static final int SCORE_PERCENTAGE = 5;
    // controls the size of the board
    public static final int TILE_SIZE = 20;
    public static final int ROWS = 27;
    public static final int COLUMNS = 37;
    // controls how many coins appear on the board
    public static final int NUM_COINS = 1;
    public static final int BODY_STARTER = 0;
    public static final int BODY_AMOUNT_ADDED = 4 ;
    public static final int SCORE_FONT_SIZE = 25;
    public static final int FONT_SIZE = 30;
    public static final int ORANGE_OCCURANCE_PERCENTAGE = 40;
    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    public Timer timer;

}
