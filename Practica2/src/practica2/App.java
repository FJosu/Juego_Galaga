package practica2;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        ScoreManager scoreManager = new ScoreManager("scores.txt");
        LeaderboardWindow leaderboardWindow = new LeaderboardWindow(scoreManager);
        
        leaderboardWindow.setVisible(true);
        /* 
        SwingUtilities.invokeLater(() -> {
            new Inicial().setVisible(true);
        });
        */
    }
    
        
    
    }
    

