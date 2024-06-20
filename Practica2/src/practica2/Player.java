package practica2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player {
    private int x, y;
    private int dy;
    private Image playerImage; // Imagen del jugador
    private int width = 50; // Ancho del jugador
    private int height = 50; // Alto del jugador
    private int points;

    public Player(int initialX, int initialY) {
        x = initialX;
        y = initialY;
        points = 0; // Inicializar puntos a 0
        
        // Cargar la imagen del jugador y escalarla al tamaño deseado
        ImageIcon playerIcon = new ImageIcon(getClass().getResource("/img/player.png")); // Ajusta la ruta según donde tengas la imagen
        playerImage = playerIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void move() {
        y += dy;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            dy = -2;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP) {
            dy = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void draw(Graphics2D g) {
        // Dibujar la imagen del jugador
        g.drawImage(playerImage, x, y, width, height, null);
    }
    public void setPoints(int points) {
        this.points = points;
    }
}

