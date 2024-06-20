package practica2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FileWindow extends JFrame {
    private MusicPlayer2 musicPlayer2;
    public FileWindow() {

        JFrame frame = new JFrame("Mi Juego");
              Game game = new Game(frame);

                 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.add(game);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(false);
        //musicPlayer = new MusicPlayer();
        //musicPlayer.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Sound.wav");


        JButton Search = new JButton("Load Game");
        Search.setBounds(50, 100, 200, 50);
        Search.setContentAreaFilled(false);
        Search.setFocusPainted(false);
        Search.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        Search.setForeground(Color.WHITE);
        Search.setFont(new Font("c", Font.BOLD, 20));
        Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                    

                game.loadGameData();
                
            }
        
        });
        add(Search);
        JButton load = new JButton("Start Game");
        load.setBounds(50, 200, 200, 50);
        load.setContentAreaFilled(false);
        load.setFocusPainted(false);
        load.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        load.setForeground(Color.WHITE);
        load.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 20));
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileWindow.this.dispose();
                musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                    
                frame.setVisible(true);
                game.Startgame();
            }
        });
        add(load);

        JButton back = new JButton("Back");
        back.setBounds(50, 300, 200, 50);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 20));
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                    FileWindow.this.dispose();
                    Inicial inicial = new Inicial();
            }
        });
        add(back);
        


        // Configurar el JFrame
        setTitle("Space Invaders");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Cargar y redimensionar el GIF
        ImageIcon gif = new ImageIcon(getClass().getResource("/img/portadagif.gif"));
        ImageIcon scaledGif = new ImageIcon(scaleGif(gif, getWidth(), getHeight()));
        JLabel gifLabel = new JLabel(scaledGif);
        add(gifLabel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Método para redimensionar el GIF
    private Image scaleGif(ImageIcon gif, int width, int height) {
        List<Image> frames = new ArrayList<>();
        int frameCount = gif.getIconHeight() / gif.getIconWidth();
        for (int i = 0; i < frameCount; i++) {
            Image frame = gif.getImage();
            frames.add(frame.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        }
        return new ImageIcon(frames.get(0)).getImage();
    }

    
}