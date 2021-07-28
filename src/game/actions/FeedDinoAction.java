package game.actions;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedDinoAction classes allows the HungryDino (dinos) to make a action to eat food if its possible.
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Action
 */
public class FeedDinoAction extends Action {
    /**
     * target dino which is being fed
     */
    private HungryDino target;

    /**
     * List of items present in players inventory which can be fed to the target dino
     */
    private List<Item> playerFood = new ArrayList<Item>();

    /**
     * direction toward target dino to feed it
     */
    private String direction;

    /**
     * display to show
     */
    private Display display = new Display();


    /**
     * This method forms the instance of FeedDinoAction
     * @param target dino which is being fed
     * @param direction direction toward the dino in order to feed it
     */
    public FeedDinoAction(HungryDino target, String direction){
        this.target = target;
        this.direction = direction;
    }

    /**
     * This method tells what did a player fed to a dino or if it has nothing to feed
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return String which tells what did a player fed to a dino or if it has nothing to feed
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        /*
          this represents the choice made by Player to feed what item to the dino
         */
        int choice;

        /*
          looks into the players inventory to see what items it can feed to the current target dino
         */
        searchFoodInInventory(actor);

        /*
          if player has no items or no items which can be fed to
          the dino then it returns that player has nothing to feed dino
         */
        if(playerFood.isEmpty())
            return actor + " has nothing to feed";

        /*
          displays the health of the target dino and its maximum food level
         */
        display.println(target +"'s food level: "+target.getFoodLevel() + "/"+ target.getMaxFoodLevel());

        /*
          choice made by the player to feed what item to dino after looking at how hungry is our target dino
         */
        choice = chooseOption();

        /*
          retrieve the item chosen by the player to feed
         */
        Item food = playerFood.get(choice-1);

        /*
          feeds the dino
         */
        feedDino(food,actor);

        return  "You fed " + food + ", "+target+"'s food level: " + target.getFoodLevel() + "/" + target.getMaxFoodLevel();

    }

    /**
     * This methods goes through players inventory and add items into playerFood list which is the list of items present
     * in the players inventory which can be fed to target dino
     * @param actor this is the target dino which need to bed fed
     */
    private void searchFoodInInventory(Actor actor){
        boolean vegKitFlag = false;
        boolean fruitFlag = false;

        for(Item item: actor.getInventory()){
            if(target.getEatingType() == 'H'){
                if(item.getDisplayChar() == 'V' && !vegKitFlag){
                    playerFood.add(item);
                    vegKitFlag = true;
                }
                else if(item.getDisplayChar() == 'o' && !fruitFlag){
                    playerFood.add(item);
                    fruitFlag = true;
                }
            }
            else if(target.getEatingType() == 'C' && item.getDisplayChar() == 'C'){
                playerFood.add(item);
                break;
            }
            if(vegKitFlag && fruitFlag){break;}
        }
    }

    /**
     * This method finds the index of item in player inventory which was selected by player so it can fed to the
     * target dino
     * @return the index of the item in player inventory which is to be fed to target dino.
     */
    private int chooseOption(){
        int index = 1;
        char c;
        int choice = 1;

        for(Item item: playerFood){
            display.println(index + ": feed " + item);
            index++;
        }

        boolean validOption = false;
        while(!validOption){
            c = display.readChar();
            choice = c - '0';

            if(choice < 1 || choice > playerFood.size())
                display.println("Invalid option, select again");
            else
                validOption = true;
        }
        return choice;
    }

    /**
     * This methods feeds the dino.
     * @param food the food(item) that is fed to the target dino
     * @param actor this is our target dino
     */
    private void feedDino(Item food,Actor actor){
        if(food.getDisplayChar() == 'V' || food.getDisplayChar() == 'C'){
            target.setFoodLevel(target.getMaxFoodLevel());
        }
        else if(food.getDisplayChar() == 'o'){
            int level = Math.min(20,target.getMaxFoodLevel()-target.getFoodLevel());
            target.setFoodLevel(target.getFoodLevel()+level);
            Player.ecoPointsInc("fd");
        }
        actor.removeItemFromInventory(food);


    }

    /**
     * gives the description of player feeding what target dino at what direction
     * @param actor The actor performing the action.
     * @return String telling the description of player feeding what target dino at what direction
     */
    @Override
    public String menuDescription(Actor actor){return actor + " feeds " + target + " to the " + direction;}


}
