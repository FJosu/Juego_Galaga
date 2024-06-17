package practica2;


import javax.swing.*;

import practica2.Game.Enemies;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private List<Bullet> bullets;
    private List<Items> items; // Lista de ítems
    private JLayeredPane panel;
    private Enemies enemigosThread;
    private ImageIcon backgroundImage;
    private MusicPlayer2 musicPlayer2;
    private Timer gameTimer;
    private Timer itemTimer;
    private int timeRemaining;
    private JLabel timeLabel;
    private JLabel pointsLabel; // Etiqueta para mostrar los puntos del jugador
    private long lastShootTime=0;
    private final long shootCooldown = 300;
    private JFrame mainFrame;
    private MusicPlayer musicPlayer;

    public Game(JFrame frame) {
        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\song04.wav");
        this.mainFrame = frame;
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        player = new Player(50, 300); // Ajuste para posicionar al jugador correctamente

        bullets = new ArrayList<>();
        items = new ArrayList<>(); // Inicializar lista de ítems
        panel = new JLayeredPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(Color.BLACK);

        // Agregar el JLayeredPane al JFrame o contenedor principal
        add(panel);

        enemigosThread = new Enemies(panel);
        enemigosThread.start();

        for (Game.Enemy enemy : enemigosThread.getEnemies()) {
            panel.add(enemy.getEnemyLabel());
        }

        timer = new Timer(10, this);
        timer.start();

        // Inicializar el temporizador del juego con 90 segundos
        timeRemaining = 90;
        timeLabel = new JLabel("Time: " + timeRemaining + "s");
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
        pointsLabel = new JLabel("Sccore: 0");
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 25));
        panel.add(pointsLabel);
        pointsLabel.setBounds(600, 10, 300, 25);

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timeLabel.setText("Time: " + timeRemaining + "s");
                timeLabel.setFont(new Font("LXGW WenKai Mono TC", Font.BOLD, 25));
                if (timeRemaining <= 0) {
                    gameTimer.stop();
                    timer.stop();
                    JOptionPane.showMessageDialog(panel, "¡Tiempo agotado!", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        gameTimer.start();

        itemTimer = new Timer(3000,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                generateRandomItems();
            }
        });
        itemTimer.start();

        addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        long currentTIme = System.currentTimeMillis();
        player.keyPressed(e);
        if (currentTIme - lastShootTime >= shootCooldown) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                bullets.add(new Bullet(player.getX() + 20, player.getY() + 7));
                musicPlayer2 = new MusicPlayer2();
                musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\-IPC1-A-Practica2_202307378\\Practica2\\src\\img\\Shoot.wav");
                lastShootTime = currentTIme;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int response = JOptionPane.showConfirmDialog(panel, "¿Deseas salir y reiniciar el juego?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                closeAndOpenInitial();
                
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }
});

        generateRandomItems();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
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
        List<Items> itemsToRemove = new ArrayList<>(); // Lista para ítems a eliminar
    
        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 50, 50); // Rectángulo del jugador
    
        for (Bullet bullet : bullets) {
            Rectangle bulletBounds = bullet.getBounds();
    
            for (Enemy enemy : enemigosThread.getEnemies()) {
                JLabel enemyLabel = enemy.getEnemyLabel();
                if (bulletBounds.intersects(enemyLabel.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemy.takeDamage(1); // Reducir la vida del enemigo
                    if (enemy.getLives() <= 0) {
                        enemiesToRemove.add(enemy);
                        showExplosion(enemyLabel.getX(), enemyLabel.getY());
                        panel.remove(enemyLabel);
                        player.addPoints(enemy.getPoints()); // Añadir puntos al jugador
                        updatePointsLabel(); // Actualizar la etiqueta de puntos
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
                    player.addPoints(10); // Añadir puntos al jugador
                } else if (item.getType().equals("penalization")) {
                    addTime(10); // Añadir 5 segundos al temporizador del juego
                } else if (item.getType().equals("-points")) {
                    player.addPoints(-10);
                } else if (item.getType().equals("-time")) {
                    addTime(-10);
                }
                updatePointsLabel(); // Actualizar la etiqueta de puntos
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
                gameTimer.stop();
                enemigosThread.stopRunning();
                // Mostrar mensaje de "Game Over"
                showEndGameDialog("¡Game Over!");
                return; // Salir del método después de detectar el Game Over
            }
        }
    
        if (enemigosThread.getEnemies().isEmpty()) {
            showEndGameDialog("¡Has ganado!");
            timer.stop();
            gameTimer.stop();
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
        int x = 800; // Los ítems comenzarán en la posición x = 0
        int y = rand.nextInt(550);
        Items item = new Items(type, x, y);
        items.add(item);
        panel.add(item.getItemLabel());
    }

    private void addTime(int seconds) {
        timeRemaining += seconds;
        timeLabel.setText("Time: " + timeRemaining + "s");
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
    private ArrayList<Enemy> enemies;
    private JLayeredPane panel;
    private int dy = 8;  // Velocidad vertical
    private boolean running = true; // Añadido

    public Enemies(JLayeredPane panel) {
        this.panel = panel;
        enemies = new ArrayList<>();
        createEnemies();
    }

    private void createEnemies() {
        int horizontalSpacing = 62; // Ajusta este valor para aumentar el espacio horizontal
        int verticalSpacing = 60;   // Ajusta este valor para aumentar el espacio vertical
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

                Image enemyImage = enemigoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Aumentar tamaño de los enemigos
                enemigoIcon = new ImageIcon(enemyImage);

                JLabel enemyLabel = new JLabel(enemigoIcon);
                enemyLabel.setBounds(700 + j * horizontalSpacing, 50 + i * verticalSpacing, 40, 40); // Ajustar la posición y tamaño de los enemigos

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
                dy = -dy;  // Cambiar de dirección
                for (Enemy en : enemies) {
                    en.getEnemyLabel().setLocation(en.getEnemyLabel().getX() - 20, en.getEnemyLabel().getY());  // Mover los enemigos a la izquierda
                }
                break;
            }
        }
    }

    public void stopRunning() { // Añadido
        running = false;
    }

    @Override
    public void run() {
        while (running) { // Cambiado
            moveEnemies();
            panel.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                running = false; // Añadido: salir del ciclo cuando se interrumpe el sueño
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
        // Detener todos los temporizadores y hilos
        timer.stop();
        gameTimer.stop();
        itemTimer.stop();
        enemigosThread.stopRunning(); // Modificado
    
        // Esperar a que el hilo de enemigos termine
        try {
            enemigosThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Eliminar el JPanel del JFrame
        mainFrame.getContentPane().remove(this);
        mainFrame.revalidate();
        mainFrame.repaint();
    
        // Abrir el JPanel inicial en el mismo JFrame
        Inicial inicial = new Inicial();
        
    }
    private void showEndGameDialog(String message) {
        String playerName = JOptionPane.showInputDialog(panel, message + "\nIngresa tu nombre:", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            ScoreManager scoreManager = new ScoreManager("scores.txt");
            scoreManager.saveScore(playerName, player.getPoints());
            
        }
        closeAndOpenInitial();
    }
    
    
    
    
}


