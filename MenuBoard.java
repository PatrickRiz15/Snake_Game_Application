import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MenuBoard extends Board {

    private int selection = 1;

    public static int selectionAmount = 4;
    MenuBoard(){
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        //setBackground(new Color(232, 232, 232));
        setBackground(new Color(0, 0, 0));

        // initialize the game state
        //selection = new Player(0, 0);

        timer = new Timer(MENU_DELAY, this);
        System.out.println("hi");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawMenu(g);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_ENTER) {
            App.switchBoard();
        } else if ((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT)){
            if (selection < selectionAmount)
                selection++;
            System.out.println(selection);
        } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT){
            if (selection > 1)
                selection--;
            System.out.println(selection);
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void drawMenu(Graphics g) {
        // set the text to be displayed
        String text = "MENU";
        String text2 = "Body Amount";
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
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS / 2), TILE_SIZE * COLUMNS, TILE_SIZE);
        Rectangle rect2 = new Rectangle(TILE_SIZE * 3, TILE_SIZE * ((3 * ROWS / 4) - 1), TILE_SIZE * 6, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
         // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        // draw the string
        g2d.drawString(text, x, y);
        
        
        g2d.setColor(new Color(62, 172, 18));
        //g2d.drawRect(TILE_SIZE * 3, TILE_SIZE * ((3 * ROWS / 4) - 2), TILE_SIZE * 6, TILE_SIZE * 5);

        int x2 = rect2.x + (rect2.width - metrics.stringWidth(text2)) / 2;
        int x21 = x2 + metrics.stringWidth(text2) / 2 - TILE_SIZE - SCORE_FONT_SIZE/4;
        int x22 = rect2.x + (rect2.width - metrics.stringWidth(Integer.toString(BODY_STARTER))) / 2;
        int y2 = rect2.y + ((rect2.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(text2, x2, y2);
        if(selection == 1){
            g2d.drawRect(x21, y2 + TILE_SIZE/2, TILE_SIZE * 2 + SCORE_FONT_SIZE/2, TILE_SIZE * 2);
        }else if(selection == 2){
            g2d.drawRect(x21 + TILE_SIZE * ROWS/4, y2 + TILE_SIZE/2, TILE_SIZE * 2 + SCORE_FONT_SIZE/2, TILE_SIZE * 2);
        }

        g2d.drawString(Integer.toString(BODY_STARTER), x22, y2 + (TILE_SIZE * 2));
    }
}
