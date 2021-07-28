package game.portableItems;

import edu.monash.fit2099.engine.Location;

/**
 * This class represents the fruits which are produced on tree or bushes
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see PortableItem
 */
public class Fruit extends PortableItem {

    /**
     * if its true it tells us if Fruit has been dropped
     */
    private boolean hasDropped;

    /**
     * tells us for how long the food has been on ground.
     */
    private int fallenDay;

    /**
     * this method creates a instance of Fruit class
     */
    public Fruit(){
        super("Fruit",'o');

        hasDropped = false;
        fallenDay = 0;
    }

    /**
     * This methods run every turn and increments the days for which the fruit has been fallen for and removes the fruit
     * if it rots.
     * @param currentLocation
     */
    @Override
    public void tick(Location currentLocation) {
        if (this.getHasDropped()) {
            fallenDay++;
            if(fallenDay >= 15){
                currentLocation.removeItem(this);
            }
        }
    }

    /**
     * Getter for hasDropped
     * @return true if the fruit has been dropped
     */
    public boolean getHasDropped(){return hasDropped;}

    /**
     * getter for fallenDay
     * @return the days for which the fruit has been laying on ground
     */
    public int fallenDay(){return fallenDay;}


}
