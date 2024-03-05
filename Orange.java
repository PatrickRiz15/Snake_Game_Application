import java.awt.*;
import java.awt.image.ImageObserver;

public class Orange extends Apple {
    public Orange(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate board grid position into a canvas pixel
        // position by multiplying by the tile size.
        //g.drawImage(image, pos.x * Board.TILE_SIZE, pos.y * Board.TILE_SIZE, observer);
        g.setColor(new Color(255, 115, 0));
        g.fillRect(super.getPos().x * Board.TILE_SIZE, super.getPos().y * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
    }
}
