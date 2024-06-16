package practica2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<Items> items; // Lista de ítems
    private JLayeredPane panel;
    private Enemies enemigosThread;
    private ImageIcon backgroundImage;
    private MusicPlayer2 musicPlayer2;
    private Timer gameTimer;
    private int timeRemaining;
    private JLabel timeLabel;

    public Game() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        player = new Player(50, 300); // Ajuste para posicionar al jugador correctamente
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        items = new ArrayList<>(); // Inicializar lista de ítems
        panel = new JLayeredPane();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(Color.BLACK);
    
        // Agregar el JLayeredPane al JFrame o contenedor principal
        add(panel);
        
        enemigosThread = new Enemies(panel);
        enemigosThread.start();

        for (JLabel enemyLabel : enemigosThread.getEnemies()) {
            panel.add(enemyLabel);
        }

        timer = new Timer(10, this);
        timer.start();

        // Inicializar el temporizador del juego con 90 segundos
        timeRemaining = 90;
        timeLabel = new JLabel("Tiempo restante: " + timeRemaining + "s");
        timeLabel.setForeground(Color.WHITE);
        panel.add(timeLabel);
        timeLabel.setBounds(10, 10, 200, 30);

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timeLabel.setText("Tiempo restante: " + timeRemaining + "s");
                if (timeRemaining <= 0) {
                    gameTimer.stop();
                    timer.stop();
                    JOptionPane.showMessageDialog(panel, "¡Tiempo agotado!", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        gameTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bullets.add(new Bullet(player.getX() + 20, player.getY() + 7));
                    musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\Practica2\\src\\img\\Shoot.wav");
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
        List<JLabel> enemiesToRemove = new ArrayList<>();
        List<Items> itemsToRemove = new ArrayList<>(); // Lista para ítems a eliminar
    
        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), 50, 50); // Rectángulo del jugador
    
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletBounds = bullet.getBounds();
    
            Iterator<JLabel> enemyIterator = enemigosThread.getEnemies().iterator();
            while (enemyIterator.hasNext()) {
                JLabel enemyLabel = enemyIterator.next();
                if (bulletBounds.intersects(enemyLabel.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemyLabel);
                    panel.remove(enemyLabel);
                    System.out.println("Enemy hit!");
                    musicPlayer2 = new MusicPlayer2();
                    musicPlayer2.playMusic("C:\\Users\\Josue\\OneDrive\\Escritorio\\Practica2\\src\\img\\explosion.wav");
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
                }else if(item.getType().equals("-points")){
                    player.addPoints(-10);
                }else if(item.getType().equals("-time")){

                    addTime(-10);
                }
            }
        }
    
        bullets.removeAll(bulletsToRemove);
        enemigosThread.getEnemies().removeAll(enemiesToRemove);
        items.removeAll(itemsToRemove);
    
        if (enemigosThread.getEnemies().isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Has ganado!", "Juego terminado", JOptionPane.INFORMATION_MESSAGE);
            timer.stop();
            gameTimer.stop();
        }
    }

    private void generateRandomItems() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
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
    }

    private void addTime(int seconds) {
        timeRemaining += seconds;
        timeLabel.setText("Tiempo restante: " + timeRemaining + "s");
    }

    class Enemies extends Thread {
        private ArrayList<JLabel> enemies;
        private JLayeredPane panel;
        private int dy = 2;  // Velocidad vertical
        private boolean movingDown = true;  // Indica si los enemigos se están moviendo hacia abajo

        public Enemies(JLayeredPane panel) {
            this.panel = panel;
            enemies = new ArrayList<>();
            createEnemies();
        }

        private void createEnemies() {
            int horizontalSpacing = 62; // Ajusta este valor para aumentar el espacio horizontal
            int verticalSpacing = 60;   // Ajusta este valor para aumentar el espacio vertical
            
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 5; j++) {
                    ImageIcon enemigoIcon;
                    if (j == 0) {
                        enemigoIcon = new ImageIcon(getClass().getResource("/img/row1.png"));
                    } else if (j == 1 || j == 2) {
                        enemigoIcon = new ImageIcon(getClass().getResource("/img/row34.png"));
                    } else {
                        enemigoIcon = new ImageIcon(getClass().getResource("/img/row45.png"));
                    }

                    Image enemyImage = enemigoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Aumentar tamaño de los enemigos
                    enemigoIcon = new ImageIcon(enemyImage);
                    
                    JLabel enemy = new JLabel(enemigoIcon);
                    enemy.setBounds(700 + j * horizontalSpacing, 50 + i * verticalSpacing, 40, 40); // Ajustar la posición y tamaño de los enemigos
                    enemies.add(enemy);
                }
            }
        }

        public ArrayList<JLabel> getEnemies() {
            return enemies;
        }

        public void moveEnemies() {
            for (JLabel enemyLabel : enemies) {
                enemyLabel.setLocation(enemyLabel.getX(), enemyLabel.getY() + dy);
                if (enemyLabel.getY() >= panel.getHeight() - 65 || enemyLabel.getY() <= 0) {
                    dy = -dy;  // Cambiar de dirección
                    for (JLabel en : enemies) {
                        en.setLocation(en.getX() - 40, en.getY());  // Mover los enemigos a la izquierda
                    }
                    break;
                }
            }
        }

        @Override
        public void run() {
            while (true) {
                moveEnemies();
                panel.repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
