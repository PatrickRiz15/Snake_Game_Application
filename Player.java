import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {

    // image that represents the player's position on the board
    private BufferedImage image;
    // current position of the player on the board grid
    private Point pos;
    // keep track of the player's score
    private int score = 1;

    public Player() {
        // load the assets
        //loadImage();

        // initialize the state
        pos = new Point(0, 0);
    }
    public Player(int x, int y) {
        // load the assets
        //loadImage();

        // initialize the state
        pos = new Point(x, y);
    }

    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(getClass().getResource("images/chan.png"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but 
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        //g.drawImage(image, pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, observer);
        g.setColor(new Color(17, 180, 6));
        g.fillRect(pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
    }

    public void drawRec(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        //g.drawImage(image, pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, observer);
        g.setColor(new Color(17, 180, 6));
        g.drawRect(pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
    }

    public void drawGray(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        //g.drawImage(image, pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, observer);
        g.setColor(new Color(121, 129, 120));
        g.fillRect(pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
    }
    
    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();
        
        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        switch (key) {
        case KeyEvent.VK_UP :
        	pos.translate(0, -1);
            break;
        case KeyEvent.VK_W : 
        	pos.translate(0, -1);
        	break;
        case KeyEvent.VK_RIGHT :
        	pos.translate(1, 0);
            break;
        case KeyEvent.VK_D : 
        	pos.translate(1, 0);
        	break;
        case KeyEvent.VK_DOWN :
        	pos.translate(0, 1);
            break;
        case KeyEvent.VK_S : 
        	pos.translate(0, 1);
        	break;
        case KeyEvent.VK_LEFT :
        	pos.translate(-1, 0);
            break;
        case KeyEvent.VK_A : 
        	pos.translate(-1, 0);
        	break;
        }
    }

    public boolean tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        // prevent the player from moving off the edge of the board sideways
        if (pos.x < 0) {
            pos.x = 0;
            return true;
        } else if (pos.x >= Board.COLUMNS) {
            pos.x = Board.COLUMNS - 1;
            return true;
        }
        // prevent the player from moving off the edge of the board vertically
        if (pos.y < 0) {
            pos.y = 0;
            return true;
        } else if (pos.y >= Board.ROWS) {
            pos.y = Board.ROWS - 1;
            return true;
        }
        return false;
    }

    public void selectionTick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        // prevent the player from moving off the edge of the board sideways
        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x >= MenuBoard.selectionAmount) {
            pos.x = MenuBoard.selectionAmount - 1;
        }
        // prevent the player from moving off the edge of the board vertically
        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y > 0) {
            pos.y = 0;
        }
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void setScore(int amount) {
        score = amount;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }
}
