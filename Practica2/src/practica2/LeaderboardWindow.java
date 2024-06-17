package practica2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardWindow extends JFrame {
    private ScoreManager scoreManager;

    public LeaderboardWindow(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        setTitle("Top 5 Scores");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal con fondo de espacio
        JPanel mainPanel = new BackgroundPanel(new ImageIcon("path_to_space_background.jpg").getImage());
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Top 5 Scores");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, gbc);

        List<Score> topScores = scoreManager.getTopScores(5);
        JPanel podiumPanel = new JPanel();
        podiumPanel.setLayout(new GridBagLayout());
        podiumPanel.setOpaque(false);

        // Agregar posiciones al podio
        for (int i = 0; i < 3; i++) {
            gbc.gridx = i;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 2 - i;
            String positionText = String.format("%d. %s: %d", i + 1, topScores.get(i).getName(), topScores.get(i).getScore());
            JLabel positionLabel = new JLabel(positionText);
            positionLabel.setFont(new Font("Arial", Font.BOLD, 20));
            positionLabel.setForeground(Color.WHITE);
            positionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            podiumPanel.add(positionLabel, gbc);
        }

        // Agregar los lugares 4 y 5 abajo del podio
        for (int i = 3; i < 5; i++) {
            gbc.gridx = i - 3;
            gbc.gridy = 3;
            gbc.gridwidth = 3;
            gbc.gridheight = 1;
            String positionText = String.format("%d. %s: %d", i + 1, topScores.get(i).getName(), topScores.get(i).getScore());
            JLabel positionLabel = new JLabel(positionText);
            positionLabel.setFont(new Font("Arial", Font.BOLD, 20));
            positionLabel.setForeground(Color.WHITE);
            mainPanel.add(positionLabel, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        mainPanel.add(podiumPanel, gbc);

        add(mainPanel);
    }

    // Panel con imagen de fondo
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

