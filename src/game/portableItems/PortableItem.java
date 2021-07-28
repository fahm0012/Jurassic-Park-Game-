package game.portableItems;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

/**
 * Base class for any item that can be picked up and dropped.
 */
public class PortableItem extends Item {
	/**
	 * counter for the items which are placed on ground
	 */
	private  int counter = 0;

	/**
	 * This method creates instance of PortableItem class.
	 * @param name of the portable item
	 * @param displayChar char of that item that is displayed on map
	 */
	public PortableItem(String name, char displayChar) {
		super(name, displayChar, true);
	}

	/**
	 * This method runs on every turn for each portable item and in this method it removes the corpse from the ground
	 * after a certain period of time
	 * @param currentLocation The location of the ground on which we lie.
	 */
	@Override
	public void tick(Location currentLocation) {
		if(displayChar == '%'){
			counter++;
			if((name.contains("Stegosaur") || name.contains("Allosaur")) && counter > 20)
				currentLocation.removeItem(this);
			else if(counter >= 40)
				currentLocation.removeItem(this);
		}
	}

	/**
	 * @return number of turns for that item when it was placed on ground .
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @return the name of the PortableItem
	 */
	public String getName(){return name;}



}
