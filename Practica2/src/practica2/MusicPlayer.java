package practica2;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {

    private Clip clip;

    public void playMusic(String filePath) {
        try {
            // Abrir el archivo de audio
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Obtener el clip de audio
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Hacer que la música se repita en loop
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Iniciar la reproducción
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
        }
    }

}
