package game.portableItems;

import edu.monash.fit2099.engine.Location;

/**
 * This is abstract class for all the different type of eggs
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see PortableItem
 */
public abstract class Egg extends PortableItem {
    public int turnsOnGround;
    /**
     * number of turns for which the egg was on ground
     */
    /**
     * False as egg is a Carnivore meal
     */
    private boolean vegetarian;

    /**
     * when does the egg hatches into a baby dino
     */
    private int hatchingPeriod;


    /**
     * This method forms a new instance of Egg class
     * @param name is the name of the egg( Allosaur Egg, Brachiosaur Egg,Stegosaur Egg)
     * @param c  the symbol which represents egg on the map
     */
    public Egg(String name, char c){
        super(name, c);
        turnsOnGround = 0;
        vegetarian = true;

    }

    /**
     * This is abstract method is run at every turn for each egg.
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
    }

    /**
     * This is abstract method in which egg is hatched
     * @param currentLocation
     */
    public void hatchEgg(Location currentLocation){

    }

    /**
     * @return the time to hatch the egg
     */
    public int getHatchingPeriod() {
        return hatchingPeriod;
    }

    /**
     * Allows to set hatchingPeriod
     * @param hatchingPeriod new hatching period which is to be set.
     */
    public void setHatchingPeriod(int hatchingPeriod) {
        this.hatchingPeriod = hatchingPeriod;
    }
}


