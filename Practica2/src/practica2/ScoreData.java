package practica2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreData {
    private String filePath;

    public ScoreData(String filePath) {
        this.filePath = filePath;
    }

    public void saveScore(String name, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(name + ": " + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Score> getTopScores(int topN) {
        List<Score> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    scores.add(new Score(name, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(scores, Comparator.comparingInt(Score::getScore).reversed());
        return scores.subList(0, Math.min(scores.size(), topN));
    }
}
