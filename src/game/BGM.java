package game;

/**
 * This class is a driver class which creates  a instance of Music and plays music for the game
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 */
public class BGM {

    public static void Play(){

        String path = "bgm.wav";

        Music ost = new Music();
        ost.playMusic(path);



    }
}
