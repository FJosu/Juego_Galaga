package practica2;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private List<Bullet> bullets;
    private List<Items> items; 
    private JLayeredPane panel;
    private Enemies enemigosThread;
    private ImageIcon backgroundImage;
    private MusicPlayer2 musicPlayer2;
    private Timer itemTimer;
    private int timeRemaining22;
    private JLabel timeLabel;
    private JLabel pointsLabel;
    private long lastShootTime = 0;
    private final long shootCooldown = 500;
    private JFrame mainFrame;
    private MusicPlayer musicPlayer;
    private volatile boolean timerunning = true;

    
    public Game(JFrame frame) {
        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\song04.wav");
        this.mainFrame = frame;
        setFocusable(true);
        setPreferredSize(new Dimension(1000, 600));

        backgroundImage = new ImageIcon(getClass().getResource("/img/fondo.gif"));

        player = new Player(50, 300); 

        bullets = new ArrayList<>();
        items = new ArrayList<>(); 
        panel = new JLayeredPane();
        panel.setPreferredSize(new Dimension(1000, 600));

        add(panel);

        enemigosThread = new Enemies(panel);
        enemigosThread.start();

        for (Game.Enemy enemy : enemigosThread.getEnemies()) {
            panel.add(enemy.getEnemyLabel());
        }

        timer = new Timer(10, this);
        timer.start();

        // Inicializar el temporizador del juego con 90 segundos
        timeRemaining22 = 90;
        timeLabel = new JLabel("Time: " + timeRemaining22 + "s");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 30));
        timeLabel.setBounds(70, 10, 200, 30);
        panel.add(timeLabel);

        ImageIcon clockIcon = new ImageIcon(getClass().getResource("/img/cronometro.png"));
        Image clockImage = clockIcon.getImage().getScaledInstance(40, 60, Image.SCALE_SMOOTH);
        clockIcon = new ImageIcon(clockImage);
        JLabel clockLabel = new JLabel(clockIcon);
        clockLabel.setBounds(40, -5, clockIcon.getIconWidth(), clockIcon.getIconHeight());
        panel.add(clockLabel);

        // Inicializar la etiqueta de puntos del jugador
        pointsLabel = new JLabel("Score: 0");
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 25));
        panel.add(pointsLabel);
        pointsLabel.setBounds(600, 10, 300, 25);

        Thread gameTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timerunning && timeRemaining22 > 0) {
                    try {
                        Thread.sleep(1000);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                timeRemaining22--;
                                timeLabel.setText("Time: " + timeRemaining22 + "s");
                                timeLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 25));
                                if (timeRemaining22 == 0) {
                                    timerunning = false; // Parar el hilo
                                    enemigosThread.stopRunning();
                                    musicPlayer2 = new MusicPlayer2();
                                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\GameOver.wav");
                                    showEndGameDialog("¡Game Over!");
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gameTimerThread.start();

        itemTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomItems();
            }
        });
        itemTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                long currentTime = System.currentTimeMillis();
                player.keyPressed(e);
                if (currentTime - lastShootTime >= shootCooldown) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        bullets.add(new Bullet(player.getX() + 20, player.getY() + 7));
                        musicPlayer2 = new MusicPlayer2();
                        musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                        lastShootTime = currentTime;
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    int response = JOptionPane.showConfirmDialog(panel, "¿Deseas salir y reiniciar el juego?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        closeAndOpenInitial();
                    }
                }
                if(e.getKeyCode()== KeyEvent.VK_S){
                    timer.stop();
                stoptime();
                enemigosThread.stopRunning();

                    saveGameData();

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });

        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar el GIF de fondo
        g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);

        player.draw(g2d);
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move();
        for (Bullet bullet : bullets) {
            bullet.move();
        }
        for (Items item : items) {
            item.move();
        }
        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<Items> itemsToRemove = new ArrayList<>(); 

        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 50, 50); 

        for (Bullet bullet : bullets) {
            Rectangle bulletBounds = bullet.getBounds();

            for (Enemy enemy : enemigosThread.getEnemies()) {
                JLabel enemyLabel = enemy.getEnemyLabel();
                if (bulletBounds.intersects(enemyLabel.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemy.takeDamage(1); 
                    if (enemy.getLives() <= 0) {
                        enemiesToRemove.add(enemy);
                        showExplosion(enemyLabel.getX(), enemyLabel.getY());
                        panel.remove(enemyLabel);
                        player.addPoints(enemy.getPoints()); 
                        updatePointsLabel(); 
                        System.out.println("Enemy destroyed!");
                        musicPlayer2 = new MusicPlayer2();
                        musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\explosion.wav");
                    }
                    break;
                }
            }
        }

        // Detectar colisiones con el jugador y los ítems
        for (Items item : items) {
            if (playerBounds.intersects(item.getBounds())) {
                itemsToRemove.add(item);
                panel.remove(item.getItemLabel());
                if (item.getType().equals("points")) {
                    player.addPoints(10); 
                } else if (item.getType().equals("penalization")) {
                    addTime(10); 
                } else if (item.getType().equals("-points")) {
                    player.addPoints(-10);
                } else if (item.getType().equals("-time")) {
                    addTime(-10);
                }
                updatePointsLabel();
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemigosThread.getEnemies().removeAll(enemiesToRemove);
        items.removeAll(itemsToRemove);

        // Comprobar si algún enemigo toca al jugador o llega al límite izquierdo
        for (Enemy enemy : enemigosThread.getEnemies()) {
            JLabel enemyLabel = enemy.getEnemyLabel();
            if (playerBounds.intersects(enemyLabel.getBounds()) || enemyLabel.getX() <= 0) {
                timer.stop();
                stoptime();
                enemigosThread.stopRunning();
                musicPlayer2 = new MusicPlayer2();
                musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\GameOver.wav");
                showEndGameDialog("¡Game Over!");
                return; 
            }
        }

        if (enemigosThread.getEnemies().isEmpty()) {
            showEndGameDialog("¡Has ganado!");
            musicPlayer2 = new MusicPlayer2();
            musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Victory.wav");
            timer.stop();
            stoptime();
        }
    }

    private void generateRandomItems() {
        Random rand = new Random();
        String type;
        if (rand.nextBoolean()) {
            type = "points";
        } else {
            type = rand.nextBoolean() ? "penalization" : rand.nextBoolean() ? "-points" : "-time";
        }
        int x = 1000; 
        int y = rand.nextInt(550);
        Items item = new Items(type, x, y);
        items.add(item);
        panel.add(item.getItemLabel());
    }

    private void addTime(int seconds) {
        timeRemaining22 += seconds;
        timeLabel.setText("Time: " + timeRemaining22 + "s");
        timeLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 30));
    }

    private void updatePointsLabel() {
        pointsLabel.setText("Score: " + player.getPoints());
        pointsLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 30));
    }

    private void showExplosion(int x, int y) {
        ImageIcon explosionIcon = new ImageIcon(getClass().getResource("/img/explosion13.png"));
        JLabel explosionLabel = new JLabel(explosionIcon);
        explosionLabel.setBounds(x, y, explosionIcon.getIconWidth(), explosionIcon.getIconHeight());
        panel.add(explosionLabel, JLayeredPane.PALETTE_LAYER);

        Timer explosionTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(explosionLabel);
                panel.repaint();
            }
        });
        explosionTimer.setRepeats(false);
        explosionTimer.start();
    }

    class Enemies extends Thread {
        Position enemyPosition;
        private ArrayList<Enemy> enemies;
        private JLayeredPane panel;
        private int dy = 2;  
        private boolean running = true;

        public void setEnemies(List<Enemy> newEnemies) {
            this.enemies = new ArrayList<>(newEnemies);
            for (Enemy enemy : newEnemies) {
                panel.add(enemy.getEnemyLabel());
            }}

        public Enemies(JLayeredPane panel) {
            this.panel = panel;
            enemies = new ArrayList<>();
            createEnemies();
        }

        private void createEnemies() {
            int horizontalSpacing = 62; 
            int verticalSpacing = 60;   
            int life;
            int points;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 5; j++) {
                    ImageIcon enemigoIcon;
                    if (j == 0) {
                        enemigoIcon = new ImageIcon(getClass().getResource("/img/row1.png"));
                        life = 2;
                        points = 10;
                    } else if (j == 1 || j == 2) {
                        enemigoIcon = new ImageIcon(getClass().getResource("/img/row34.png"));
                        life = 3;
                        points = 20;
                    } else {
                        enemigoIcon = new ImageIcon(getClass().getResource("/img/row45.png"));
                        life = 4;
                        points = 30;
                    }

                    Image enemyImage = enemigoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
                    enemigoIcon = new ImageIcon(enemyImage);

                    JLabel enemyLabel = new JLabel(enemigoIcon);
                    enemyLabel.setBounds(700 + j * horizontalSpacing, 50 + i * verticalSpacing, 40, 40); 

                    enemies.add(new Enemy(enemyLabel, life, points));
                }
            }
        }

        public ArrayList<Enemy> getEnemies() {
            return enemies;
        }

        public void moveEnemies() {
            for (Enemy enemy : enemies) {
                JLabel enemyLabel = enemy.getEnemyLabel();
                enemyLabel.setLocation(enemyLabel.getX(), enemyLabel.getY() + dy);
                if (enemyLabel.getY() >= panel.getHeight() - 65 || enemyLabel.getY() <= 0) {
                    dy = -dy;  
                    for (Enemy en : enemies) {
                        en.getEnemyLabel().setLocation(en.getEnemyLabel().getX() - 20, en.getEnemyLabel().getY()); 
                    }
                    break;
                }
            }
        }

        public void stopRunning() { 
            running = false;
        }

        @Override
        public void run() {
            while (running) { 
                moveEnemies();
                panel.repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    running = false; 
                }
            }
        }
    }

    class Enemy {
        private JLabel enemyLabel;
        private int lives;
        private int points;

        public Enemy(JLabel enemyLabel, int lives, int points) {
            this.enemyLabel = enemyLabel;
            this.lives = lives;
            this.points = points;
        }

        public JLabel getEnemyLabel() {
            return enemyLabel;
        }

        public int getLives() {
            return lives;
        }

        public void takeDamage(int damage) {
            lives -= damage;
        }

        public int getPoints() {
            return points;
        }
    }

    private void closeAndOpenInitial() {
        // Detener todos los temporizadores e hilos
        timer.stop();
        stoptime();
        itemTimer.stop();
        enemigosThread.stopRunning();

        try {
            enemigosThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Eliminar el JPanel del JFrame
        mainFrame.getContentPane().remove(this);
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.dispose();

        Inicial inicial = new Inicial();
    }

    private void showEndGameDialog(String message) {
        int tempscore = player.getPoints();
        String playerName = JOptionPane.showInputDialog(panel, message + "\nIngresa tu nombre: \n" + //
                        "Tu Score es de :"+tempscore, "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            ScoreData scoreManager = new ScoreData("scores.txt");
            scoreManager.saveScore(playerName, player.getPoints());
        }
        closeAndOpenInitial();
    }
    public void stoptime(){
        timerunning = false;
    }

    public void saveGameData() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Guardar datos del juego");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setSelectedFile(new File(String.format("%02d_%02d_%02d_%02d_%02d_%04d.bin",
                java.time.LocalDateTime.now().getHour(),
                java.time.LocalDateTime.now().getMinute(),
                java.time.LocalDateTime.now().getSecond(),
                java.time.LocalDateTime.now().getDayOfMonth(),
                java.time.LocalDateTime.now().getMonthValue(),
                java.time.LocalDateTime.now().getYear())));
        int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();

        List<Enemy> enemies = enemigosThread.getEnemies();
        int playerPoints = player.getPoints();
        int remainingTime = timeRemaining22;

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeInt(enemies.size());
            for (Enemy enemy : enemies) {
                oos.writeInt(enemy.getEnemyLabel().getX());
                oos.writeInt(enemy.getEnemyLabel().getY());
                oos.writeInt(enemy.getLives());
            }

            oos.writeInt(playerPoints);
            oos.writeInt(remainingTime);

            JOptionPane.showMessageDialog(panel, "Datos del juego guardados exitosamente.");
            closeAndOpenInitial();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error al guardar los datos del juego.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private String getImagePathForColumn(int columnIndex) {
    switch (columnIndex) {
        case 0:
            return "/img/row1.png";
        case 1:
        case 2:
            return "/img/row34.png";
        case 3:
        case 4:
            return "/img/row45.png";
        default:
            return ""; 
    }
}

