package game.dinosaur;

import java.util.*;

import edu.monash.fit2099.engine.*;
import game.actions.*;
import game.behaviours.AttackStegosaurBehaviour;
import game.behaviours.*;
import game.behaviours.MoveToFoodSource;
import game.player.Player;

import java.util.ArrayList;


/**
 * This class represents a Pterodactyls dino
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Action
 */
public class Stegosaur extends HungryDino {
	// Will need to change this to a collection if Stegosaur gets additional Behaviours.
	//private Behaviour behaviour;

	/**
	 * The list of behaviours to be done by Stegosaur according to the priority of behaviour.The first behaviour in the list has the most priority
	 */
	private List<Behaviour> actionFactories = new ArrayList<Behaviour>();

	/**
	 * Instance of Random module
	 */
	private Random rand = new Random();
	/**
	 * tells if a Stegosaur is wounded
	 */
	private boolean woundedState;

	/**
	 * tells for how many turns it has been wounded for
	 */
	private  int turnsWounded;

	/** 
	 * Constructor.
	 * All Stegosaurs are represented by a 'd' and have 100 hit points.
	 * @param name the name of this Stegosaur
	 * @param gender of the Stegosaur
	 * @param growthState A char represents  Stegosaur is Adult and C char represents Stegosaur is a child
	 * @param hitPoints   current food level of Stegosaur
	 */
	public Stegosaur(String name, char gender, char growthState, int hitPoints) {
		this.name = name;
		this.gender = gender;
		this.growthState = growthState;
		this.hitPoints = hitPoints;
		displayChar = 'd';
		maxHitPoints = 100;
		minHitPoints = 90;
		ageToBecomeAdult = 30;
		eatingType = 'H';
		incubationPeriodTurns = 10;
		turnsBeingUnconscious = 0;
		turnsToBecomeCorpse = 20;
		waterLvl = 60;
		maxWaterLvl = 100;
		minWaterLvl = 40;
		turnsUnconThirst = 0;
		mating = false;

		if (growthState == 'A')
			age = 30;
		else
			age = 0;

		woundedState = false;
		turnsWounded = 0;

		//mating = false;


		actionFactories.add(new EatBehaviour());
		actionFactories.add(new MoveToWaterSource());
		actionFactories.add(new MoveToMateBehaviour());
		actionFactories.add(new MoveToFoodSource());
		actionFactories.add(new WanderBehaviour());
	}

	/**
	 * This methods tells what actions can be done on Stegosaur by other actors.
	 * @param otherActor the Actor that might be performing attack
	 * @param direction  String representing the direction of the other Actor
	 * @param map        current GameMap
	 * @return  List of Actions which can be done by other actors on Stegosaur.
	 */
	@Override
	public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {

		Actions list = super.getAllowableActions(otherActor,direction,map);
		if (this.isConscious())
			list.add(new AttackAction(this));

		list.add(new FeedDinoAction(this,direction));

		if(!(otherActor instanceof Player))
			list.add(new MateBehaviour(this));

		if(otherActor instanceof Allosaur)
			list.add(new AttackStegosaurBehaviour(this));


		return list;
	}
	/**
	 * This method runs at each turn and in this method Stegosaur makes decision to do a specific action
	 * @param actions    collection of possible Actions for this Actor
	 * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
	 * @param map        the map containing the Actor
	 * @param display    the I/O object to which messages may be written
	 * @return Action chosen by Stegosaur at each turn
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

		updateHealthStats(map);

		//display.println("WL: " + waterLvl + ",FL: " + hitPoints);
		//display.println("TUT:" + turnsUnconThirst);

		if(!isConscious() || !isConsciousThirst()){
			displayChar = 'u';
			return new DoNothingAction();
		}
		else
			displayChar = 'd';

		if(woundedState){
			if(turnsWounded == 20)
				woundedState = false;
			else
				turnsWounded++;
		}

		if(hasEgg){
			//display.println("turnsE:"+turnsHasEgg);
			if(turnsHasEgg == incubationPeriodTurns){
				return new LayEggAction();
			}
			else
				turnsHasEgg++;
		}

		for(Action action: actions){
			if(action instanceof DrinkAction){
				if(!map.contains(this))
					break;
				if(waterLvl < minWaterLvl){
					return action;
				}
			}
		}


		//actionFactories.add(0, new DrinkBehaviour(actions));
		actionFactories.add(0, new MateDriver(actions));

		for (Behaviour factory : actionFactories) {
			if(!map.contains(this))
				break;
			Action action = factory.getAction(this, map);
			if(action != null){
				//System.out.println(name + " :"+factory);
				cleanse(actionFactories);
				//System.out.println("IM CALLED:" + action.menuDescription(this));
				return action;
			}
		}
		cleanse(actionFactories);
		return new DoNothingAction();
	}

	/**
	 * @return if Stegosaurs is wounded
	 */
	public boolean isWoundedState() {
		return woundedState;
	}

	/**
	 * when a Stegosaurs is attacked its woundedState is set to True
	 * @param woundedState tells if Stegosaurs was attacked
	 */
	public void setWoundedState(boolean woundedState) {
		this.woundedState = woundedState;
	}

	/**
	 * This methods removes the MateDriver from actionfactories for Stegosaur after mating
	 * @param actionFactories List of behaviours Stegosaur can do
	 */
	private void cleanse(List<Behaviour> actionFactories){
		actionFactories.removeIf(factory -> factory instanceof MateDriver);
	}
}
