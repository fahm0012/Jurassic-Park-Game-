package game.grounds;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Exit;
import game.grounds.Bush;

/**
 * This class represents  the dirt which is on the Ground
 * A class that represents bare dirt.
 */

public class Dirt extends Ground {
	/**
	 * This methods creates the instance of Dirt class
	 */
	public Dirt() {
		super('.');
	}

	/**
	 * This methods grows dirt into bush.If dirt is next to two bushes there is 10% chance of dirt to grow in bush
	 * if its not next two bushes nor next to a tree then there is 1% chance of dirt to grow to bush
	 * @param location of current insatnce of dirt
	 */
	private void growBush(Location location){
		double growProbability = 0.01;

		/*
		  checks if dirt is next to two bushes
		 */
		if(nextToBushes(location))
			growProbability = 0.1;
		/*
		  creates a bush randomly according to probability
		 */
		if(Math.random() <= growProbability)
			location.setGround(new Bush());
	}

	/**
	 * This methods checks if a dirt is next to two bushes.
	 * @param here Location of the dirt
	 * @return true if dirt next to two bushes
	 */
	private boolean nextToBushes(Location here){

		int bushCounter;
		bushCounter = 0;

		for(Exit exit: here.getExits()){
			Location destination = exit.getDestination();

			if(destination.getGround() instanceof Bush)
				bushCounter++;
		}

		return bushCounter >= 2;

	}

	/**
	 * This methods checks if dirt is next to a tree
	 * @param here Location of the dirt
	 * @return true if dirt is next to a tree
	 */
	private boolean nextToTree(Location here){

		for(Exit exit: here.getExits()){
			Location destination = exit.getDestination();

			if(destination.getGround().getDisplayChar() == '+')
				return true;
		}

		return false;
	}

	/**
	 * Runs every turn for every dirt and produces bushes on dirt if possible
	 * @param location The location of the dirt
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);

		if(!nextToTree(location))
			growBush(location);
	}

}
