package game.player;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Menu;
import game.actions.QuitAction;
import game.grounds.Plant;


/**
 * Class representing the Player.
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Actor
 *
 */
public class Player extends Actor {

	/**
	 * Menu given to player to make the choice
	 */
	private Menu menu = new Menu();
	/**
	 * This is the ecoPoints of the player so it can buy items from vending machine.Player is given intial 100
	 * ecoPoints at the start of the game
	 */
	public static int ecoPoints;

	/**
	 * number of points needed to win in ChallengeMode of the game
	 */
	public static int pointsToWin;

	/**
	 * turns left to win the game in ChallengeMode
	 */
	public static int turnsToWin;

	/**
	 * when a player chooses the option between challenge  and sandbox
	 * game mode.gameMode would be set to 1 when a challenge mode is chosen
	 */
	public static int gameMode;



	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}

	/**
	 * This method allows player to make action at every turn in the game
	 * @param actions    collection of possible Actions for this Actor
	 * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
	 * @param map        the map containing the Actor
	 * @param display    the I/O object to which messages may be written
	 * @return Action taken by the player
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		if(gameMode == 1){
			challengeMode(display);

			if(turnsToWin < 0){
				return new QuitAction();
			}
		}

		actions.add(new QuitAction());


		display.println("Inventory: "+ getInventory());
		display.println("EcoPoints: " + ecoPoints);

		if(map.locationOf(this).getGround() instanceof Plant){
			actions.add(((Plant)(map.locationOf(this).getGround())).getFruit());
		}



		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		return menu.showMenu(this, actions, display);
	}

	/**
	 * In Challenge Game Mode for a player it tells if Player lost or won and at each turn tells
	 * whats the  points to win the game and number of turns left for the game to finish
	 *
	 * @param display the menu for the player
	 */
	public void challengeMode(Display display){

		display.println("Target EcoPoints: " + pointsToWin);
		display.println("Turns left: " + turnsToWin);

		if(turnsToWin <= 0){
			if(ecoPoints >= pointsToWin){
				display.println("You've won, congrats! EP:" + ecoPoints + "\n");
			}
			else
				display.println("You lost :( EP:" + ecoPoints + "\n");
		}

		turnsToWin--;
	}

	/**
	 * This method increments the ecoPoints of the player as a specific event occurs
	 * @param event event which takes place and will increment the ecopoints of player according to that.
	 */
	public static void ecoPointsInc(String event){

//      Increments the ecoPoints of player by 1 when a ripe fruit is produced by tree
		if (event == "rf") {
			Player.ecoPoints += 1;
		}
//      Increments the ecoPoints of player by 10 when a ripe fruit is harvested from a bush or a tree
		else if(event == "hf"){
			Player.ecoPoints += 10;
		}
//      Increments the ecoPoints of player by 10 when a fruit is fed to a dinosaur
		else if (event == "fd"){
			Player.ecoPoints += 10;
		}
//      Increments the ecoPoints of player by 100 when a stegosaurEgg or PterodactylsEgg hatches.

		else if (event == "sh"){
			Player.ecoPoints += 100;
		}
//      Increments the ecoPoints of player by 1000 when a brachiosaurEgg hatches.

		else if (event == "bh"){
			Player.ecoPoints += 1000;
		}
//      Increments the ecoPoints of player by 1000 when a allosaurEgg hatches.
		else if (event == "ah"){
			Player.ecoPoints += 1000;
		}

	}


}
