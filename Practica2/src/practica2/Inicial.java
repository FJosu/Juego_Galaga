package practica2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inicial extends JFrame {
    private MusicPlayer musicPlayer;
    private MusicPlayer2 musicPlayer2;
    public Inicial() {

        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Sound.wav");


        JButton newgame = new JButton("New Game");
        newgame.setBounds(100, 250, 200, 50);
        newgame.setContentAreaFilled(false);
        newgame.setFocusPainted(false);
        newgame.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        newgame.setForeground(Color.WHITE);
        newgame.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 20));
        newgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                Inicial.this.dispose();
                JFrame gameFrame = new JFrame("Space Invaders");
                Game gamePanel = new Game(gameFrame);
                gameFrame.add(gamePanel);
                gameFrame.pack();
                gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);
                gamePanel.requestFocusInWindow();
                musicPlayer.stopMusic();
            }
        
        });
        add(newgame);
        JButton load = new JButton("Load Game");
        load.setBounds(100, 325, 200, 50);
        load.setContentAreaFilled(false);
        newgame.setFocusPainted(false);
        load.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        load.setForeground(Color.WHITE);
        load.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 20));
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                System.out.println("Juego Iniciado");
                dispose();
            }
        
        });
        add(load);
        JButton score = new JButton("Scores");
        score.setBounds(100, 400, 200, 50);
        score.setContentAreaFilled(false);
        score.setFocusPainted(false);
        score.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        score.setForeground(Color.WHITE);
        score.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 20));
        score.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicPlayer2 = new MusicPlayer2();
                musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                musicPlayer.stopMusic();
                showLeaderboard();
            }
        
        });
        add(score);
        JButton exit = new JButton("Exit");
        exit.setBounds(100, 475, 200, 50);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        exit.setForeground(Color.WHITE);
        exit.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 20));
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                dispose();
                musicPlayer2 = new MusicPlayer2();
                musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
            }
        
        });
        add(exit);


        // Configurar el JFrame
        setTitle("Space Invaders");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Cargar y redimensionar el GIF
        ImageIcon gif = new ImageIcon(getClass().getResource("/img/portadagif.gif"));
        ImageIcon scaledGif = new ImageIcon(scaleGif(gif, getWidth(), getHeight()));

        // Crear un JLabel con el GIF redimensionado
        JLabel gifLabel = new JLabel(scaledGif);

        // Añadir el JLabel al JFrame
        add(gifLabel, BorderLayout.CENTER);

        // Hacer visible la ventana
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

    private void showLeaderboard(){
        ScoreManager scoreManager = new ScoreManager("scores.txt");
        LeaderboardWindow leaderboardWindow = new LeaderboardWindow(scoreManager);
        leaderboardWindow.setVisible(true);
    }

    
}
