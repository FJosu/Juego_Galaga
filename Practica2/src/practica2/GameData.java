package practica2;

import java.io.Serializable;
import java.util.List;
import java.awt.Point; // Import the Point class

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L; // Asegura la compatibilidad de versiones

    private List<Point> enemyPositions; // Asume que Point es una clase que almacena posiciones x, y
    private int enemiesRemaining;
    private int points;
    private int timeRemaining;

    // Constructor
    public GameData(List<Point> enemyPositions, int enemiesRemaining, int points, int timeRemaining) {
        this.enemyPositions = enemyPositions;
        this.enemiesRemaining = enemiesRemaining;
        this.points = points;
        this.timeRemaining = timeRemaining;
    }

    // Getters y Setters
    public List<Point> getEnemyPositions() {
        return enemyPositions;
    }

    public void setEnemyPositions(List<Point> enemyPositions) {
        this.enemyPositions = enemyPositions;
    }

    public int getEnemiesRemaining() {
        return enemiesRemaining;
    }

    public void setEnemiesRemaining(int enemiesRemaining) {
        this.enemiesRemaining = enemiesRemaining;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}