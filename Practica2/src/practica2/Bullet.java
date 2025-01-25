package practica2;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private int dx = 5;
    private Image bulletImage; 
    private int width = 30; 
    private int height = 30; 

    public Bullet(int initialX, int initialY) {
        x = initialX;
        y = initialY;


        ImageIcon bulletIcon = new ImageIcon(getClass().getResource("/img/Bullet.png")); 
        bulletImage = bulletIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void move() {
        x += dx; 
    }

    public void draw(Graphics2D g) {
        g.drawImage(bulletImage, x, y, null); 
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, bulletImage.getWidth(null), bulletImage.getHeight(null));
    }
}

