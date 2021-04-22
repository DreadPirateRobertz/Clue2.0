package clueGame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SimpleAudioPlayer {
 //Not sure how to link my sound card/mixer with Ubuntu and IntelliJ but is playing fine on Windows
        //Fix^
        Clip clip;
        AudioInputStream audioInputStream;

        // constructor to initialize streams and clip
        public SimpleAudioPlayer(String filePath) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
            clip = AudioSystem.getClip();
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip.open(audioInputStream);
        }
        // Method to play the audio
        public void play()
        {
            //start the clip
            clip.start();
        }
    }

