package practica2;
import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    // Constructor que carga el GIF
    public BackgroundPanel(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            backgroundImage = icon.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT); // Ajusta el tamaño según sea necesario
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Paso 3: Sobrescribir paintComponent para dibujar el GIF
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this); // Dibuja el GIF ajustado al tamaño del panel
    }
}