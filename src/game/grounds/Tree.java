package game.grounds;

import edu.monash.fit2099.engine.*;
import game.portableItems.Fruit;

/**
 * This class represents the Tree present in the game
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Plant
 */
public class Tree extends Plant {
	/**
	 * The age of the fruit which is dropped by the Tree
	 */
	private int age = 0;

	/**
	 * This is a method which creates instance of Tree class.
	 */
	public Tree() {
		super("Tree", 0.5);
		displayChar = '+';
	}

	/**
	 * This methods allows the tree to drop fruits
	 * @return boolean if the tree failed to drop a fruit
	 */
	private boolean dropFruit(){

		if(hasFruits()){
			if(Math.random() <= 0.05) {
				removeFromFruitList();
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * This method runs every turn for every tree in which tree tries to make a fruit and tries to drop a fruit
	 * @param location The location of the Ground
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);
		displayChar = '+';

		makeFruit();

		if(dropFruit()){
			location.addItem(new Fruit());
		}


		//age++;
		//if (age == 10)
		//	displayChar = 't';
		//if (age == 20)
		//	displayChar = 'T';
	}
}
