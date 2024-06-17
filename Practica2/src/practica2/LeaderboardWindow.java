package practica2;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardWindow extends JFrame {
    private ScoreManager scoreManager;
    private MusicPlayer musicPlayer;

    public LeaderboardWindow(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;

        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\song16.wav");
       
        ImageIcon Top5 = new ImageIcon(getClass().getResource("/img/TOP5.png"));
        Image top5dime = Top5.getImage().getScaledInstance(600, 300,Image.SCALE_SMOOTH);
        ImageIcon finaltop5 = new ImageIcon(top5dime);
        JLabel toplabel = new JLabel(finaltop5);
        toplabel.setBounds(-140, -100, Top5.getIconWidth(), Top5.getIconHeight());
        add(toplabel);

       
        // Obtener los puntajes principales
        List<Score> topScores = scoreManager.getTopScores(5);

        // Agregar los nombres y puntuaciones en el podio
        JLabel firstPlaceLabel = new JLabel(formatScore(topScores.get(0)));
        firstPlaceLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 18));
        firstPlaceLabel.setForeground(Color.WHITE);
        firstPlaceLabel.setBounds(165, 360, 200, 50); // Ajusta las coordenadas según sea necesario
        add(firstPlaceLabel);

        JLabel secondPlaceLabel = new JLabel(formatScore(topScores.get(1)));
        secondPlaceLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 18));
        secondPlaceLabel.setForeground(Color.WHITE);
        secondPlaceLabel.setBounds(10, 390, 200, 50); // Ajusta las coordenadas según sea necesario
        add(secondPlaceLabel);

        JLabel thirdPlaceLabel = new JLabel(formatScore(topScores.get(2)));
        thirdPlaceLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 18));
        thirdPlaceLabel.setForeground(Color.WHITE);
        thirdPlaceLabel.setBounds(260, 400, 200, 50); // Ajusta las coordenadas según sea necesario
        add(thirdPlaceLabel);

        // Agregar los lugares 4 y 5 abajo del podio
        JLabel fourthPlaceLabel = new JLabel("4. "+formatScore(topScores.get(3)));
        fourthPlaceLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 18));
        fourthPlaceLabel.setForeground(Color.WHITE);
        fourthPlaceLabel.setBounds(50, 525, 400, 50); // Ajusta las coordenadas según sea necesario
        add(fourthPlaceLabel);

        JLabel fifthPlaceLabel = new JLabel("5. "+formatScore(topScores.get(4)));
        fifthPlaceLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 18));
        fifthPlaceLabel.setForeground(Color.WHITE);
        fifthPlaceLabel.setBounds(50, 550, 400, 50); // Ajusta las coordenadas según sea necesario
        add(fifthPlaceLabel);

         // Cargar la imagen del podio
         ImageIcon podiumImage = new ImageIcon(getClass().getResource("/img/podium.png"));
         Image imagedimension = podiumImage.getImage().getScaledInstance(400, 400,Image.SCALE_SMOOTH);
         ImageIcon finalimage = new ImageIcon(imagedimension);
         JLabel podiumLabel = new JLabel(finalimage);
         podiumLabel.setBounds(-65, 100, podiumImage.getIconWidth(), podiumImage.getIconHeight());
         add(podiumLabel);

         JButton back = new JButton("Back");
            back.setBounds(300, 10, 75, 50);
            back.setContentAreaFilled(false);
            back.setFocusPainted(false);
            back.setBorder(BorderFactory.createEmptyBorder());
            back.setForeground(Color.WHITE);
            back.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 30));
            back.addActionListener(e -> {
                dispose();
                Inicial inicial = new Inicial();
                musicPlayer.stopMusic();
            });
            add(back);
 

    
        // Configurar el JFrame
        setTitle("Score");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Cargar y redimensionar el GIF
        ImageIcon gif = new ImageIcon(getClass().getResource("/img/space.gif"));
        ImageIcon scaledGif = new ImageIcon(scaleGif(gif, getWidth(), getHeight()));

        // Crear un JLabel con el GIF redimensionado
        JLabel gifLabel = new JLabel(scaledGif);

        // Añadir el JLabel al JFrame
        add(gifLabel, BorderLayout.CENTER);

        // Hacer visible la ventana
        setVisible(true);
    }

    private String formatScore(Score score) {
        return String.format("%s: %d", score.getName(), score.getScore());
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







