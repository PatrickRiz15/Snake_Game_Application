import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends Board {

    //shows the direction front node is traveling, 1-up, 2-right, 3-down, 4-left
    public static int DIRECTION = 0;
    // objects that appear on the game board
    private Player front;
    private ArrayList<Player> body = new ArrayList<Player>();
    private ArrayList<Apple> apples;
    private boolean didStart = false;
    private boolean restart = false;
    private boolean finished = false;
    private KeyEvent key;
    private KeyEvent secondKey;
    private boolean keyReceived = false;
    private boolean secondKeyExists = false;
    private boolean GRID = false;

    public GameBoard() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        //setBackground(new Color(232, 232, 232));
        setBackground(new Color(0, 0, 0));

        Random random = new Random();
        // initialize the game state
        front = new Player(random.nextInt(COLUMNS), random.nextInt(ROWS));
        //front = new Player();

        front.addScore(BODY_STARTER);

        apples = populateCoins();

        timer = new Timer(DELAY, this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        if(!body.isEmpty()){
            for(int i = body.size() - 1; i > 0; i--) {
                body.get(i).getPos().setLocation(body.get(i - 1).getPos().getLocation());
            }
            body.get(0).getPos().setLocation(front.getPos().getLocation());
        }

        keyReceived = false;
        if(DIRECTION == 1){
            key.setKeyCode(KeyEvent.VK_UP);
        }else if(DIRECTION == 2){
            key.setKeyCode(KeyEvent.VK_RIGHT);
        }else if(DIRECTION == 3){
            key.setKeyCode(KeyEvent.VK_DOWN);
        }else {
            key.setKeyCode(KeyEvent.VK_LEFT);
        }
        front.keyPressed(key);

        // prevent the player from disappearing off the board
        if(front.tick() || bodyTick()){
            collisionOccur();
        }

        // give the player points for collecting coins
        if(didFinish())
            gameFinished();
        else
            collectCoins();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();


        if(secondKeyExists){
            keyPressed(secondKey);
            secondKeyExists = false;
        }


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        if(GRID)
            drawBackground(g);

        for (Apple apple : apples) {
            apple.draw(g, this);
        }
        front.draw(g, this);
        for (Player p : body) {
            p.draw(g, this);
        }
        if(restart) {
            front.drawGray(g, this);
            drawGameOver(g, "GAME OVER", "Press any key to try again");
        }else if(finished){
            drawGameOver(g, "FINISHED GAME", "Press any key to play again");
        }

        drawScore(g);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(restart || finished){
            Random random = new Random();
            front = new Player(random.nextInt(COLUMNS), random.nextInt(ROWS));
            body.clear();
            apples = populateCoins();
            didStart = false;
            restart = false;
            finished = false;
            keyReceived = false;
            repaint();
        }else if(!didStart){
            int f11 = e.getKeyCode();
            if(f11 == KeyEvent.VK_F11){
                App.switchFullscreen();
                return;
            }
            key = e;
            keyReceived = false;
            didStart = true;
            // create starter parts of snake body if wanted
            for(int i = 0; i < BODY_STARTER; i++){
                body.add((new Player((int) front.getPos().getX(), (int) front.getPos().getY())));
            }
            front.setScore(BODY_STARTER + 1);
            // this timer will call the actionPerformed() method every DELAY ms
            DIRECTION = 0;
            timer = new Timer(DELAY, this);
            set_timer();
            timer.start();

        }

        if(keyReceived) {
            secondKey = e;
            secondKeyExists = true;
            return;
        }else {
            keyReceived = true;
        }
        // react to key down events
        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (DIRECTION != 3) {
                    DIRECTION = 1;
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (DIRECTION != 4) {
                    DIRECTION = 2;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (DIRECTION != 1) {
                    DIRECTION = 3;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (DIRECTION != 2) {
                    DIRECTION = 4;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                App.switchBoard();
                break;
            case KeyEvent.VK_1:
                GRID = !GRID;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }

    private void drawBackground(Graphics g) {
        // draw a checkered background
        //g.setColor(new Color(214, 214, 214));
        g.setColor(new Color(26, 25, 25));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // only color every other tile
                if ((row + col) % 2 == 1) {
                    // draw a square tile at the current row/column position
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

            }
        }
    }

    private void drawScore(Graphics g) {
        // set the text to be displayed
        //String text = "$" + player.getScore();
        String text = front.getScore();
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(30, 201, 139));
        g2d.setFont(new Font("Lato", Font.BOLD, SCORE_FONT_SIZE));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y);
    }

    private void drawGameOver(Graphics g, String text, String text2) {
        // set the text to be displayed
        //String text = "$" + player.getScore();
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(239, 0, 0));
        g2d.setFont(new Font("Lato", Font.BOLD, FONT_SIZE));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0, (TILE_SIZE * ((ROWS - 2) / 2)), TILE_SIZE * COLUMNS, TILE_SIZE * 2);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int x2 = rect.x + (rect.width - metrics.stringWidth(text2)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y - TILE_SIZE);
        g2d.drawString(text2, x2, y + TILE_SIZE);
    }

    private ArrayList<Apple> populateCoins() {
        ArrayList<Apple> appleList = new ArrayList<>();
        Random rand = new Random();

        // create the given number of coins in random positions on the board.
        // note that there is not check here to prevent two coins from occupying the same
        // spot, nor to prevent coins from spawning in the same spot as the player
        for (int i = 0; i < NUM_COINS; i++) {
            int coinX = rand.nextInt(COLUMNS);
            int coinY = rand.nextInt(ROWS);
            boolean occupied = true;
            while(occupied){
                occupied = false;
                for(Apple apple : appleList){
                    if((new Point(coinX, coinY)).equals(apple.getPos())){
                        occupied = true;
                    }
                }
                for(Player p: body){
                    if((new Point(coinX, coinY)).equals(p.getPos())){
                        occupied = true;
                    }
                }
                if((new Point(coinX, coinY)).equals(front.getPos())){
                    occupied = true;
                }
                if(occupied){
                    coinX = rand.nextInt(COLUMNS);
                    coinY = rand.nextInt(ROWS);
                }
            }

            if(rand.nextInt(100) <= ORANGE_OCCURANCE_PERCENTAGE)
                appleList.add(new Orange(coinX, coinY));
            else
                appleList.add(new Apple(coinX, coinY));
        }

        return appleList;
    }

    private void collectCoins() {
        // allow player to pickup apples
        ArrayList<Apple> collectedApples = new ArrayList<>();
        for (Apple apple : apples) {
            // if the player is on the same tile as an apple, collect it
            if (front.getPos().equals(apple.getPos())) {

                collectedApples.add(apple);
                if(!(apple instanceof Orange)){
                    if(body.size() <= ((ROWS * COLUMNS) - (BODY_AMOUNT_ADDED * 2 - 1))){
                        for(int i = 0; i < BODY_AMOUNT_ADDED; i++){
                            if(!body.isEmpty())
                                body.add((new Player((int) body.get(body.size() - 1).getPos().getX(), (int) body.get(body.size() - 1).getPos().getY())));
                            else
                                body.add((new Player((int) front.getPos().getX(), (int) front.getPos().getY())));
                            front.addScore(1);
                        }
                    }else {
                        // give the player some points for picking this up
                        front.addScore(1);
                        body.add((new Player((int) front.getPos().getX(), (int) front.getPos().getY())));
                    }
                    set_timer();
                }else {
                    if(!body.isEmpty()) {
                        body.remove(body.size() - 1);
                        front.addScore(-1);
                    }
                }
            }
        }
        // remove collected coins from the board
        apples.removeAll(collectedApples);

        if(apples.isEmpty()){
            apples = populateCoins();
        }
    }

    public boolean bodyTick(){
        if(body.size() > 1){
            for(Player p : body){
                if(front.getPos().equals(p.getPos()))
                    return true;
            }
        }
        return false;
    }

    public void collisionOccur(){
        timer.stop();
        restart = true;
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS * 2, TILE_SIZE * ROWS * 2));

    }

    public void gameFinished(){
        timer.stop();
        finished = true;
    }

    public boolean didFinish(){
        return (ROWS * COLUMNS) <= (1 + body.size());
    }
    public void set_timer(){
        int faster = DELAY;
        int multiplier = (Integer.parseInt(front.getScore()) / BODY_AMOUNT_ADDED) / SCORE_PERCENTAGE;
        int decreaser = DELAY_DECREASER * multiplier;
        int newSpeed = faster -= decreaser;
        if(faster > (SMALLEST_DELAY + SCORE_PERCENTAGE)) {
            faster = newSpeed;
        } else {
            faster = SMALLEST_DELAY + SCORE_PERCENTAGE;
        }
        timer.setDelay(faster);
    }
}
