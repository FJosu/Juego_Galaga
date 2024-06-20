package practica2;
import javax.swing.*;
import java.awt.*;


public class Items {
    private String type;
    private JLabel itemLabel;
    private int width = 30; 
    private int height = 30; 
    private int dx = -2; 

    public Items(String type, int x, int y) {
        this.type = type;
        
        // Cargar la imagen del ítem según su tipo
        ImageIcon itemIcon = null;
        if (type.equals("points")) {
            itemIcon = new ImageIcon(getClass().getResource("/img/ficha.png")); 
        } else if(type.equals("penalization")){
            itemIcon = new ImageIcon(getClass().getResource("/img/bonus2.png")); 
        }else if(type.equals("-points")){
            itemIcon = new ImageIcon(getClass().getResource("/img/lesspoints.png")); 
        }else if(type.equals("-time")){
            itemIcon = new ImageIcon(getClass().getResource("/img/lesstime.png"));
        }

        Image itemImage = itemIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        itemIcon = new ImageIcon(itemImage);
        
        itemLabel = new JLabel(itemIcon);
        itemLabel.setBounds(x, y, width, height);
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




