package practica2;

import java.awt.*;

public class Enemy {
    private int x, y;
    private int dx = -1; // Movimiento hacia la izquierda por defecto
    private int dy = 1;  // Movimiento hacia abajo por defecto
    private boolean movingDown = true;
    private int lateralShift = 20; // Desplazamiento lateral cuando se alcanzan los límites

    public Enemy(int initialX, int initialY) {
        x = initialX;
        y = initialY;
    }

    public void move() {
        if (movingDown) {
            y += dy; // Mueve hacia abajo
        } else {
            y -= dy; // Mueve hacia arriba
        }
    }

    public void shiftSideways() {
        x += dx - lateralShift; // Desplazamiento lateral limitado
    }

    public void reverseDirection() {
        dx = -dx; // Invierte la dirección horizontal
    }

    public void changeVerticalDirection() {
        dy = -dy; // Invierte la dirección vertical
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 50, 50); // Dibuja un rectángulo rojo (representa al enemigo)
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50); // Devuelve un rectángulo que representa los límites del enemigo
    }
}

