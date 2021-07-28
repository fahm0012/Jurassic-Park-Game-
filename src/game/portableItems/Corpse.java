package game.portableItems;

import edu.monash.fit2099.engine.Location;

/**
 * This class represents the Corpse of the dinos
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see PortableItem
 */
public class Corpse extends PortableItem{


    private int hp;
    /**
     * number of turns for which the corpse is on ground
     */
    private int counter = 0;

    public Corpse(String name, char displayChar, int hp) {
        super(name,displayChar);
        this.hp = hp;
    }

    /**
     * Runs for corpse at each turn so it can remove corpse from ground after a certain time for each dino
     * @param currentLocation The location of the ground on which corpse lie.
     */
    @Override
    public void tick(Location currentLocation) {
        if(displayChar == '%'){
            counter++;
            if((name.contains("Stegosaur") || name.contains("Allosaur")) && counter >= 20)
                currentLocation.removeItem(this);
            else if(counter >= 40)
                currentLocation.removeItem(this);
        }
    }

    public void decHP(int hp){ this.hp -= hp;}

    public int getHp() { return hp; }
}
