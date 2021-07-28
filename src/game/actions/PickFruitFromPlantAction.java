package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.portableItems.Fruit;
import game.player.Player;
import game.grounds.Plant;

/**
 * PickFruitFromPlantAction  allows the Player to take a action to pick fruit from a Tree or a Bush and tells if the Player was able to pick fruit.
 * to pick the fruit
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Action
 */
public class PickFruitFromPlantAction extends Action {
    /**
     * plant from which fruit is to be picked
     */
    private Plant plant;

    /**
     * This method makes a instance of  PickFruitFromPlantAction class
     * @param plant from which fruit has to be picked
     */
    public PickFruitFromPlantAction(Plant plant){ this.plant = plant;
    }

    /**
     * This method checks if plant has fruits on it and if it does then it allows the player to pick fruit from
     * the plant and removes it from the map and the plants inventory and increments players ecopoints as it
     * harvests a fruit.Player has 60% chance of failing to harvest fruit
     * @param actor The actor performing the action to harvest the fruit from plant(bush or tree)
     * @param map The map the actor is on.
     * @return String which tells us if player was able to harvest fruit or if there was no fruits in plant.
     */
    @Override
    public String execute(Actor actor, GameMap map){

        if(!plant.hasFruits())
            return "Tree/bush has no fruits";

        if(Math.random() <= 0.4){
            actor.addItemToInventory(new Fruit());
            plant.removeFromFruitList();
            Player.ecoPointsInc("hf");
            return actor + " successfully finds and picks a fruit";
        }
        else{
            return actor + " searches the tree/bush, but could not find any ripe fruits";
        }

    }

    /**
     * @param actor The actor performing the action.
     * @return String which tells that actor looked into trees and bushes
     */
    @Override
    public String menuDescription(Actor actor){ return actor + " searches tree/bush for fruit";}
}
