package practica2;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

import javax.swing.*;
import java.awt.*;

public class Items {
    private String type;
    private JLabel itemLabel;
    private int width = 30; // Ancho del ítem
    private int height = 30; // Alto del ítem
    private int dx = -2; // Velocidad horizontal del ítem

    public Items(String type, int x, int y) {
        this.type = type;
        
        // Cargar la imagen del ítem según su tipo
        ImageIcon itemIcon = null;
        if (type.equals("points")) {
            itemIcon = new ImageIcon(getClass().getResource("/img/ficha.png")); // Ajusta la ruta según donde tengas la imagen
        } else if(type.equals("penalization")){
            itemIcon = new ImageIcon(getClass().getResource("/img/bonus2.png")); // Ajusta la ruta según donde tengas la imagen
        }else if(type.equals("-points")){
            itemIcon = new ImageIcon(getClass().getResource("/img/lesspoints.png")); // Ajusta la ruta según donde tengas la imagen
        }else if(type.equals("-time")){
            itemIcon = new ImageIcon(getClass().getResource("/img/lesstime.png")); // Ajusta la ruta según donde tengas la imagen
        }

        Image itemImage = itemIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        itemIcon = new ImageIcon(itemImage);
        
        itemLabel = new JLabel(itemIcon);
        itemLabel.setBounds(x, y, width, height); // Establecer la posición del ítem
    }

    public String getType() {
        return type;
    }

    public JLabel getItemLabel() {
        return itemLabel;
    }

    public Rectangle getBounds() {
        return itemLabel.getBounds();
    }

    public void move() {
        itemLabel.setLocation(itemLabel.getX() + dx, itemLabel.getY());
    }
}