public void loadGameData() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo Bin", "bin");
    fileChooser.setFileFilter(filter);
    fileChooser.setDialogTitle("Cargar datos del juego");

    int userSelection = fileChooser.showOpenDialog(panel);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToLoad = fileChooser.getSelectedFile();
        String filePath = fileToLoad.getAbsolutePath();

        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            int numberOfEnemies = ois.readInt();
            List<Enemy> loadedEnemies = new ArrayList<>();

            for (int i = 0; i < numberOfEnemies; i++) {
                int x = ois.readInt();
                int y = ois.readInt();
                int lives = ois.readInt();
                int column = (x - 600) / 62; 
                String imagePath = getImagePathForColumn(column);
                ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
                Image originalImage = originalIcon.getImage();
    
                Image resizedImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImage);
                JLabel enemyLabel = new JLabel(resizedIcon);
                enemyLabel.setBounds(x, y, 40, 40);
                // Ajustar los puntos basados en la columna
                int points;
                if (column == 0) {
                    points = 10;
                } else if (column == 1 || column == 2) {
                    points = 20;
                } else if (column == 3 || column == 4) {
                    points = 30;
                } else {
                    points = 0; 
                }
                loadedEnemies.add(new Enemy(enemyLabel, lives, points));
            }
            

            int playerPoints = ois.readInt();
            int remainingTime = ois.readInt();
            enemigosThread.stopRunning(); 
            for (Enemy enemy : enemigosThread.getEnemies()) {
                panel.remove(enemy.getEnemyLabel());
            }
    
            // Limpiar la lista de enemigos
            enemigosThread.getEnemies().clear();
            panel.revalidate();
            panel.repaint();

            // Actualizar la lista de enemigos en el juego
            enemigosThread = new Enemies(panel); 
            enemigosThread.setEnemies(loadedEnemies); 
            player.setPoints(playerPoints);
            timeRemaining22 = remainingTime+3;

            updatePointsLabel();
            timeLabel.setText("Time: " + timeRemaining22 + "s");
            
            JOptionPane.showMessageDialog(panel, "Datos del juego cargados exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error al cargar los datos del juego.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
public void Startgame(){
    
    timer.start();
    itemTimer.start();
    enemigosThread.start(); 
    
}
    
}
