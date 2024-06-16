package practica2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Bullet {
    private int x, y;
    private int dx = 5;
    private Image bulletImage; // Imagen de la bala
    private int width = 30; // Ancho de la bala
    private int height = 30; // Alto de la bala

    public Bullet(int initialX, int initialY) {
        x = initialX;
        y = initialY;


        ImageIcon bulletIcon = new ImageIcon(getClass().getResource("/img/Bullet.png")); // Ruta de la imagen de la bala
        bulletImage = bulletIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void move() {
        x += dx; // Mover la bala hacia la derecha (puedes ajustar la dirección según tu juego)
    }

    public void draw(Graphics2D g) {
        g.drawImage(bulletImage, x, y, null); // Dibujar la imagen de la bala en las coordenadas (x, y)
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, bulletImage.getWidth(null), bulletImage.getHeight(null)); // Obtener los límites de la imagen de la bala
    }
}

