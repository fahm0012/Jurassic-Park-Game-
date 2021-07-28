package game.weapon;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * This class represents a Laser which is used by Player to Kill Stegosaur
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see WeaponItem
 */
public class Laser extends WeaponItem {
    /**
     * This method creates instance of laser class
     */
    public Laser(){
        super("laser",'-', (int) Math.floor(Math.random()*(100-50+1)+50),"shoots");
    }
}

