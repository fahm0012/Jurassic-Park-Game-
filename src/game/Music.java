package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

/**
 * This class plays the music for the game.
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 */
public class Music {
    /**
     * stop set to True when we want to stop the music when the game ends
     */
    public static boolean stop = false;

    void playMusic(String musicLoc)
    {


        try
        {
            File mPath = new File(musicLoc);

            if(mPath.exists()){

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(mPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);

                if(stop){
                    clip.stop();
                    stop = false;
                }


                //JOptionPane.showMessageDialog(null,"Turn off music");
            }
            else {
                System.out.println("NF");
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
